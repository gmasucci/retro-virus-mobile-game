package mwgd;

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.media.*;
import javax.microedition.midlet.MIDletStateChangeException;
import java.util.Random;

public class MainCanvas
        extends GameCanvas
        implements Runnable{

    //reference to the midlet so we can call destroyApp();
    private MainMidlet parent;
    private int SLEEPTIME;
    private static final int IMAGE = 0;
    private static final int BUTTON = 1;
    private static final int BACKGROUND = 2;
    private int horCenter = getWidth()/2;
    private int vertCenter = getHeight()/2;

    //sprites
    private Sprite[]        aboutSp;
    private Sprite[]        exitSp;
    private Sprite[]        hiscoreSp;
    private Sprite[]        startSp;
    private Sprite          titleSp;
    private Sprite		gameIntroOne;
    private Sprite		gameBG1Sp;
    private Sprite		gameBG2Sp;
    private Sprite		gameBG3Sp;
    private HeroSprite	hs;
    private Sprite		heroSp;
    private Sprite		hBulletSp[];
    private Sprite		vBulletSp;

    //static images,
    private Image           titleImg;
    private Image           rvImg;
    private Image           vImg;
    private Image           bgImg;
    private Image           aboutBGImg;
    private Graphics        g;

    // title positions
    private int titlex = 200;
    private int titley = 100;

    static private int rot =0;
    private int menuChoice = 0;
    private int keycounter = 0;

    // Application state
    private int MODE;
    private static final int MENU = 0;
    private static final int ABOUT = 1;
    private static final int GAME = 2;

    // "GAME" application state sub states;
    private int GAMESTATE;
    private static final int INTRO = 0;
    private static final int PLAYING = 1;
    private static final int OUTRO = 2;

    private static final int NUMBADDIES = 12;
    private static final int MAXHEROBULLETS = 10;

    //sound
    Player menuTune;
    Player menuEffect;
    Player gameTune;
    private Class storedClass;
    private Enemy enemy[];
    Thread th;
    private boolean heroFired[];
    private int bulletcounter;
    private int shotTimer;

        public MainCanvas(final MainMidlet m) throws IOException{
            super( true );
	    storedClass = getClass();
            g = getGraphics();
	    this.setFullScreenMode(true);
            
	    MODE = MENU;
	    GAMESTATE = INTRO;

	    //assign our MainMidlet pointer
	    parent = m;

	    //  set the horizontal and vertical centers
            horCenter = getWidth()/2;
            vertCenter = getHeight()/2;

            //  load some images
            bgImg   =   Utils.loadImage("BG", BACKGROUND);
            aboutBGImg = Utils.loadImage("aboutBG", BACKGROUND);
            rvImg   =   Utils.loadImage("retroHD", IMAGE);
            vImg    =   Utils.loadImage("virusHD", IMAGE);
            titleImg =  Utils.loadImage("title", IMAGE);

            //  temporary sprite for the title
            titleSp = new Sprite(titleImg);
            titleSp.defineReferencePixel(titleSp.getWidth()/2, titleSp.getHeight()/2);
            

            //  About button
            aboutSp = new Sprite[2];
            aboutSp[0] = new Sprite(Utils.loadImage("aboutoff",BUTTON));
            aboutSp[1] = new Sprite(Utils.loadImage("abouton",BUTTON));
            
            //  Exit button
            exitSp  = new Sprite[2];
            exitSp[0] = new Sprite(Utils.loadImage("exitoff",BUTTON));
            exitSp[1] = new Sprite(Utils.loadImage("exiton",BUTTON));
            
            //  start button
            startSp = new Sprite[2];
            startSp[0] = new Sprite(Utils.loadImage("startoff",BUTTON));
            startSp[1] = new Sprite(Utils.loadImage("starton",BUTTON));
            
            //  hiscore button
            hiscoreSp = new Sprite[2];
            hiscoreSp[0] = new Sprite(Utils.loadImage("hisoff",BUTTON));
            hiscoreSp[1] = new Sprite(Utils.loadImage("hison",BUTTON));

            //  sound
            menuTune = Utils.playWav("tune.wav", storedClass);
            menuEffect = Utils.playWav("starteffect.wav", storedClass);
	    gameTune = Utils.playWav("gameTune.wav", storedClass);
	    

            // set sprite positions

            // off sprites
            aboutSp[0].setPosition(horCenter - (10 + aboutSp[0].getWidth()/2), vertCenter);
            startSp[0].setPosition(horCenter - (10 + startSp[0].getWidth()/2), vertCenter-30);
            hiscoreSp[0].setPosition(horCenter - (10 + hiscoreSp[0].getWidth()/2), vertCenter+30);
            exitSp[0].setPosition(horCenter - (10 + exitSp[0].getWidth()/2), vertCenter +60);
            // on sprites
            aboutSp[1].setPosition(horCenter - (10 + aboutSp[0].getWidth()/2), vertCenter);
            startSp[1].setPosition(horCenter - (10 + startSp[0].getWidth()/2), vertCenter-30);
            hiscoreSp[1].setPosition(horCenter - (10 + hiscoreSp[0].getWidth()/2), vertCenter+30);
            exitSp[1].setPosition(horCenter - (10 + exitSp[0].getWidth()/2), vertCenter +60);

	    gameIntroOne = new Sprite(Utils.loadImage("WARNING1", IMAGE));
	    gameIntroOne.setPosition(0, getHeight());

	    gameBG1Sp = new Sprite(Utils.loadImage("gameBG1", BACKGROUND));
	    gameBG2Sp = new Sprite(Utils.loadImage("gameBG2", BACKGROUND));
	    gameBG3Sp = new Sprite(Utils.loadImage("gameBG3", BACKGROUND));

	    hBulletSp = new Sprite[MAXHEROBULLETS];
	    heroFired = new boolean[MAXHEROBULLETS];


	    for (int i = 0; i < MAXHEROBULLETS; i++) {
		hBulletSp[i] = new Sprite(Utils.loadImage("heroBullet", IMAGE));
	    }
	    vBulletSp = new Sprite(Utils.loadImage("enemyBullet", IMAGE));

	    for (int i = 0; i < MAXHEROBULLETS; i++) {
		hBulletSp[i].setPosition(-10, -10);
	    }
	    vBulletSp.setPosition(-10, -10);

	    hs = new HeroSprite();
	    heroSp = hs.getHeroSp();
	    heroSp.setFrame(0);
	    heroSp.setPosition(horCenter, getHeight()- heroSp.getHeight());
	    createEnemies();
        }

	private void createEnemies() throws IOException{
	    enemy = new Enemy[NUMBADDIES];
	    for(int i=0;i<NUMBADDIES;i++){
		Random j = new Random();
		j.setSeed(i+System.currentTimeMillis());
		int k = j.nextInt(216);
		enemy[i] = new Enemy(k);
		 
	    }
	}
        public void startMe(){
            th = new Thread(this);
            th.start();
            try {
                menuEffect.start();
		SLEEPTIME = 40;
            } catch (MediaException ex) {
                System.out.println("Error: " + ex.toString());
            }
        }

        public void processKeys() throws MediaException, IOException{

		switch(MODE){
		    case MENU:
			menuKeys();
			break;
		    case ABOUT:
			aboutKeys();
			break;
		    case GAME:
			gameKeys();
			break;
		    default:
			break;

		}

        }

        public void updateMe() throws MediaException, IOException{

	    switch(MODE){
		case MENU:
		    updateMenu();
		    break;
		case ABOUT:
		    updateAbout();
		    break;
		case GAME:
		    updateGame();
		    break;
	    }


        }

	public void paint(){

	    switch(MODE){
		case MENU:
		    paintMenu();
		    break;
		case ABOUT:
		    paintAbout();
		    break;
		case GAME:
		    paintGame();
		    break;
	    }

	}
	private void paintAbout() {

	    
	    g.drawImage(aboutBGImg, 0, 0, Graphics.LEFT | Graphics.TOP);

	}

        private void paintMenu(){
            g.setColor(0, 255, 0);
            g.fillRect( 0, 0, getWidth(), getHeight());
            g.drawImage(bgImg, 0, 0, Graphics.LEFT | Graphics.TOP);
            //  title animation for the startscreen
            if (titley >3)
            {
                titley--;
            }
            if (titlex > (horCenter-(titleImg.getWidth()/2)))
            {
                titlex--;
            }
            
            //draw decorations
            titleSp.paint(g);
            g.drawImage(vImg, 0, 0, Graphics.LEFT | Graphics.TOP);
            g.drawImage(rvImg, (horCenter*2) - rvImg.getWidth() , vertCenter+100, Graphics.LEFT | Graphics.TOP);
            //draw buttons

            switch(menuChoice){
                case 0:
                    startSp[1].paint(g);
                    aboutSp[0].paint(g);
                    hiscoreSp[0].paint(g);
                    exitSp[0].paint(g);
                    break;
                case 1:
                    startSp[0].paint(g);
                    aboutSp[1].paint(g);
                    hiscoreSp[0].paint(g);
                    exitSp[0].paint(g);
                    break;
                case 2:
                    startSp[0].paint(g);
                    aboutSp[0].paint(g);
                    hiscoreSp[1].paint(g);
                    exitSp[0].paint(g);
                    break;
                case 3:
                    startSp[0].paint(g);
                    aboutSp[0].paint(g);
                    hiscoreSp[0].paint(g);
                    exitSp[1].paint(g);
                    break;
            }
        }

	private void updateMenu() throws MediaException {
	    if (titleSp!=null && titlex > (horCenter-(titleImg.getWidth()/2)))
                titlex-=2;
            if (titleSp!=null && titley>3 ){
                titleSp.setPosition(titlex, titley-=2);
                titleSp.setTransform(rot);
                // 0 5 3 6  rotation constants for 0,90,180 and 270 degrees
                switch (rot){
                    case 0:
                        rot = 5;
                        break;
                    case 5:
                        rot = 3;
                        break;
                    case 3:
                        rot = 6;
                        break;
                    case 6:
                        rot = 0;
                        break;
                }
            }
            else{
                menuTune.start();
                titleSp.setTransform(Sprite.TRANS_NONE);
            }
	}
	public void run(){

            while(true){
                g = getGraphics();
                try {
                    processKeys();
                    updateMe();
                    paint();
                    Thread.sleep(SLEEPTIME);
		} catch (IOException ioe)
		    {System.out.println("Error: " + ioe.toString());ioe.printStackTrace();}
                  catch (InterruptedException ie)
		    {System.out.println("Error: " + ie.toString());ie.printStackTrace();}
                  catch ( MediaException me) 
		    {System.out.println("Error: " + me.toString());me.printStackTrace();}

                flushGraphics();
		
            }

	}

    private void updateAbout() throws MediaException {
	menuTune.start();
    }

    private void menuKeys() throws MediaException {
	if (keycounter>5){
	    if((this.getKeyStates() & FIRE_PRESSED)!=0){
                    switch(menuChoice){
                        case 0:
                            MODE = GAME;
			    menuTune.stop();
			    keycounter = 0;
			    SLEEPTIME = 30;
			    System.gc();
                            break;
                        case 1:
                            MODE = ABOUT;
			    menuTune.stop();
			    keycounter = 0;
			    System.gc();
                            break;
                        case 2:
                            //hiscores
                            break;
                        case 3:
			    System.gc();
                            try {
				this.parent.destroyApp(true);
			    } catch (MIDletStateChangeException ex) {
				System.out.println("Error: " + ex.toString());
			    }

                            break;
                        default:
                            break;
                    }
            }

	    if ((this.getKeyStates() & UP_PRESSED) !=0 ){
		menuChoice--;
		keycounter = 0;
	    }
            if ((this.getKeyStates() & DOWN_PRESSED )!=0 ){
		menuChoice++;
                keycounter = 0;
	    }

	}
            keycounter++;
            if (menuChoice <0) menuChoice =3;
	    if (menuChoice >3) menuChoice =0;
    }

    private void aboutKeys() {
	if (keycounter>5){
	    if((this.getKeyStates() & FIRE_PRESSED)!=0)
	    {
		MODE = MENU;
		keycounter=0;
	    }
	}
	keycounter++;
    }

    private void gameKeys() throws IOException {

	
	if(GAMESTATE == PLAYING){

	    if((this.getKeyStates() & LEFT_PRESSED) !=0){
		heroSp.move(-3, 0);
	    }
	    if((this.getKeyStates() & RIGHT_PRESSED) !=0){
		heroSp.move(3, 0);
	    }
	    if((this.getKeyStates() & FIRE_PRESSED) !=0){

		
		    if(bulletcounter>MAXHEROBULLETS-1){
			bulletcounter=0;
		    }
		    for(int i=0;i<MAXHEROBULLETS;i++){
			if(!heroFired[i]){
			
			}
		    }
		    hBulletSp[bulletcounter].setPosition(heroSp.getX(), heroSp.getY());
		    heroFired[bulletcounter] = true;
		    bulletcounter++;
		    shotTimer=0;
    //		for(int i=0;i<NUMBADDIES;i++){
    //		    enemy[i].kill();
    //		}
		}
		
	    
	}
    }

    private void paintGame() {
	switch(GAMESTATE){

	    case INTRO:

		g.setColor(0, 0, 0);
		g.fillRect( 0, 0, getWidth(), getHeight());
		gameIntroOne.paint(g);
		
		break;

	    case PLAYING:
		g.setColor(0, 50, 0);
		g.fillRect( 0, 0, getWidth(), getHeight());
		gameBG3Sp.paint(g);
		gameBG2Sp.paint(g);
		gameBG1Sp.paint(g);
		for(int i=0;i<NUMBADDIES;i++){
		    enemy[i].draw(g);
		}
		heroSp.paint(g);
		for (int i = 0; i < MAXHEROBULLETS; i++) {
		    if(heroFired[i]){
			hBulletSp[i].paint(g);
		    }
		}
		break;

	    case OUTRO:
		break;

	}
    }

    private void updateGame() throws MediaException, IOException {
	switch(GAMESTATE){

	    case INTRO:

		gameTune.start();
		if(gameIntroOne.getY() < -640){
		    GAMESTATE = PLAYING;
		    
		}else{
		    if(gameIntroOne.getY() < -320){
			gameIntroOne.move(0, -12);
		    }else{
			gameIntroOne.move(0, -2);
		    }
		}

		break;

	    case PLAYING:
		gameTune.start();
		if(gameBG1Sp.getY() > 0){
		    gameBG1Sp.setPosition(0, -320);
		    gameBG1Sp.move(0, 3);
		}else{
		    gameBG1Sp.move(0, 3);
		}
		if(gameBG2Sp.getY() > 0){
		    gameBG2Sp.setPosition(0, -320);
		    gameBG2Sp.move(0, 2);
		}else{
		    gameBG2Sp.move(0, 2);
		}
		if(gameBG3Sp.getY() > 0){
		    gameBG3Sp.setPosition(0, -320);
		    gameBG3Sp.move(0, 1);
		}else{
		    gameBG3Sp.move(0, 1);
		}
		for(int i=0;i<NUMBADDIES;i++){
		    enemy[i].update();
		}
		for (int i = 0; i < MAXHEROBULLETS; i++) {
		    if(heroFired[i]){
			for(int j =0;j<NUMBADDIES;j++){
			    if(hBulletSp[i].collidesWith(enemy[j].getSprite(), false)){
				enemy[j].kill();
				heroFired[i] = false;
				hBulletSp[i].setPosition(-10, -10);
			    }
			}
			if(hBulletSp[i].getY() < -hBulletSp[i].getHeight()){
			    heroFired[i] = false;
			    hBulletSp[i].setPosition(-10, -10);
			}else{
			    hBulletSp[i].move(0, -10);
			}
		    }
		}
		break;

	    case OUTRO:
		break;

	}
    }






}

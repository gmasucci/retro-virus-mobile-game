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

        private static final int IMAGE = 0;
        private static final int BUTTON = 1;
        private static final int BACKGROUND = 2;
        private int horCenter = getWidth()/2;
        private int vertCenter = getHeight()/2;

	//  All sprites
        //  button sprites
        private Sprite[]        aboutSp;
        private Sprite[]        exitSp;
        private Sprite[]        hiscoreSp;
        private Sprite[]        startSp;
        //  whole-screen sprites
        private Sprite          titleSp;
	private Sprite		gameIntroOne;
        private Background      background;
        //  and handy ways to access them
        //  remeber to set these up right
        //  or the background parallax will be off
        private Sprite[]        bkground;

        //  enemies
	private Sprite[]	enemy;
	private Virus		virusSprites;
        //  end of all sprites

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
        private static int keycounter = 0;

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
	//sound
        Player menuTune;
        Player menuEffect;
	Player gameTune;
        private Class storedClass;

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
            loadImages();
            
            //  temporary sprite for the title
            titleSp = new Sprite(titleImg);
            titleSp.defineReferencePixel(titleSp.getWidth()/2, titleSp.getHeight()/2);

            //  load and set the menu buttons
            initButtons();

            // prepare the sounds for later
            prepSounds();
            //  init the background array
            //  and background sprites
            initBackgrounds();

            // set sprite positions
	    gameIntroOne = new Sprite(Utils.loadImage("WARNING1", IMAGE));
	    gameIntroOne.setPosition(0, getHeight());
	    virusSprites = new Virus();
	    enemy = new Sprite[10];
	    for(int i=0;i<10;i++){
		int k;
		Random j = new Random();
		j.setSeed(System.currentTimeMillis());
		k = j.nextInt(3);
		switch(k){
		    case 0:
			enemy[i] = virusSprites.getVirusSp();
			break;
		    case 1:
			enemy[i] = virusSprites.getTrojanSp();
			break;
		    case 2:
			enemy[i] = virusSprites.getWormSp();
			break;
		}
	    }
        }

    //  Start and Run Methods
        public void startMe(){
            Thread th = new Thread(this);
            th.start();
            try {
                menuEffect.start();
            } catch (MediaException ex) {
                System.out.println("Error: " + ex.toString());
            }
        }
        public void run(){
            while(true){
                g = getGraphics();
                try {
                    processKeys();
                    updateMe();
                    paint();
                    Thread.sleep(40);
                } catch (InterruptedException e)
		    {System.out.println("Error: " + e.toString());}
                  catch ( MediaException me) 
		    {System.out.println("Error: " + me.toString());}

                flushGraphics();
            }
	}

    //  Key Methods
        private void processKeys(){
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
        private void menuKeys() {
	if (keycounter>5){
	    if((this.getKeyStates() & FIRE_PRESSED)!=0){
                switch(menuChoice){
                    case 0:
                        MODE = GAME;
                try {
                    menuTune.stop();
                } catch (MediaException ex) {
                    ex.printStackTrace();
                }
                        keycounter = 0;
                        break;
                    case 1:
                        MODE = ABOUT;
                try {
                    menuTune.stop();
                } catch (MediaException ex) {
                    ex.printStackTrace();
                }
                        keycounter = 0;
                        break;
                    case 2:
                        //hiscores
                        break;
                    case 3:
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
            menuChoice=cycleRound(menuChoice, 3);

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
        private void gameKeys() {

	if(GAMESTATE == PLAYING){

	    if((this.getKeyStates() & LEFT_PRESSED) !=0){
		//gameAction move left;
	    }
	    if((this.getKeyStates() & RIGHT_PRESSED) !=0){
		//gameAction move right;
	    }
	    if((this.getKeyStates() & FIRE_PRESSED) !=0){
		//gameAction fire bullet;
	    }
	}
        else if(GAMESTATE == INTRO){
            if ((this.getKeyStates()& FIRE_PRESSED) !=0)
                if (gameIntroOne.isVisible())
                {
                    gameIntroOne.setVisible(false);
                    GAMESTATE = PLAYING;
                }
        }
    }

    //  Paint Methods
        public void paint() {
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
        private void paintGame() {
	switch(GAMESTATE){

	    case INTRO:

		g.setColor(0, 0, 0);
		g.fillRect( 0, 0, getWidth(), getHeight());
		gameIntroOne.paint(g);

		break;

	    case PLAYING:
		g.setColor(0, 0, 0);
		g.fillRect( 0, 0, getWidth(), getHeight());
//		if(gameIntroOne.getY() < -640){
//		    gameIntroOne.paint(g);
//		}
                //  do backgrounds before all else,
                //  in order of bottom, middle, top
//                bkground[0].paint(g);

//		for (int i=0;i<10; i++){
//		    enemy[i].paint(g);
//                    enemy[i].nextFrame();
//		}

//		enemy[0].paint(g);
//		enemy[1].paint(g);
//		enemy[2].paint(g);
//		enemy[3].paint(g);



		break;

	    case OUTRO:
		break;

	}
    }
        private void paintMenu(){
            g.setColor(0, 255, 0);
            g.fillRect( 0, 0, getWidth(), getHeight());
            g.drawImage(bgImg, 0, 0, Graphics.LEFT | Graphics.TOP);
            //  title animation for the startscreen
            if (titley >3)  titley--;
            if (titlex > (horCenter-(titleImg.getWidth()/2)))   titlex--;

            //draw decorations
            titleSp.paint(g);
            g.drawImage(vImg, 0, 0, Graphics.LEFT | Graphics.TOP);
            g.drawImage(rvImg, (horCenter*2) - rvImg.getWidth() , vertCenter+100, Graphics.LEFT | Graphics.TOP);
            //  draw our menu buttons
            drawMenuButtons(/*menuChoice, g*/);
        }
        private void paintAbout() {
	    g.setColor(0, 255, 0);
            g.fillRect( 0, 0, getWidth(), getHeight());
	    g.drawImage(aboutBGImg, 0, 0, Graphics.LEFT | Graphics.TOP);
	}

    //  Update Methods
        public  void updateMe(){
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
        private void updateMenu(){
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
            try {
                menuTune.start();
            } catch (MediaException ex) {
                ex.printStackTrace();
            }
                titleSp.setTransform(Sprite.TRANS_NONE);
            }
	}
        private void updateAbout() {
	// do nothing, this is not the update you are looking for
    }
        private void updateGame(){
	switch(GAMESTATE){

	    case INTRO:
        try {
            gameTune.start();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
		if(gameIntroOne.getY() < -640){
		    GAMESTATE = PLAYING;

		    //generate starting enemy positions

		    for(int i=0;i<10;i++){
			Random j = new Random();
			j.setSeed(System.currentTimeMillis());
			int k = j.nextInt(getWidth()-enemy[i].getWidth());
			enemy[i].setPosition(i,i);
		    }

		}else{
		    if(gameIntroOne.getY() < -320){
			gameIntroOne.move(0, -12);
		    }else{
			gameIntroOne.move(0, -4);
		    }
		}
		break;

	    case PLAYING:
                gameIntroOne.setVisible(false);
//		if(gameIntroOne.getY() < -640){
//		    gameIntroOne.move(0, -3);
//		}
//                while(bkground[0].getY() < -640)
//                    bkground[0].move(0,1);
        try {
            gameTune.start();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }

		for(int i=0;i<10;i++){
		    enemy[i].move(0, 1);
		}
		break;

	    case OUTRO:
		break;

	}
    }

    //  Startup Methods
    //  these collate some startup routines to keep them manageable and tidy

    private void initButtons(){
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

        //  set the button positions
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
    }
    private void loadImages(){
        bgImg   =   Utils.loadImage("BG", BACKGROUND);
        aboutBGImg = Utils.loadImage("aboutBG", BACKGROUND);
        rvImg   =   Utils.loadImage("retroHD", IMAGE);
        vImg    =   Utils.loadImage("virusHD", IMAGE);
        titleImg =  Utils.loadImage("title", IMAGE);
    }
    private void prepSounds(){
        menuTune = Utils.playWav("tune.wav", storedClass);
        menuEffect = Utils.playWav("starteffect.wav", storedClass);
        gameTune = Utils.playWav("gameTune.wav", storedClass);
    }
    private void drawMenuButtons(/*int menuChoice, Graphics g*/){
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
    private void initBackgrounds(){
        bkground = new Sprite[3];
//        try {
//            //  set up backgrounds
//
//            bkground[0] = background.getBkgBottom();
//            bkground[1] = background.getBkgMiddle();
//            bkground[2] = background.getBkgTop();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

    }

    //  function for looping round a set of ints,
    //  handy for say menus, arrays etc:)
    private int cycleRound(int intName, int maxExtent)
    {
        if (intName <0) intName =maxExtent;
	if (intName >maxExtent) intName =0;
        return intName;
    }

}

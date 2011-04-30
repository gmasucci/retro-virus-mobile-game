//All Images & sound by Jonathan Grant/Neil Finlay/Giovanni Masucci
//All sound created from samples from "http://www.freesound.org"
//Main game tune made with SunVox "http://warmplace.ru/soft/sunvox/index.php"

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

    //  SPRITES
    //  Button sprite arrays
    private Sprite[]        aboutSp;
    private Sprite[]        exitSp;
    private Sprite[]        hiscoreSp;
    private Sprite[]        startSp;
    private Sprite          titleSp;
    //  background sprites
    private Sprite		gameIntroOne;
    private Sprite		gameBG1Sp;
    private Sprite		gameBG2Sp;
    private Sprite		gameBG3Sp;
    //  hero sprites
    private HeroSprite          hs;
    private Sprite		heroSp;
    private Sprite		hBulletSp[];
    private Sprite              hBullet2Sp[];
    //  generic sprites
    private Sprite		vBulletSp;
    private ExplodeSprite	exp;
    private Sprite		explodeSp;

    //static images,
    private Image           titleImg;
    private Image           rvImg;
    private Image           vImg;
    private Image           bgImg;
    private Image           aboutBGImg;
    private Graphics        g;

    //  upgrade system
    public Upgrade             upgrade;


    // title positions
    private int titlex = 200;
    private int titley = 100;

    static private int rot =0;
    private int menuChoice = 0;
    private int keyRepeatCounter = 0;

    // STATES
    // Application state
    private int MODE;
    private static final int MENU = 0;
    private static final int ABOUT = 1;
    private static final int GAME = 2;
    private static final int HIGHSCORES = 3;
    // "GAME" application state sub states;
    private int GAMESTATE;
    private static final int INTRO = 0;
    private static final int PLAYING = 1;
    private static final int OUTRO = 2;
    //  hero states
    private int HEROSTATE;
    private static final int NOPOWER = 0;
    private static final int POWERONE = 1;
    private static final int POWERTWO = 2;
    private static final int DEAD = 3;

    //  GAME CONTROL
    //  game control variables
    private static  int NUMBADDIES = 12;
    private static final int MAXBADDIES = 30;
    private static final int MAXHEROBULLETS = 10;
    //  background scroll speeds
    private int topBGspeed = 3;
    private int mdlBGspeed = 2;
    private int btmBGspeed = 1;
    //  game control arrays
    private Enemy enemy[];
    private boolean heroFired[];
    private boolean heroFired2[];

    //  main game reporting stuff
    private int score;
    private int lives;
    private int kills=0;

    //  sounds
    Player menuTune;
    Player menuEffect;
    Player gameTune;
    Player shot;

    //hiscore stuff
    private HighScoreSystem hiscore;
    String httpmessage;

    //  general game variables
    private Class storedClass;
    Thread th;
    private int bulletcounter;
    private static int shotTimer;
    private boolean WIN;


        public MainCanvas(final MainMidlet m) throws IOException{

	    super( true );
	    storedClass = getClass();
            g = getGraphics();
	    this.setFullScreenMode(true);
	    
	    MODE = MENU;
	    GAMESTATE = INTRO;
	    HEROSTATE = POWERONE;
	    //assign our MainMidlet pointer
	    parent = m;
	    hiscore = new HighScoreSystem();
	    if(!hiscore.init()){
		try {
		    parent.destroyApp(true);
		} catch (MIDletStateChangeException ex) {
		    ex.printStackTrace();
		}
	    }
	    //  set the horizontal and vertical centers
            horCenter = getWidth()/2;
            vertCenter = getHeight()/2;
            //  set an upgrade system
            upgrade = new Upgrade();

            //some neatifying jiggery pokery here
            //  load some images
            loadImages();
            //  temporary sprite for the title
            initTitleSprite();
            //  load and init the buttons for the menu
            loadButtonSprites();
            setButtonSpritePositions();
            //  sort the backgrounds out
            loadAndInitBackgrounds();
	    //  may just need a hero, lets set his stuff now
            initHero();
            //  sound
            loadSounds();
            //  set some enemies up
            createEnemies();
        }


        public void     startMe(){
            th = new Thread(this);
            th.start();
            try {
                menuEffect.start();
		SLEEPTIME = 40;
            } catch (MediaException ex) {
                System.out.println("Error: " + ex.toString());
            }
        }
        public void     run(){

            while(true){
                g = getGraphics();
                try {
                    processKeys();
                    updateMe();
                    paint();
                    Thread.sleep(SLEEPTIME);
		} catch (IOException ioe)
			//TODO: Remove Stacktraces
		    {System.out.println("Error: " + ioe.toString());ioe.printStackTrace();}
                  catch (InterruptedException ie)
		    {System.out.println("Error: " + ie.toString());ie.printStackTrace();}
                  catch ( MediaException me)
		    {System.out.println("Error: " + me.toString());me.printStackTrace();}

                flushGraphics();

            }

	}
        //  updating Methods
        public void     updateMe() throws MediaException, IOException{

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
        private void    updateAbout() throws MediaException {
	menuTune.start();
    }
        private void    updateMenu() throws MediaException {
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
        private void    updateGame() throws MediaException, IOException {
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
                if (lives < 1)
                {
                    HEROSTATE=DEAD;
		    GAMESTATE = OUTRO;
                }

		if(HEROSTATE != DEAD){
		    gameTune.start();
                    upgrade.update();
                    spdUpdates();
                    updateForceField();
                    moveBackgrounds();
		    for(int i=0;i<NUMBADDIES;i++){
			enemy[i].update();
		    }
		    for (int i = 0; i < MAXHEROBULLETS; i++) {
			if(heroFired[i]){
			    for(int j=0;j<NUMBADDIES;j++){
				if(hBulletSp[i].collidesWith(enemy[j].getSprite(), false) && (enemy[j].getState() == 0))
                                {
                                    if(enemy[j].kill()) alterScore(j);
                                        heroFired[i] = false;
                                        hBulletSp[i].setPosition(-10, -10);
				}
                                if(hBullet2Sp[i].collidesWith(enemy[j].getSprite(), false) && (enemy[j].getState() == 0))
                                {
                                    if(enemy[j].kill()) alterScore(j);
                                        heroFired2[i] = false;
                                        hBullet2Sp[i].setPosition(-10, -10);
				}
			    }
			    validateShot(i);
                            moveHeroBullets(i);	    
			}
                    }
		  
		    for(int i=0;i<NUMBADDIES;i++){
			if(heroSp.collidesWith(enemy[i].getSprite(), false) && enemy[i].getState() == 0){
			    if(upgrade.currentLevel == Upgrade.Stage1){
				HEROSTATE = DEAD;
                                Upgrade.killCount=0;
				lives -=1;
			    }else if(upgrade.currentLevel == Upgrade.Stage2){
                                upgrade.downgrade();
                                HEROSTATE = NOPOWER;
			    }else if(upgrade.currentLevel == Upgrade.Stage3){
                                upgrade.downgrade();
                                HEROSTATE = POWERONE;
			    }
			}
		    }
		}
                else
                {
		    explodeSp.setPosition(heroSp.getX(), heroSp.getY());
		    explodeSp.nextFrame();
		    if(explodeSp.getFrame() == 7)
                    {
			HEROSTATE = NOPOWER;
			for(int i=0;i< NUMBADDIES;i++)
                        {
			    enemy[i].kill();
			    enemy[i].kill();
			}
		    }
		}
		break;

	    case OUTRO:
		//display score
		if(WIN){
		    //winner sprite.show
		    //highscore
		    score = (int) (score * 1.5);
		}
		if(!WIN){
		}
		httpmessage = hiscore.upload(score);
		break;

	}
    }
        //  Painting methods
	public void     paint(){

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
	private void    paintAbout() {


	    g.drawImage(aboutBGImg, 0, 0, Graphics.LEFT | Graphics.TOP);

	}
        private void    paintMenu(){
            g.setColor(0, 255, 0);
            g.fillRect( 0, 0, getWidth(), getHeight());
            g.drawImage(bgImg, 0, 0, Graphics.LEFT | Graphics.TOP);
            //  title animation for the startscreen
            if (titley >3)  {   titley--;   }
            if (titlex > (horCenter-(titleImg.getWidth()/2)))   { titlex--;     }
            //draw decorations
            drawDecorations();
            //draw buttons
            drawButtons();
        }
        private void    paintGame() {
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
		if (HEROSTATE != DEAD){
		    for(int i=0;i<NUMBADDIES;i++)   {   enemy[i].draw(g);   }
                    heroSp.setFrame(upgrade.currentLevel);
		    heroSp.paint(g);
                    if (upgrade.currentLevel==Upgrade.Stage3)   upgrade.shredder.paint(g);

		    for (int i = 0; i < MAXHEROBULLETS; i++) {
			if(heroFired[i]){
                            switch (upgrade.currentLevel){
                                case Upgrade.Stage1:
                                    hBulletSp[i].paint(g);
                                    break;
                                case Upgrade.Stage2:
                                    hBulletSp[i].paint(g);
                                    hBullet2Sp[i].paint(g);
                                    break;
                                case Upgrade.Stage3:

                                    hBulletSp[i].paint(g);
                                    hBullet2Sp[i].paint(g);
                                    break;
                            }
                        }
                    }
		    g.fillRect(0, -10, getWidth(), 30);
		    g.setColor(32, 255, 32);
		    g.drawString("Score: " + Integer.toString(score), 0, 0, 0);
		    g.drawString("Lives: " + Integer.toString(lives), horCenter, 0, 0);
		}
                else
                {
		    explodeSp.paint(g);
		}
		break;

	    case OUTRO:

		g.setColor(0, 50, 0);
		g.fillRect( 0, 0, getWidth(), getHeight());
		g.setColor(255, 255, 255);
		if(httpmessage!=null)
		    g.drawString(httpmessage, 0, 0, 0);

		break;

	}
    }

    //  key-input methods
    public void     processKeys() throws MediaException, IOException{

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
    private void    aboutKeys() {
    if (keyRepeatCounter>5){
        if((this.getKeyStates() & FIRE_PRESSED)!=0)
        {
            MODE = MENU;
            keyRepeatCounter=0;
        }
    }
    keyRepeatCounter++;
}
    private void    menuKeys() throws MediaException {
        if (keyRepeatCounter>5){
            if((this.getKeyStates() & FIRE_PRESSED)!=0){
                    switch(menuChoice){
                        case 0:
                            MODE = GAME;
                            menuTune.stop();
                            menuEffect.stop();
                            keyRepeatCounter = 0;
                            SLEEPTIME = 20;
                            System.gc();
                            break;
                        case 1:
                            MODE = ABOUT;
                            //menuTune.stop();
                            keyRepeatCounter = 0;
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
                keyRepeatCounter = 0;
            }
            if ((this.getKeyStates() & DOWN_PRESSED )!=0 ){
                menuChoice++;
                keyRepeatCounter = 0;
            }

        }
            keyRepeatCounter++;
            if (menuChoice <0) menuChoice =3;
            if (menuChoice >3) menuChoice =0;
    }
    private void    gameKeys() throws IOException, MediaException {

    if(GAMESTATE == INTRO){
        if((this.getKeyStates() & FIRE_PRESSED) !=0)    {   gameIntroOne.move(0, -10);  }
    }

    if(GAMESTATE == PLAYING && HEROSTATE != DEAD){

    if((this.getKeyStates() & LEFT_PRESSED) !=0){   heroSp.move(-3, 0); }
    if((this.getKeyStates() & RIGHT_PRESSED) !=0){  heroSp.move(3, 0);  }
    if (upgrade.currentLevel==Upgrade.Stage3)
        {
            if((this.getKeyStates() & UP_PRESSED) !=0){ heroSp.move(0,-2);  }
            if((this.getKeyStates() & DOWN_PRESSED) !=0){heroSp.move(0, 4); }
        }

        if((this.getKeyStates() & FIRE_PRESSED) !=0){

            if(bulletcounter==MAXHEROBULLETS)   {   bulletcounter=0;    }
            if (keyRepeatCounter>shotTimer){
                if(heroFired[bulletcounter]){   bulletcounter++;        }
                else
                {
                    switch(upgrade.currentLevel){
                        case Upgrade.Stage1:
                            hBulletSp[bulletcounter].setPosition(heroSp.getX(), heroSp.getY());
                            heroFired[bulletcounter] = true;
                            break;
                        case Upgrade.Stage2:
                            hBulletSp[bulletcounter].setPosition(heroSp.getX()+16, heroSp.getY());
                            hBullet2Sp[bulletcounter].setPosition(heroSp.getX(), heroSp.getY());
                            heroFired[bulletcounter] = true;
                            heroFired2[bulletcounter]= true;
                            break;
                        case Upgrade.Stage3:
                            hBulletSp[bulletcounter].setPosition(heroSp.getX()+16, heroSp.getY());
                            hBullet2Sp[bulletcounter].setPosition(heroSp.getX(), heroSp.getY());
                            heroFired[bulletcounter] = true;
                            heroFired2[bulletcounter]= true;
                            break;
                    }
                    if(shot.getState() == Player.STARTED){
                        shot.stop();
                    }
                    shot.start();
                    keyRepeatCounter=0;
                }
            }
        }
        keyRepeatCounter++;
    }
}

    //  static accessors, for use in other classes
    public static void  decreaseShotTimer(int valueToDecreaseBy)     {   shotTimer -= valueToDecreaseBy;  }
    public static void  increaseShotTimer(int valueToIncreaseBy)     {   shotTimer += valueToIncreaseBy;  }
    public static void  decreaseMaxEnemies(int valueToDecreaseBy)    {   NUMBADDIES -=  valueToDecreaseBy;}
    public static void  increaseMaxEnemies(int valueToIncreaseBy)    {   NUMBADDIES +=  valueToIncreaseBy;}
    public static int   getMaxEnemies()                               {   return NUMBADDIES;  }
    public static void  setMaxEnemies(int newMax)                    {   NUMBADDIES = newMax;    }

    //  loading and segregation methods
    private void loadImages(){
        //  load some images
        bgImg   =   Utils.loadImage("BG", BACKGROUND);
        aboutBGImg = Utils.loadImage("aboutBG", BACKGROUND);
        rvImg   =   Utils.loadImage("retroHD", IMAGE);
        vImg    =   Utils.loadImage("virusHD", IMAGE);
        titleImg =  Utils.loadImage("title", IMAGE);
    }
    private void loadSounds(){
        menuTune = Utils.loadWav("tune.wav", storedClass);
        menuEffect = Utils.loadWav("starteffect.wav", storedClass);
        gameTune = Utils.loadWav("gameTune.wav", storedClass);
        shot = Utils.loadWav("shot.wav", storedClass);
    }
    private void loadButtonSprites(){
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
        menuTune = Utils.loadWav("tune.wav", storedClass);
        menuEffect = Utils.loadWav("starteffect.wav", storedClass);
        gameTune = Utils.loadWav("gameTune.wav", storedClass);
        shot = Utils.loadWav("shot.wav", storedClass);
    }
    private void setButtonSpritePositions(){
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
    }
    private void loadAndInitBackgrounds(){
        gameIntroOne = new Sprite(Utils.loadImage("WARNING1", IMAGE));
        gameIntroOne.setPosition(0, getHeight());

        gameBG1Sp = new Sprite(Utils.loadImage("gameBG1", BACKGROUND));
        gameBG2Sp = new Sprite(Utils.loadImage("gameBG2", BACKGROUND));
        gameBG3Sp = new Sprite(Utils.loadImage("gameBG3", BACKGROUND));
    }
    private void initHero(){
        //hero stuff - for retro
        lives = 3;
        kills = 0;
        //  array setups
        hBulletSp = new Sprite[MAXHEROBULLETS];
        hBullet2Sp = new Sprite[MAXHEROBULLETS];
        heroFired = new boolean[MAXHEROBULLETS];
        heroFired2 = new boolean[MAXHEROBULLETS];
        shotTimer = 10; // 1 shot every 10 frames initially
                        //next level is 4;
        //  setup the heros sprite
        hs = new HeroSprite();
        try {
            heroSp = hs.getHeroSp();
        } catch (IOException ex) {        }
        heroSp.setFrame(upgrade.currentLevel);
        heroSp.setPosition(horCenter, getHeight()- heroSp.getHeight());
        heroSp.defineReferencePixel(12,12);
        //  setup bullets for our hero
        for (int i = 0; i < MAXHEROBULLETS; i++) {
            hBulletSp[i] = new Sprite(Utils.loadImage("heroBullet", IMAGE));
            hBullet2Sp[i] = new Sprite(Utils.loadImage("heroBullet", IMAGE));
            hBulletSp[i].setPosition(-10, -10);
            hBullet2Sp[i].setPosition(-10, -10);
        }
        //  setup enemies and enemy bullets
        try {   createEnemies();    }   catch   (IOException ex)    {        }

        vBulletSp = new Sprite(Utils.loadImage("enemyBullet", IMAGE));
        vBulletSp.setPosition(-10, -10);
        exp = new ExplodeSprite();
        try {   explodeSp = exp.getExplodeSp();  } catch (IOException ex) {}

    }
    private void initTitleSprite(){
        titleSp = new Sprite(titleImg);
        titleSp.defineReferencePixel(titleSp.getWidth()/2, titleSp.getHeight()/2);
    }
    private void createEnemies() throws IOException{
	    enemy = new Enemy[MAXBADDIES];
	    int k =0;
	    Random j = new Random();
	    for(int i=0;i<MAXBADDIES;i++){

		j.setSeed(k);
		k = j.nextInt(216);
		enemy[i] = new Enemy(k, storedClass);
	    }
	}

    //  update-type methods
    private void spdUpdates(){
    Enemy.setSpeedVar(upgrade.currentLevel);
    topBGspeed = upgrade.currentLevel + 3;
    mdlBGspeed = upgrade.currentLevel + 2;
    btmBGspeed = upgrade.currentLevel + 1;
}
    private void validateShot(int i){
    if(hBulletSp[i].getY() < -hBulletSp[i].getHeight()){
        heroFired[i] = false;
        hBulletSp[i].setPosition(-10, -10);
    }
    if (hBullet2Sp[i].getY() < -hBullet2Sp[i].getHeight()){
        heroFired2[i] = false;
        hBullet2Sp[i].setPosition(-10,-10);
    }
}
    private void moveHeroBullets(int i){
        switch (upgrade.currentLevel){
            case Upgrade.Stage1:
                if (heroFired[i])   hBulletSp[i].move(0, -10);
                break;
            case Upgrade.Stage2:
                if (heroFired[i])   hBulletSp[i].move(0, -10);
                if (heroFired2[i])  hBullet2Sp[i].move(0, -10);
                break;
            case Upgrade.Stage3:
                if (heroFired[i])   hBulletSp[i].move(0, -10);
                if (heroFired2[i])  hBullet2Sp[i].move(0, -10);
                break;
        }
    }
    private void moveBackgrounds(){
        if(gameBG1Sp.getY() > 0){    gameBG1Sp.setPosition(0, -320);    }
            gameBG1Sp.move(0, topBGspeed);
        if(gameBG2Sp.getY() > 0){    gameBG2Sp.setPosition(0, -320);    }
            gameBG2Sp.move(0, mdlBGspeed);
        if(gameBG3Sp.getY() > 0){   gameBG3Sp.setPosition(0, -320);     }
            gameBG3Sp.move(0, btmBGspeed);
    }
    private void updateForceField(){
        upgrade.shredder.setPosition(
                (heroSp.getX()-upgrade.shredder.getWidth()/2)+heroSp.getWidth()/2,
                (heroSp.getY()-upgrade.shredder.getHeight()/2)-4+(heroSp.getHeight()/2) );
        if (upgrade.currentLevel==Upgrade.Stage3)
        {
            for (int i=0;i<NUMBADDIES;i++)
            {
                if(upgrade.shredder.collidesWith(enemy[i].getSprite(), false) && (enemy[i].getState() == Enemy.ALIVE))
                    try {
                    enemy[i].kill();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (MediaException ex) {
                    ex.printStackTrace();
                }

            }
        }
    }
    private void alterScore(int j){
            switch(enemy[j].getType()){
                case 0:
                    score+=10;
                    break;
                case 1:
                    score+=20;
                    break;
                case 2:
                    score+=30;
                    break;
                default:
                    break;
            }
    }

    //  draw-type methods
    private void drawButtons(){
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
    private void drawDecorations(){
        titleSp.paint(g);
        g.drawImage(vImg, 0, 0, Graphics.LEFT | Graphics.TOP);
        g.drawImage(rvImg, (horCenter*2) - rvImg.getWidth() , vertCenter+100, Graphics.LEFT | Graphics.TOP);
    }




}
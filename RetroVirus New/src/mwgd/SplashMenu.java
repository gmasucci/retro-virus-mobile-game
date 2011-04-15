/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mwgd;

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.media.*;

/**
 *
 * @author Neil
 */
public class SplashMenu
        extends GameCanvas
        implements Runnable{

        private static final int IMAGE = 0;
        private static final int BUTTON = 1;
        private static final int BACKGROUND = 2;
        private int horCenter = getWidth()/2;
        private int vertCenter = getHeight()/2;

        private Sprite[]        aboutSp;
        private Sprite[]        exitSp;
        private Sprite[]        hiscoreSp;
        private Sprite[]        startSp;
        private Sprite          titleSp;
        private Image           titleImg;
        private Image           rvImg;
        private Image           vImg;
        private Image           bgImg;
	private Graphics        g;
        //  handy title positioner
        private int titlex = 200;
        private int titley = 100;

        // rotator static member
        static private int rot =0;
        private int keyStates = 0;
        private int menuChoice = 0;
        private boolean upPressed = false;
        private boolean downPressed = false;
        private int keycounter = 0;
        //sound
        Player tune;
        Player effect;
        private Class storedClass;

        public SplashMenu() throws IOException{
            super( true );
            storedClass = getClass();
            g = getGraphics();
            //  set the horizontal and vertical centers
            horCenter = getWidth()/2;
            vertCenter = getHeight()/2;

            //  load some images
            bgImg   =   Utils.loadImage("BG", BACKGROUND);
            rvImg   =   Utils.loadImage("retroHD", IMAGE);
            vImg    =   Utils.loadImage("virusHD", IMAGE);
            titleImg =  Utils.loadImage("title", IMAGE);
            //  temporary sprite for the title
            titleSp = new Sprite(titleImg);
            titleSp.defineReferencePixel(titleSp.getWidth()/2, titleSp.getHeight()/2);
            

            //  About button stuff
            aboutSp = new Sprite[2];
            aboutSp[0] = new Sprite(Utils.loadImage("aboutoff",BUTTON));
            aboutSp[1] = new Sprite(Utils.loadImage("abouton",BUTTON));
            
            //  Exit button stuff
            exitSp  = new Sprite[2];
            exitSp[0] = new Sprite(Utils.loadImage("exitoff",BUTTON));
            exitSp[1] = new Sprite(Utils.loadImage("exiton",BUTTON));
            
            //  start button stuff
            startSp = new Sprite[2];
            startSp[0] = new Sprite(Utils.loadImage("startoff",BUTTON));
            startSp[1] = new Sprite(Utils.loadImage("starton",BUTTON));
            
            //  hiscore button stuff
            hiscoreSp = new Sprite[2];
            hiscoreSp[0] = new Sprite(Utils.loadImage("hisoff",BUTTON));
            hiscoreSp[1] = new Sprite(Utils.loadImage("hison",BUTTON));

            //  sound

            tune = Utils.playWav("tune.wav", storedClass);
            effect = Utils.playWav("starteffect.wav", storedClass);
        try {
            effect.start();
            // set sprite positions
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
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


        public void startMe(){
            Thread th = new Thread(this);
            th.start();
        }

        private void processKeys(){

             
                if (keycounter>10){
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

        public void updateMe() throws MediaException{

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
                tune.start();
                titleSp.setTransform(Sprite.TRANS_NONE);
            }


        }

        public void paint(){
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
            g.drawImage(rvImg, (horCenter*2) - rvImg.getWidth() , vertCenter+80, Graphics.LEFT | Graphics.TOP);
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

	public void run(){

            while(true){
                g = getGraphics();
                try {
                    processKeys();
                    updateMe();
                    paint();
                    Thread.sleep(40);
                } catch (InterruptedException e) {}
                  catch ( MediaException me) {}

                flushGraphics();
            }

	}


}

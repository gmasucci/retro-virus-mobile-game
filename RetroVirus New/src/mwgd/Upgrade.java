/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mwgd;

import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author DarkArts
 */
public class Upgrade {
    public static final int Stage1 = 0;
    public static final int Stage2 = 1;
    public static final int Stage3 = 2;
    public int currentLevel = Stage1;
    public static int killCount = 0;
    public static final int upgradeReq1 = 20;
    public static final int upgradeReq2 = 60;
    public static final int upgradeReq3 = 100;
    public Sprite shredder;
    public int seq[] = {0,1};
    public int seqDelay = 500;
    private long lastTime=0;
    private long thisTime=0;
    private long shieldTime=10000;
    private long shieldStart=0;

    private static int MAXLEVEL = 3;
    private static int MINLEVEL = 0;

    public Upgrade(){
        lastTime=System.currentTimeMillis();
       // shredder = new Sprite(Utils.loadImage("fileshredder3", 0),34 ,34);
        //shredder.setFrameSequence(seq);
        shredder = new Sprite(Utils.loadImage("another",0));
        shredder.defineReferencePixel(21, 21);
        //shredder.defineReferencePixel(17,17);
    }
    public void upgrade(){
        switch(currentLevel){
            case Stage1:
                MainCanvas.increaseMaxEnemies(8);
                currentLevel = Stage2;
                break;
            case Stage2:
                MainCanvas.decreaseShotTimer(6);
                MainCanvas.increaseMaxEnemies(10);
                currentLevel = Stage3;
                break;
            default:
                if (MainCanvas.getMaxEnemies()>20)
                    MainCanvas.setMaxEnemies(20);
                // maxed out do nothing;
                break;
        }
    }
    public void downgrade(){
        switch(currentLevel){
            case Stage3:
                MainCanvas.setMaxEnemies(20);
                currentLevel = Stage2;
                break;
            case Stage2:
                MainCanvas.increaseShotTimer(6);
                MainCanvas.setMaxEnemies(12);
                currentLevel = Stage1;
                break;
            default:
                // bottomed out do nothing;
                break;
        }
    }
    public void update(){
        if (currentLevel==Stage3)
        {
           thisTime=System.currentTimeMillis();
           if (thisTime-lastTime >= seqDelay){
               shredder.nextFrame();
               lastTime=thisTime;
           }
           if (thisTime-shieldStart >=shieldTime) {
               downgrade();
           }

        }

        switch(killCount){
            case upgradeReq1:
                //killCount=0;
                killCount++;
                upgrade();
                break;
            case upgradeReq2:
                //killCount=0;
                killCount++;
                upgrade();
                break;
            case upgradeReq3:
                shieldStart = System.currentTimeMillis();
                killCount=0;
                upgrade();
                break;
        }
    }
    public static void incrementKills(){    killCount++;    }

    
}

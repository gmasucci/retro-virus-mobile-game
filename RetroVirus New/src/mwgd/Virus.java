/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mwgd;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.io.IOException;

/**
 * @author Lenovo
 */
public class Virus {

    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Image enemyVirus;
    private Sprite virusSp;
    public int virusSpseq001Delay = 40;
    public int[] virusSpseq001 = {0, 0, 1, 1, 2, 2, 3, 3};
    private Image enemyTrojan;
    private Sprite trojanSp;
    public int trojanSpseq001Delay = 40;
    public int[] trojanSpseq001 = {0, 0, 1, 1, 2, 2, 3, 3};
    private Image enemyWorm;
    private Sprite wormSp;
    public int wormSpseq001Delay = 40;
    public int[] wormSpseq001 = {0, 0, 1, 1, 2, 2, 3, 3};
    private Image BG;
    private Image explode;
    private Sprite explodeSp;
    public int explodeSpseq001Delay = 110;
    public int[] explodeSpseq001 = {0, 1, 2, 3};
    //</editor-fold>//GEN-END:|fields|0|

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|

    public Image getEnemyVirus() throws java.io.IOException {//GEN-BEGIN:|1-getter|0|1-preInit
	if (enemyVirus == null) {//GEN-END:|1-getter|0|1-preInit
	    // write pre-init user code here
	    enemyVirus = Image.createImage("/images/enemyVirus.png");//GEN-BEGIN:|1-getter|1|1-postInit
	}//GEN-END:|1-getter|1|1-postInit
	// write post-init user code here
	return this.enemyVirus;//GEN-BEGIN:|1-getter|2|
    }
//GEN-END:|1-getter|2|

    public Sprite getVirusSp() throws java.io.IOException {//GEN-BEGIN:|2-getter|0|2-preInit
	if (virusSp == null) {//GEN-END:|2-getter|0|2-preInit
	    // write pre-init user code here
	    virusSp = new Sprite(getEnemyVirus(), 24, 24);//GEN-BEGIN:|2-getter|1|2-postInit
	    virusSp.setFrameSequence(virusSpseq001);//GEN-END:|2-getter|1|2-postInit
	    // write post-init user code here
	}//GEN-BEGIN:|2-getter|2|
	return virusSp;
    }
//GEN-END:|2-getter|2|

    public Image getEnemyTrojan() throws java.io.IOException {//GEN-BEGIN:|4-getter|0|4-preInit
	if (enemyTrojan == null) {//GEN-END:|4-getter|0|4-preInit
	    // write pre-init user code here
	    enemyTrojan = Image.createImage("/images/enemyTrojan.png");//GEN-BEGIN:|4-getter|1|4-postInit
	}//GEN-END:|4-getter|1|4-postInit
	// write post-init user code here
	return this.enemyTrojan;//GEN-BEGIN:|4-getter|2|
    }
//GEN-END:|4-getter|2|

    public Sprite getTrojanSp() throws java.io.IOException {//GEN-BEGIN:|5-getter|0|5-preInit
	if (trojanSp == null) {//GEN-END:|5-getter|0|5-preInit
	    // write pre-init user code here
	    trojanSp = new Sprite(getEnemyTrojan(), 24, 24);//GEN-BEGIN:|5-getter|1|5-postInit
	    trojanSp.setFrameSequence(trojanSpseq001);//GEN-END:|5-getter|1|5-postInit
	    // write post-init user code here
	}//GEN-BEGIN:|5-getter|2|
	return trojanSp;
    }
//GEN-END:|5-getter|2|

    public Image getEnemyWorm() throws java.io.IOException {//GEN-BEGIN:|7-getter|0|7-preInit
	if (enemyWorm == null) {//GEN-END:|7-getter|0|7-preInit
	    // write pre-init user code here
	    enemyWorm = Image.createImage("/images/enemyWorm.png");//GEN-BEGIN:|7-getter|1|7-postInit
	}//GEN-END:|7-getter|1|7-postInit
	// write post-init user code here
	return this.enemyWorm;//GEN-BEGIN:|7-getter|2|
    }
//GEN-END:|7-getter|2|

    public Sprite getWormSp() throws java.io.IOException {//GEN-BEGIN:|8-getter|0|8-preInit
	if (wormSp == null) {//GEN-END:|8-getter|0|8-preInit
	    // write pre-init user code here
	    wormSp = new Sprite(getEnemyWorm(), 24, 24);//GEN-BEGIN:|8-getter|1|8-postInit
	    wormSp.setFrameSequence(wormSpseq001);//GEN-END:|8-getter|1|8-postInit
	    // write post-init user code here
	}//GEN-BEGIN:|8-getter|2|
	return wormSp;
    }
//GEN-END:|8-getter|2|

    public Image getBG() throws java.io.IOException {//GEN-BEGIN:|10-getter|0|10-preInit
	if (BG == null) {//GEN-END:|10-getter|0|10-preInit
	    // write pre-init user code here
	    BG = Image.createImage("/images/backgrounds/BG.png");//GEN-BEGIN:|10-getter|1|10-postInit
	}//GEN-END:|10-getter|1|10-postInit
	// write post-init user code here
	return this.BG;//GEN-BEGIN:|10-getter|2|
    }
//GEN-END:|10-getter|2|

    public Image getExplode() throws java.io.IOException {//GEN-BEGIN:|12-getter|0|12-preInit
	if (explode == null) {//GEN-END:|12-getter|0|12-preInit
	    // write pre-init user code here
	    explode = Image.createImage("/images/explode.png");//GEN-BEGIN:|12-getter|1|12-postInit
	}//GEN-END:|12-getter|1|12-postInit
	// write post-init user code here
	return this.explode;//GEN-BEGIN:|12-getter|2|
    }
//GEN-END:|12-getter|2|

    public Sprite getExplodeSp() throws java.io.IOException {//GEN-BEGIN:|13-getter|0|13-preInit
	if (explodeSp == null) {//GEN-END:|13-getter|0|13-preInit
	    // write pre-init user code here
	    explodeSp = new Sprite(getExplode(), 30, 30);//GEN-BEGIN:|13-getter|1|13-postInit
	    explodeSp.setFrameSequence(explodeSpseq001);//GEN-END:|13-getter|1|13-postInit
	    // write post-init user code here
	}//GEN-BEGIN:|13-getter|2|
	return explodeSp;
    }
//GEN-END:|13-getter|2|



}

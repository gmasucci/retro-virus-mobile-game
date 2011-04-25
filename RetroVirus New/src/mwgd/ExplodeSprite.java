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
public class ExplodeSprite {

    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Image explode;
    private Sprite explodeSp;
    public int explodeSpseq001Delay = 40;
    public int[] explodeSpseq001 = {0, 0, 1, 1, 2, 2, 3, 3};
    //</editor-fold>//GEN-END:|fields|0|

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|

    public Image getExplode() throws java.io.IOException {//GEN-BEGIN:|1-getter|0|1-preInit
	if (explode == null) {//GEN-END:|1-getter|0|1-preInit
	    // write pre-init user code here
	    explode = Image.createImage("/images/explode.png");//GEN-BEGIN:|1-getter|1|1-postInit
	}//GEN-END:|1-getter|1|1-postInit
	// write post-init user code here
	return this.explode;//GEN-BEGIN:|1-getter|2|
    }
//GEN-END:|1-getter|2|

    public Sprite getExplodeSp() throws java.io.IOException {//GEN-BEGIN:|2-getter|0|2-preInit
	if (explodeSp == null) {//GEN-END:|2-getter|0|2-preInit
	    // write pre-init user code here
	    explodeSp = new Sprite(getExplode(), 30, 30);//GEN-BEGIN:|2-getter|1|2-postInit
	    explodeSp.setFrameSequence(explodeSpseq001);//GEN-END:|2-getter|1|2-postInit
	    // write post-init user code here
	}//GEN-BEGIN:|2-getter|2|
	return explodeSp;
    }
//GEN-END:|2-getter|2|

}

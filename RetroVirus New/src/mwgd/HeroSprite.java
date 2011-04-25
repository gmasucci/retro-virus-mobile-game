/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mwgd;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.io.IOException;


public class HeroSprite {

    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Image retro;
    private Sprite heroSp;
    public int heroSpseq001Delay = 200;
    public int[] heroSpseq001 = {0, 1, 2};
    //</editor-fold>//GEN-END:|fields|0|

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|

    public Image getRetro() throws java.io.IOException {//GEN-BEGIN:|1-getter|0|1-preInit
	if (retro == null) {//GEN-END:|1-getter|0|1-preInit
	    // write pre-init user code here
	    retro = Image.createImage("/images/retro.png");//GEN-BEGIN:|1-getter|1|1-postInit
	}//GEN-END:|1-getter|1|1-postInit
	// write post-init user code here
	return this.retro;//GEN-BEGIN:|1-getter|2|
    }
//GEN-END:|1-getter|2|

    public Sprite getHeroSp() throws java.io.IOException {//GEN-BEGIN:|2-getter|0|2-preInit
	if (heroSp == null) {//GEN-END:|2-getter|0|2-preInit
	    // write pre-init user code here
	    heroSp = new Sprite(getRetro(), 24, 24);//GEN-BEGIN:|2-getter|1|2-postInit
	    heroSp.setFrameSequence(heroSpseq001);//GEN-END:|2-getter|1|2-postInit
	    // write post-init user code here
	}//GEN-BEGIN:|2-getter|2|
	return heroSp;
    }
//GEN-END:|2-getter|2|

}

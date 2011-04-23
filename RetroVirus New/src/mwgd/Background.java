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
 * @author DarkArts
 */
public class Background {

    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Image bkg1;
    private Sprite bkgTop;
    public int bkgTopseq001Delay = 200;
    public int[] bkgTopseq001 = {0};
    private Image bkg2;
    private Sprite bkgMiddle;
    public int bkgMiddleseq001Delay = 200;
    public int[] bkgMiddleseq001 = {0};
    private Image bkg3;
    private Sprite bkgBottom;
    public int bkgBottomseq001Delay = 200;
    public int[] bkgBottomseq001 = {0};
    //</editor-fold>//GEN-END:|fields|0|

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|

    public Image getBkg1() throws java.io.IOException {//GEN-BEGIN:|1-getter|0|1-preInit
        if (bkg1 == null) {//GEN-END:|1-getter|0|1-preInit
            // write pre-init user code here
            bkg1 = Image.createImage("/images/backgrounds/bkg1.png");//GEN-BEGIN:|1-getter|1|1-postInit
        }//GEN-END:|1-getter|1|1-postInit
        // write post-init user code here
        return this.bkg1;//GEN-BEGIN:|1-getter|2|
    }
//GEN-END:|1-getter|2|



    public Sprite getBkgTop() throws java.io.IOException {//GEN-BEGIN:|5-getter|0|5-preInit
        if (bkgTop == null) {//GEN-END:|5-getter|0|5-preInit
            // write pre-init user code here
            bkgTop = new Sprite(getBkg1(), 240, 640);//GEN-BEGIN:|5-getter|1|5-postInit
            bkgTop.setFrameSequence(bkgTopseq001);//GEN-END:|5-getter|1|5-postInit
            // write post-init user code here
        }//GEN-BEGIN:|5-getter|2|
        return bkgTop;
    }
//GEN-END:|5-getter|2|

    public Image getBkg2() throws java.io.IOException {//GEN-BEGIN:|7-getter|0|7-preInit
        if (bkg2 == null) {//GEN-END:|7-getter|0|7-preInit
            // write pre-init user code here
            bkg2 = Image.createImage("/images/backgrounds/bkg2.png");//GEN-BEGIN:|7-getter|1|7-postInit
        }//GEN-END:|7-getter|1|7-postInit
        // write post-init user code here
        return this.bkg2;//GEN-BEGIN:|7-getter|2|
    }
//GEN-END:|7-getter|2|

    public Sprite getBkgMiddle() throws java.io.IOException {//GEN-BEGIN:|8-getter|0|8-preInit
        if (bkgMiddle == null) {//GEN-END:|8-getter|0|8-preInit
            // write pre-init user code here
            bkgMiddle = new Sprite(getBkg2(), 240, 640);//GEN-BEGIN:|8-getter|1|8-postInit
            bkgMiddle.setFrameSequence(bkgMiddleseq001);//GEN-END:|8-getter|1|8-postInit
            // write post-init user code here
        }//GEN-BEGIN:|8-getter|2|
        return bkgMiddle;
    }
//GEN-END:|8-getter|2|

    public Image getBkg3() throws java.io.IOException {//GEN-BEGIN:|10-getter|0|10-preInit
        if (bkg3 == null) {//GEN-END:|10-getter|0|10-preInit
            // write pre-init user code here
            bkg3 = Image.createImage("/images/backgrounds/bkg3.png");//GEN-BEGIN:|10-getter|1|10-postInit
        }//GEN-END:|10-getter|1|10-postInit
        // write post-init user code here
        return this.bkg3;//GEN-BEGIN:|10-getter|2|
    }
//GEN-END:|10-getter|2|

    public Sprite getBkgBottom() throws java.io.IOException {//GEN-BEGIN:|11-getter|0|11-preInit
        if (bkgBottom == null) {//GEN-END:|11-getter|0|11-preInit
            // write pre-init user code here
            bkgBottom = new Sprite(getBkg3(), 240, 640);//GEN-BEGIN:|11-getter|1|11-postInit
            bkgBottom.setFrameSequence(bkgBottomseq001);//GEN-END:|11-getter|1|11-postInit
            // write post-init user code here
        }//GEN-BEGIN:|11-getter|2|
        return bkgBottom;
    }
//GEN-END:|11-getter|2|

}

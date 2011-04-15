/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DarkArts
 */

package mwgd;
//  both the following required for sound
import java.io.*;
import javax.microedition.media.*;
//  need this for loading gfx . . .
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class Utils {
    //  image loading functions
    static public Image loadImage(String imgName, int type)    {
        try {
            switch(type){
                case 0:
                    return Image.createImage("images/" + imgName + ".png");
                case 1:
                    return Image.createImage("images/buttons/" + imgName + ".png");
                case 2:
                    return Image.createImage("images/backgrounds/" + imgName + ".png");
                default:
                    return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        return null;
        }
    }
    static public Image loadBkg(String bkgName)    {
        try {
            return Image.createImage("images/backgrounds/" + bkgName + ".png");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    static public Image loadBtn(String btnName)    {
        try {
            return Image.createImage("images/buttons/" + btnName +".png");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
//    static public Image loadImage(String imgName)    {
//        try {
//            return Image.createImage("images/" + imgName);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }
    //  sound loading functions
    static public void playWebWav(String webWavName)    {
        try
        {
            Player p = Manager.createPlayer("http://www.dark-arts.co.cc/"+webWavName+".wav");
            p.start();
        }
            catch (IOException ioe) { }
            catch (MediaException me) { }
    }
    static public Player playWav(String wavName, Class storedClass)    {
        Player p = null;
        try
          {	
            InputStream is = storedClass.getResourceAsStream("../sound/"+wavName);
            p = Manager.createPlayer(is, "audio/X-wav");
            
          }
          catch (IOException ioe) { }
          catch (MediaException me) { }
        return p;
    }

    /*
     * this function taken from blog.rafols.org and rearanged
     * to be more readable
     * */

    public static void rotateImage(Image img, float angle, Graphics gfx) {
        int width = img.getWidth();
        int height = img.getHeight();
        int [] srcDataBuffer = new int[width * height];
        int[] destDataBuffer = new int[width * height];

        img.getRGB(srcDataBuffer, 0, width, 0, 0, width, height);

        double rads = angle * Math.PI / 180.0f;
        float sa = (float) Math.sin(rads);
        float ca = (float) Math.cos(rads);
        int isa = (int) (256 * sa);
        int ica = (int) (256 * ca);

        int my = - (height >> 1);
        for(int i = 0; i < height; i++) {
            int wpos = i * width;

            int xacc = my * isa - (width >> 1) * ica + ((width >> 1) << 8);
            int yacc = my * ica + (width >> 1) * isa + ((height >> 1) << 8);

            for(int j = 0; j < width; j++) {
                int srcx = (xacc >> 8) & 0xff;
                int srcy = yacc & 0xff00;

                destDataBuffer[wpos++] = srcDataBuffer[srcx + srcy];

                xacc += ica;
                yacc -= isa;
            }
            my++;
        }
        // drawrgb definition
        //  public void drawRGB(int[] rgbData, int offset, int scanlength,
        //  int x, int y, int width, int height, boolean processAlpha)
        gfx.drawRGB(destDataBuffer, 0, width, 0, 0, width, height, true);
        }
    public static void rotateSprite(Sprite sprite, float angle, Graphics gfx)
    {
        /*
         * code to be added soon
         * */

        //rotateImage((Image)sprite.), angle, gfx);  // does not work ahem

    }
}

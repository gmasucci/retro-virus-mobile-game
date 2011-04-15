/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mwgd;

import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

/**
 * @author Neil
 */
public class MainMidlet extends MIDlet implements CommandListener {


    private Display display;
    private SplashMenu splash;

    public static final Command exitCommand = new Command("Exit",Command.EXIT,1);

    public void commandAction(Command c, Displayable d) {
        if(c == exitCommand)
            exitMIDlet();
    }



    public Display getDisplay(){return display;}

    private void exitMIDlet() {
        notifyDestroyed();
    }



    protected void startApp() throws MIDletStateChangeException {
        if(display==null){
            display = Display.getDisplay(this);
            try {
                splash = new SplashMenu();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            splash.addCommand(exitCommand);
            splash.setCommandListener(this);

            getDisplay().setCurrent(splash);
            splash.startMe();
        }
    }

    protected void pauseApp() {

    }

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        exitMIDlet();
    }



}

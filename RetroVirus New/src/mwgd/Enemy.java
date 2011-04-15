/**
 *
 * @author DarkArts
 */

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.*;

public class Enemy {
    private GameCanvas on_canvas;    //  canvas to appear on
    private float x, y;             //  current x,y position
    private float velX, velY;       //  current speed components
    private int nmeLevel = 1;
    private int maxOnScreenShots = 2;

    static public int CREATED   =   0;
    static public int ALIVE     =   1;
    static public int DYING     =   2;
    static public int DEAD      =   4;

    private Sprite[] sprites;


    public Enemy(){};
    public Enemy(float startPosX, float startPosY, float ivelx, float ively, int nmeLev, int maxShots){
        x       =   startPosX;      y    =   startPosY;
        velX    =   ivelx;          velY =   ively;
        nmeLevel=   nmeLev;         maxOnScreenShots    =   maxShots;
    };
    public Enemy(float startPosX, float startPosY, float ively){
        x       =   startPosX;      y    =  startPosY;
        velX    =   0;              velY =  ively;
        nmeLevel=   1;              maxOnScreenShots    =   2;
    }
    public Enemy(float stuffhere){

    }



}
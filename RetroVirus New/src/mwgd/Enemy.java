package mwgd;

import java.io.IOException;
import java.util.Random;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.*;

public class Enemy {

    private Sprite mySprite;
    private int myType,health,xDeath,yDeath,movecounter,k;
    private boolean doIShoot;
    private boolean haveIShotAlready;
    public Virus virusSprites;
    public ExplodeSprite exp;


    private int STATE;
    private final static int ALIVE	= 0;
    private final static int DYING	= 1;
    private final static int DEAD	= 2;
    private final static int SHOOTING	= 3;

    Enemy(int xpos) throws IOException{
	exp = new ExplodeSprite();
	virusSprites = new Virus();
	create(xpos);

    }


    public int getType(){ return myType;}
    public void setType(int type){this.myType = type;}

    public boolean shoots(){return this.doIShoot;}
    public void setShoots(boolean shoots){doIShoot = shoots;}

    private void create(int xpos) throws IOException{
	Random j = new Random();
	j.setSeed(k+System.currentTimeMillis());
	k = j.nextInt(100);
	if(k<50){

		mySprite = virusSprites.getVirusSp();
		health = 0;
		myType = 0;
	}else if(k>=50 && k < 75){
		mySprite = virusSprites.getTrojanSp();
		health = 1;
		myType = 1;
	}else if(k>=75){
		mySprite = virusSprites.getWormSp();
		health = 1;
		myType = 2;
	}
	j.setSeed(k+System.currentTimeMillis());
	k = j.nextInt(240-mySprite.getWidth());
	mySprite.setPosition(k, -mySprite.getHeight());
	STATE = ALIVE;
    }
    public void kill() throws IOException{
	if (health > 0){
	    health--;
	}else{
	    xDeath = mySprite.getX();
	    yDeath = mySprite.getY();
	    mySprite = exp.getExplodeSp();
	    mySprite.setFrame(0);
	    mySprite.setPosition(xDeath, yDeath);
	    STATE = DYING;
	    destroy(true);
	}
    }

    public void destroy(boolean explode) throws IOException{
	if(explode){
	    int j = mySprite.getFrame();
	    if(j==7){
		STATE = DEAD;
	    }
	}else{
	    STATE = DEAD;
	}


    }

    public void setState(int state){
	if(state < 4){
	    if (state>=0){
		STATE = state;
	    }
	}
    }
    public Sprite getSprite(){
	return mySprite;
    }
    public void update() throws IOException{

	
	switch(STATE){
	    case ALIVE:
		if(mySprite.getY() > 320){
		    destroy(false);
		}else{
		    mySprite.nextFrame();
		    if(myType == 0){
			mySprite.move(0,1);
		    }else if (myType == 1){
			if(movecounter>1){
			    mySprite.move(0,1);
			    movecounter=0;
			}else{
			    movecounter++;
			}
		    }else{
			mySprite.move(0,1);
		    }
		}
		break;
	    case DYING:
		mySprite.nextFrame();
		destroy(true);
		
		break;
	    case DEAD:
	    Random j = new Random();
	    j.setSeed(k+System.currentTimeMillis());
	    int k = j.nextInt(240 - mySprite.getWidth());
		create(k);
		break;
	    case SHOOTING:
		//shoot
		mySprite.nextFrame();
		mySprite.move(0,1);
		break;


	}
	
	
    }
    public void draw(Graphics g){
	mySprite.paint(g);
    }

}
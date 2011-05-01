/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mwgd;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.lcdui.*;
/**
 *
 * @author Neil
 */
public class HighScoreSystem {
    private String url = "http://www.mcm-high-scores.appspot.com/score?game=retrovirus";
    private String name = "";
    private String email = "retro@retro.rv";
    private String response="";
    private String geturl = "http://www.mcm-high-scores.appspot.com/scoretable?game=retrovirus";


    //send score;
    private String uploadScore(String url) throws IOException{

	StringBuffer b = new StringBuffer();
	InputStream is = null;
	HttpConnection con = null;
	try {
		// code to download data goes here...
                con = (HttpConnection)Connector.open( url );
                is = con.openInputStream();
                int ch = 0;
                while( (ch = is.read()) != -1 )
                    b.append((char) ch);
                return b.toString();
        }  finally {
		if(is != null)
		    is.close();
		if(con!=null)
		    con.close();

    }
    }

    //method called when gameover - win or lose.
    public String upload(int score) throws IOException{
        if (this.name==null) {name ="RetroPlayer";}
        String uri = url+"&nickname="+this.name+"&email="+email+"&score="+score;
        response = uploadScore(uri);
        return response;
    }

    //asks user permission to access web.
    //if they say no, close app.
    public boolean init() throws IOException{
	HttpConnection h = null;
        name = System.getProperty("user.name");
	boolean allowed;
	try{
	h = (HttpConnection)Connector.open( url );
	} catch(java.lang.SecurityException se){
	    h=null;
	}
	if(h==null){
	    allowed = false;
	}else{
	    allowed = true;
	    h.close();
	}
	return allowed;
	
    }

    	public String getScores() throws IOException {
		StringBuffer b = new StringBuffer();
		InputStream is = null;
		HttpConnection con = null;
		try {
			// code to download data goes here...
                        con = (HttpConnection)Connector.open( geturl );
                        is = con.openInputStream();
                        int ch = 0;
                        while( (ch = is.read()) != -1 )
                            b.append((char) ch);
                        return b.toString();

		}  finally {
			is.close();
			con.close();
		}
	}
}//end

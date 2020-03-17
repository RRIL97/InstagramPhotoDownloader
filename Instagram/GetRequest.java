 
package Instagram;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

 
public class GetRequest {
    /**
    * Get Request Class
    * Used to send a GET request to the instagram server.
    */ 
    
    private URL                tUrl;
    private InputStream        tInputStream;
    private BufferedReader     tBufferedReader;
    private HttpsURLConnection tHttpsURLConnection;
      
    /* 
    Input is a website URL
    Returns Source code of the page. Upon timeout "EXCEPTION" will return.
    */ 
    public String initate(String PageUrl)
    { 
    tInputStream    = null;
    tBufferedReader = null;
        
    String Output = "";
    String Tmp    = "";

     try {
        tUrl                = new URL(PageUrl);
        tHttpsURLConnection = (HttpsURLConnection) tUrl.openConnection();
        tInputStream        = tHttpsURLConnection.getInputStream();
        
        tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));

        while ((Tmp = tBufferedReader.readLine()) != null) 
             Output += (Tmp+"\n");
        
        tBufferedReader.close();
        tInputStream.close();  
        
     }catch(Exception GrabSourceException)
     { 
         Output = "EXCEPTION";
     }
     return Output;
    }
    
}

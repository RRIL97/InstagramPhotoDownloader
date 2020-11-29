 
package Instagram;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList; 

 
public class User {
    
    /*
    This class handles the user activites such as photo download, link fetching.
    */
   
    private String UserId;
    private String              Username;            
    private PictureLinksFetcher PicLinkGrabber        = null; 
    private Thread              PicLinkGrabberThread  = null;
   
     
    private ArrayList<String>    UserPicUrls;  
    private GetRequest           UserGetRequest;
    
    public User(String UserToFetch) {
    Username        = UserToFetch;
    UserPicUrls     = new ArrayList<>(); 
    UserGetRequest  = new GetRequest(); 
    UserId          = getUserId();
    }  
     
    public boolean userExists(){
        return (UserId != null); 
    }
    public String getUserId(){
        try{
            String Tmp = UserGetRequest.initate("https://www.instagram.com/"+Username+"/?__a=1"); 
            if(Tmp.equals("EXCEPTION"))  //Upon timeout/ip blacklist/invalid username
                UserId = null;
            else{
            UserId     = Tmp.split("profilePage_")[1].split("\",\"show_suggested_profiles")[0];  //Fetch ID
            System.out.println("Located User Id - "+UserId );
            }
        }catch(Exception grabUserException){
            UserId = null;
        }
        return UserId;
    } 
     
    public ArrayList<String> getPictureLinks()
    {  
      try{
      PicLinkGrabber         = new PictureLinksFetcher(Username,UserId,UserPicUrls);  
      PicLinkGrabberThread   = new Thread(PicLinkGrabber);   
      PicLinkGrabberThread.start();
      PicLinkGrabberThread.join();
      }catch(Exception photoGrabException){
          photoGrabException.printStackTrace();
      }    
      return UserPicUrls;
    } 
    
    public void downloadAllPictures(){
        if(UserPicUrls.size() > 0)
        {
             File outputFolder = new File(Username);
             if(outputFolder.exists())
                 outputFolder.delete();
             
             outputFolder.mkdir();
             
             PictureDownloader picDown;
             Thread            picDownThread;
             ArrayList<Thread> downloaderThreads = new ArrayList<>(); 
             
             for(int i = 0 ;i < UserPicUrls.size(); i++){
                 try{
                 picDown = new PictureDownloader(UserPicUrls.get(i), outputFolder+"//"+i+".jpg");
                 picDownThread = new Thread(picDown);
                 picDownThread.start();
                 downloaderThreads.add(picDownThread);
                  
                 }catch(Exception pictureDownloadException){
                      System.out.println("Error saving -" + UserPicUrls.get(i));
                      pictureDownloadException.printStackTrace();
                 }
             }
            
        }
        
        System.out.println("\r\nFinished Downloading!");
    }
    
    public void saveLinks()
    {
            if(UserPicUrls.size() > 0){
                
            String outputFileName    = Username+".txt"; 
            System.out.println("Saving log file as " + outputFileName); 
            
            try{
            Files.write(Paths.get(outputFileName),UserPicUrls,Charset.defaultCharset());
            
            }catch(Exception logFileWriteException){
                System.out.println("Logfile write error! " + logFileWriteException.getMessage());
            }
            }
    }
}

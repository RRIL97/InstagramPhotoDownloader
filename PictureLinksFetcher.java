 
package Instagram;

import java.util.ArrayList;

 
public class PictureLinksFetcher implements Runnable {
    /**
    * Instagram Picture Link Fetcher
    * Fetches all photo links on a specific profile 
    */
    
    private String            Username;
    private String            UserId; 
    private String            PageSource;
    private String            PageID    ;   
    
    private final String      PhotoQueryId = "e769aa130647d2354c40ea6a439bfc08"; //Hardcoded instagram value. (For photos)
    
    private GetRequest        UserGetRequest;     
    private ArrayList<String> PhotoLinks; 
    
    public PictureLinksFetcher(String Username,String UserId,ArrayList<String> PhotoLinks)
    {
        this.Username    = Username;
        this.UserId      = UserId;
        this.PhotoLinks  = PhotoLinks;
        UserGetRequest   = new GetRequest();
    }
    
    @Override
    public void run() {
   
           boolean UserHasMorePhotos = false; //Stores current status
           
           PageSource              = UserGetRequest.initate("https://www.instagram.com/"+Username+"/");  
           String LastPicInRequest = "" ; 
           
           do{
           try{
           PageID                 = PageSource.split("end_cursor\":\"")[1].split("\"},\"edges\"")[0]; //Current PageId (Used For Browsing Photos)
           String tUrls []        = PageSource.split("cdninstagram.com/"); 
           
           LastPicInRequest       = "https://scontent-ams4-1.cdninstagram.com/"+tUrls[tUrls.length-1].split("\",\"")[0].replaceAll("\\\\u0026","&");
           
           ///Instagram will keep sending the same picture if you reach the last photo.
           UserHasMorePhotos      = (!PhotoLinks.contains(LastPicInRequest)); 
           
           //Grab Photos
           for(int i = 2 ; i < tUrls.length; i++){ 
                   String tmpPicURL = "https://scontent-ams4-1.cdninstagram.com/"+tUrls[i].split("\",\"")[0].replaceAll("\\\\u0026","&");
                   
                   if(!PhotoLinks.contains(tmpPicURL))
                   PhotoLinks.add(tmpPicURL);    
           }
           System.out.println("Scraped - "+ PhotoLinks.size() + " pictures in total!");
           
           //Get the next photos by the id we have found
           PageSource = UserGetRequest.initate("https://www.instagram.com/graphql/query/?query_hash="+PhotoQueryId+"&variables=%7B%22id%22%3A%22"+UserId+"%22%2C%22first%22%3A12%2C%22after%22%3A%22"+PageID+"%22%7D");   
           Thread.sleep(5000);
           
           }catch(Exception PictureFetchException){
               System.out.println("Links Fetcher Exception - " + PictureFetchException.getMessage() + ". Number Pictures Fetched -"+PhotoLinks.size() );
               PageSource = "EXCEPTION"; 
           }    
           }while(UserHasMorePhotos && !PageSource.equals("EXCEPTION")); 
    }
    
}

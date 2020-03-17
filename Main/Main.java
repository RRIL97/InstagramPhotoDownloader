 
package Main;

import Instagram.User; 
import java.util.ArrayList;
import java.util.Scanner;

 
public class Main {
     
    /*
    Main class
    */
    public static void main(String [] args)
    {
        Scanner InputScanner  = new Scanner(System.in);
        
        System.out.println("Instagram Photo Scraper. Input Username-");
        
        String  userInput = InputScanner.nextLine();
        
        String toFetch                     = userInput; 
        User   InstaUser                   = new User(toFetch); 
        
        if(InstaUser.userExists()){ //Fetched instagram id successfully 
            
        ArrayList<String> userPhotoLinks   = InstaUser.getPictureLinks();   
        System.out.println("Located total - " +userPhotoLinks.size() + " pictures."); 
        
        System.out.println("Download? (yes/no)");
        String toDownload = InputScanner.nextLine().toLowerCase();  
        if(toDownload.equals("yes")){ 
            InstaUser.downloadAllPictures(); //start picture download
        }else{
            InstaUser.saveLinks(); //save links without download
        }
        }else
        System.out.println("User doesn't exists."); //Can't fetch instagram id.
        
    }
}

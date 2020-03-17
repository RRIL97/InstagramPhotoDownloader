 
package Instagram;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream; 
import java.net.URL;
 

public class PictureDownloader implements Runnable
{ 
    /**
    * Image Downloader Class
    * Saves image from url to destination 
    */
    private String source; 
    private String dest; 
    
    public PictureDownloader(String source, String dest)
    {
        this.source = source;
        this.dest   = dest;
    }
  
    public void downloadImage() 
    {
        try{
        URL          imageUrl    = new URL(source);
        InputStream  imageReader = new BufferedInputStream (imageUrl.openStream());
        OutputStream imageWriter = new BufferedOutputStream(new FileOutputStream(dest));
        {
            int imageByte; 
            while ((imageByte = imageReader.read()) != -1)
            {
                imageWriter.write(imageByte);
            }
        }
        System.out.println("Saved to -" + dest);
    }catch(Exception imageDownloadException){
        System.out.println("Exception Downloading Photo - " + source);
        imageDownloadException.printStackTrace();
    }
    }

    @Override
    public void run() {
        downloadImage();
    }
}
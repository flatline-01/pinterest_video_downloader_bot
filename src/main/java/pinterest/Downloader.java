package pinterest;

import java.util.concurrent.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class Downloader implements Callable<InputFile>{
    public static final String PINTEREST_VIDEO_DOWNLOADING_WEBSITE = "https://www.expertstool.com/download-pinterest-video-online-1/";
    private String _Url;

    public Downloader(String url) {
        _Url = url;
    }
 
    public InputFile call(){
        InputFile inputFile = null;
        try {
            Connection.Response  response =  Jsoup.connect(PINTEREST_VIDEO_DOWNLOADING_WEBSITE)
                    .method(Connection.Method.POST)
                    .data("url", _Url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .followRedirects(true)
                    .execute();
            Document doc = response.parse();
            String videoLink = doc.select("video").attr("src");
            if (videoLink != null && videoLink != " ") {
                URL videoURL = new URI(doc.select("video").attr("src")).toURL();
                URLConnection connection = videoURL.openConnection();
                InputStream is = connection.getInputStream();
                inputFile = new InputFile();
                inputFile.setMedia(is, videoURL.getPath().toString().substring(25));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return inputFile;
    }
}

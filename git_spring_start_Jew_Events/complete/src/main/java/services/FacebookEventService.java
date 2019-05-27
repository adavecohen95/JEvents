package services;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

public class FacebookEventService {


    public String testMethod() {
        return "Service calls work ";
    }


    private final String KEY_API = "EAAHZCDnb8thQBADVAWB2gXjBoa1va4dIab96PjRM3bLv2Vv4Y2KHRrhp0usYUYkEh6k0PZCS7bx9ThQ8N6Gm9CuQhdBnDGpqPoojZCBGHeYZCa53rp5HknPynMPHwuEtnutaPCAZC81s9rfnndlPZBGN2XzkZB4jwZCfOYqUZBwhQSkF90s3liKRl9Jo8ZAYzOrZAVV23zoZA5R4IAZDZD";
    private final String GRAPH_API_URL = "https://graph.facebook.com/v3.3/2422730031089996/events";
   // ?pretty=0&type=not_replied&limit=200&access_token=

    public void getEvents() {

        URL graphUrl = null;
        HttpURLConnection con = null;
        int responseCode = 0;
        try {
            graphUrl  = new URL(GRAPH_API_URL);
        } catch(MalformedURLException e) {

        }

        try {
            con = (HttpURLConnection) graphUrl.openConnection();
        } catch (IOException e) {

        }

        try {
            con.setRequestMethod("GET");
        }catch (ProtocolException e) {

        }

        con.setRequestProperty("pretty","0");
        con.setRequestProperty("type","not_replied");
        con.setRequestProperty("limit","200");
        con.setRequestProperty("access_token",KEY_API);

        try {
            responseCode = con.getResponseCode();


           String text = IOUtils.toString(con.getInputStream(), StandardCharsets.UTF_8.name());
        } catch (IOException e) {

        }



    }


}

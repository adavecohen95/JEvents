package services;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URIBuilder;

public class FacebookEventService {

    private static final String KEY_API = "EAAHZCDnb8thQBAMpZA3XS6qfS4i5uc9WMlAk79f7pkx1fUBw5f2sZBbn6o54zcTsZChObQeIk1tbZAck57OCXR09fq832YbGoBmwM38f0EWEMX6KENbpY32ZA128PU5LYRj4GAVc6LM5VWXfvvGQB1YpMVxeUVMMNZC0ZBy5YZB0o1BaqRwn8ZCckdYtfoqyiXZBZB2D1R22sP7LSAZDZD";
    private static final String GRAPH_API_URL = "https://graph.facebook.com/v3.3/2422730031089996/events";
    private URIBuilder fbURI;
    private HttpURLConnection con;
    private int responseCode;

    public void getEvents() throws IOException, URISyntaxException {

        try {
            fbURI  = new URIBuilder(GRAPH_API_URL)
            .addParameter("pretty","1")
            .addParameter("type","not_replied")
            .addParameter("limit","200")
            .addParameter("access_token",KEY_API);

            System.out.println(fbURI.toString());

            URL url = new URL(fbURI.toString());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            responseCode = con.getResponseCode();
            String text = IOUtils.toString(con.getInputStream(), StandardCharsets.UTF_8.name());
            System.out.println(text);

        } catch (URISyntaxException e) {
            System.out.println("URI Syntax is incorrect");
            throw e;

        } catch (IOException e) {
            System.out.println("Failed to create a URL Object or failed to get get input from facebook ");
            System.out.println("Response Code: " + responseCode);
            throw e;
        }
    }
    public static void main(String args[]) throws Exception {
        new FacebookEventService().getEvents();
    }


}

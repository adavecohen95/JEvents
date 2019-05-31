package services;



import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;
import models.facebookEvents.FacebookEvent;
import models.FacebookEventList;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URIBuilder;


public class FacebookEventService {

    private static final String KEY_API = "EAAHZCDnb8thQBAB64oU0jO3wzs62kkTT11gLqCd97MaRp54ecPO8KI1d62cumkaRsEOi2nzTXPPCubKwxZACPnoW0GShRpCyzR7xdRK0TJk7guNiKjZBrpY5lQHEDaOCp1ZA9CdTsfGy9pqpHRZAmCqWvqSc8nzhBsA0g1hVQhSpclFYBI6H9TDLQGza6cSIlPKT54XsZA6wZDZD";
    private static final String GRAPH_API_URL = "https://graph.facebook.com/v3.3/2422730031089996/events";
    private URIBuilder fbURI;
    private HttpURLConnection con;
    private int responseCode;


    public FacebookEventService(){}

    public String testA = " { \"description\": \"Join us for a celebration of sustainability! The Zero Waste Fair, presented by the City of Encinitas, hosted by ILACSD, returns to the EUSD Farm Lab on Saturday, June 1st at 10 AM. The event is open to the public.\\n\\nDrop in and visit educational booths addressing topics such as bulk shopping, plant nutrition, daily zero waste habits, and more. Participate in one of the hands-on activities for all ages, including a kids craft and a make-and-take project! Bring in gently used items you were planning to donate and participate in the Swap \\u2018n\\u2019 Shop! Participants can exchange or donate those items giving them a second life and keeping them out of the landfill.\\n\\nThe highlight of the fair will be an interactive speaking panel featuring zero waste innovators, experts, and industry leaders. Don't forget to stick around for our raffle! All attendees present have the opportunity to win sustainable prizes that will help with your zero waste journey!\\n\\nRegister here: https://goo.gl/forms/CdxkHMkY6Ka9FwkN2\", \"end_time\": \"2019-06-01T13:00:00-0700\", \"name\": \"Zero Waste Fair\", \"place\": { \"name\": \"EUSD Farm Lab\", \"location\": { \"city\": \"Encinitas\", \"country\": \"United States\", \"latitude\": 33.056201463993, \"longitude\": -117.27839033823, \"state\": \"CA\", \"street\": \"441 Quail Gardens Dr\", \"zip\": \"92024\" }, \"id\": \"1404838366405369\" }, \"start_time\": \"2019-06-01T10:00:00-0700\", \"id\": \"147590732792846\", \"rsvp_status\": \"attending\" }";

    public List<FacebookEvent> getEvents() throws IOException, URISyntaxException {

        try {
            fbURI  = new URIBuilder(GRAPH_API_URL)
            .addParameter("pretty","1")
            .addParameter("limit","200")
            .addParameter("access_token",KEY_API);

            System.out.println(fbURI.toString());

            URL url = new URL(fbURI.toString());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            responseCode = con.getResponseCode();
            String text = IOUtils.toString(con.getInputStream(), StandardCharsets.UTF_8.name());
            Gson gson = new Gson();
            FacebookEventList facebookEventList = gson.fromJson(text,FacebookEventList.class);

            for(FacebookEvent facebookEvent: facebookEventList.data) {
                System.out.println(facebookEvent);
            }

            return facebookEventList.data;

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

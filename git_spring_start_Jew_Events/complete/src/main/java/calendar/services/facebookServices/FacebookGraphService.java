package calendar.services.facebookServices;

import calendar.factories.FacebookEventServiceFactory;
import calendar.scheduler.CalendarScheduler;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import calendar.models.FacebookResposne;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//All print statements will be converted to log statements

/**
 * This class is meant to be an all purpose service class for Facebook Graph Api calls.
 */
public class FacebookGraphService {

  /*
    The following variables all serve the purpose of configuring the service towards the user's specific graph api request
   */
  private URIBuilder fbURI;
  private HttpURLConnection con;
  private int responseCode;
  private static final Logger log = LoggerFactory.getLogger(CalendarScheduler.class);

  private String _authToken;
  private String _graphUrl;
  private Map<String, String> _parameters;

  public FacebookGraphService(String authToken, String graphUrl, String userId, Map<String, String> parameters) {
    this._authToken = authToken;
    this._graphUrl = String.format(graphUrl, userId);
    this._parameters = parameters;
  }

  /**
   * Creates the URL object from the configurations given in the constructor.
   * @return The URL object that the service will connect to in order to send the request.
   * Includes the parameters being set in the GET request.
   * @throws URISyntaxException
   * @throws MalformedURLException
   */
  private URL createURL() throws URISyntaxException, MalformedURLException {

    try {
      fbURI = new URIBuilder(_graphUrl).addParameter("access_token", _authToken);

      _parameters.keySet().forEach(key -> {
        fbURI.addParameter(key, _parameters.get(key));
      });

      System.out.println("[INFO] Facebook GET URL: " + fbURI.toString());
      return new URL(fbURI.toString());
    } catch (URISyntaxException e) {
      System.out.println("[EXCEPTION] Failed to form a URI Object from input");
      throw e;
    } catch (MalformedURLException e) {
      System.out.println("[EXCEPTION] Failed to form a URL Object from input");
      throw e;
    }
  }

  /**
   * Makes calls to facebook under the configurations set in the constructor and returns the response.
   * @return  The Object form of the the JSON response from facebook. Including the errors or data that is received.
   * @throws URISyntaxException
   * @throws IOException
   */
  public FacebookResposne getResponse() throws URISyntaxException, IOException {

    Gson gson = new Gson();
    FacebookResposne facebookResposne = null;
    String output = null;
    try {

      URL url = createURL();
      con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");

      log.debug("GET URL " + url.getPath());

      responseCode = con.getResponseCode();
      InputStream inputStream = null;
      inputStream =
          (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) ? (con.getInputStream()) : (con.getErrorStream());

      output = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
      facebookResposne = gson.fromJson(output, FacebookResposne.class);


    log.info("ResponseCode: " + responseCode);

      con.disconnect();
    } catch (IOException e) {
      System.out.println("[EXCEPTION] Failed to create a URL object or failed to get get output from facebook.");
      System.out.println("\t\t Check to make sure the auth token is correct.");
      System.out.println("\t\t Response Code: " + responseCode);
      throw e;
    }

    return facebookResposne;
  }
  public static void main(String args[]) throws IOException, URISyntaxException {
    FacebookEventService facebookEventService = FacebookEventServiceFactory.createInstance();
    System.out.println(new Gson().toJson(facebookEventService.getEvents()));

  }
}

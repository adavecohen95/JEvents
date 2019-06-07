package calendar.services.facebookServices;

import java.io.IOException;
import java.net.URISyntaxException;
import calendar.models.FacebookResposne;


/**
 *  This class gets all of the facebook events that we get from our user.
 */
public class FacebookEventService {

  private FacebookGraphService _facebookGraphService;

  public FacebookEventService(FacebookGraphService facebookGraphService) {
    this._facebookGraphService = facebookGraphService;
  }

  /**
   * Using the facebook graph api service, we can make calls to get facebook events of the user.
   * @return Returns the response from making a call to get Batch API Events.
   * @throws IOException
   * @throws URISyntaxException
   */
  public FacebookResposne getEvents() throws IOException, URISyntaxException {
    return _facebookGraphService.getResponse();
  }
}

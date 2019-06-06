package services.facebookServices;

import java.io.IOException;
import java.net.URISyntaxException;
import models.FacebookResposne;


public class FacebookEventService {

  private FacebookGraphService _facebookGraphService;

  public FacebookEventService(FacebookGraphService facebookGraphService) {
    this._facebookGraphService = facebookGraphService;
  }

  public FacebookResposne getEvents() throws IOException, URISyntaxException {
    return _facebookGraphService.getResponse();
  }
}

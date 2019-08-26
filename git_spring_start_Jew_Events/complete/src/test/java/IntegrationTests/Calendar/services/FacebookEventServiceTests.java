package IntegrationTests.Calendar.services;

import calendar.factories.FacebookEventServiceFactory;
import calendar.models.FacebookResposne;
import org.junit.Assert;
import org.junit.Before;
import calendar.services.facebookServices.FacebookEventService;
import org.junit.Test;
import org.mortbay.log.Log;

import java.io.IOException;
import java.net.URISyntaxException;


public class FacebookEventServiceTests {

  private FacebookEventService _facebookEventService;

  @Before
  public void setUp() {
    _facebookEventService = FacebookEventServiceFactory.createInstance();
  }

  /**
   * This test will be made available once there is a final decision made on how to handle auth tokens.
   * Until then it will remain local only.
   */
  @Test
  public void testGetEvents() throws IOException, URISyntaxException {
    FacebookResposne facebookResposne = _facebookEventService.getEvents();
    Assert.assertNotNull(facebookResposne);
    Assert.assertEquals(null, facebookResposne.error);
    Log.info("Testing Get Facebook Events resulted in " + facebookResposne.data.size() + " events pulled");
  }
}

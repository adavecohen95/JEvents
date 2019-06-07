package services;

import factories.FacebookEventServiceFactory;
import org.junit.Before;
import services.facebookServices.FacebookEventService;


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
//  @Test
//  public void testGetEvents() throws IOException, URISyntaxException {
//    FacebookResposne facebookResposne = _facebookEventService.getEvents();
//    System.out.println(facebookResposne);
//
//    Assert.assertEquals(null, facebookResposne.error);
//  }
}

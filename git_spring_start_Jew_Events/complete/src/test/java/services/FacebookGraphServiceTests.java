package services;

import factories.FacebookEventServiceFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import models.FacebookResposne;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.facebookServices.FacebookEventService;


public class FacebookGraphServiceTests {

  private FacebookEventService _facebookEventService;

  @Before
  public void setUp() {
    _facebookEventService = FacebookEventServiceFactory.createInstance();
  }

  @Test
  public void testGetEvents() throws IOException, URISyntaxException {
    FacebookResposne facebookResposne = _facebookEventService.getEvents();
    System.out.println(facebookResposne);

    Assert.assertEquals(null, facebookResposne.error);
  }
}

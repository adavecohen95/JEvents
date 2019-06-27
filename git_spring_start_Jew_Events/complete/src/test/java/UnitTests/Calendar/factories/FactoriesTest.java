package UnitTests.Calendar.factories;

import calendar.factories.CalendarJobFactory;
import calendar.factories.FacebookErrorsFactory;
import calendar.factories.FacebookEventServiceFactory;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.junit.Assert;
import org.junit.Test;


public class FactoriesTest {


  @Test
  public void testCalendarJobFactory() {
    Assert.assertNotNull(CalendarJobFactory.createInstance());
  }

  @Test
  public void testFacebookErrorsFactory() {
    Assert.assertNotNull(FacebookErrorsFactory.createInstance());
  }

  @Test
  public void testFacebookEventServiceFactory() {
    Assert.assertNotNull(FacebookEventServiceFactory.createInstance());
  }

  @Test
  public void testTest() throws GeneralSecurityException, IOException {
    Assert.assertNotNull(GoogleNetHttpTransport.newTrustedTransport());
  }


}

package UnitTests.Calendar.jobs;

import calendar.jobs.CalendarJob;
import calendar.services.GoogleCalendarService;
import calendar.services.facebookServices.FacebookEventService;
import calendar.factories.FacebookErrorsFactory;
import calendar.models.CalendarEvent;
import calendar.models.FacebookResposne;
import calendar.models.facebookEvents.FacebookErrorResponse;
import calendar.util.FacebookErrors;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;


//public class CalendarJobTests {
//
//@Mock private FacebookEventService _facebookEventServiceData;
//@Mock private FacebookEventService _facebookEventServiceError;
//@Mock private GoogleCalendarService _googleCalendarService;
//
//private CalendarJob _calendarJob;
//private CalendarJob _calendarJobError;
//private FacebookErrors _facebookErrors;
//
//  @Before
//  public void setUp() throws IOException, URISyntaxException, GeneralSecurityException {
//
//    MockitoAnnotations.initMocks(this);
//
//    FacebookResposne facebookResposneError = new FacebookResposne();
//    facebookResposneError.error = new FacebookErrorResponse();
//    facebookResposneError.error.code = 204;
//
//    FacebookResposne facebookResposneData = new FacebookResposne();
//    facebookResposneData.data = new ArrayList<CalendarEvent>();
//
//    when(_facebookEventServiceData.getEvents()).thenReturn(facebookResposneData);
//    when(_facebookEventServiceError.getEvents()).thenReturn(facebookResposneError);
//
//
//    _facebookErrors = FacebookErrorsFactory.createInstance();
//
//    _calendarJob = new CalendarJob(_facebookEventServiceData,_googleCalendarService,_facebookErrors);
//    _calendarJobError = new CalendarJob(_facebookEventServiceError, _googleCalendarService, _facebookErrors);
//  }
//
//  @Test
//  public void testStartJob() {
//    Assert.assertTrue(_calendarJob.runJob());
//  }
//
//  @Test
//  public void testStartJobError() {
//    Assert.assertFalse(_calendarJobError.runJob());
//  }
//


//}

package UnitTests.Calendar.jobs;

import calendar.jobs.CalendarJob;
import calendar.services.GoogleCalendarService;
import calendar.models.facebookEvents.FacebookCalendarEvent;
import calendar.services.facebookServices.FacebookEventService;
import calendar.factories.FacebookErrorsFactory;
import calendar.models.FacebookResposne;
import calendar.models.facebookEvents.FacebookErrorResponse;
import calendar.util.FacebookErrors;
import calendar.util.ResonseActions.SuccessfulResponseAction;
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


public class CalendarJobTests {

@Mock private FacebookEventService _facebookEventServiceData;
@Mock private FacebookEventService _facebookEventServiceError;
@Mock private GoogleCalendarService _googleCalendarService;
@Mock private SuccessfulResponseAction _successfulResponseAction;

private CalendarJob _calendarJob;
private CalendarJob _calendarJobError;
private FacebookErrors _facebookErrors;

private static final int FACEBOOK_RESPONSE_CODE = 204;

  @Before
  public void setUp() throws IOException, URISyntaxException, GeneralSecurityException {

    MockitoAnnotations.initMocks(this);

    FacebookResposne facebookResposneError = new FacebookResposne();
    facebookResposneError.error = new FacebookErrorResponse();
    facebookResposneError.error.code = FACEBOOK_RESPONSE_CODE;

    FacebookResposne facebookResposneData = new FacebookResposne();
    facebookResposneData.data = new ArrayList<FacebookCalendarEvent>();

    when(_facebookEventServiceData.getEvents()).thenReturn(facebookResposneData);
    when(_facebookEventServiceError.getEvents()).thenReturn(facebookResposneError);

    _facebookErrors = FacebookErrorsFactory.createInstance();

    _calendarJob = new CalendarJob(_facebookEventServiceData,_googleCalendarService,_facebookErrors,
        _successfulResponseAction);
    _calendarJobError = new CalendarJob(_facebookEventServiceError, _googleCalendarService, _facebookErrors,
        _successfulResponseAction);
  }

  @Test
  public void testStartJob() {
    Assert.assertTrue(_calendarJob.runJob());
  }

//  @Test
//  public void testStartJobError() {
//    Assert.assertFalse(_calendarJobError.runJob());
//  }



}

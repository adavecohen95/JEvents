package UnitTests.Calendar.jobs;

import calendar.models.FacebookResposne;
import calendar.models.facebookEvents.FacebookErrorResponse;
import IntegrationTests.Calendar.services.GoogleCalendarService;
import IntegrationTests.Calendar.services.facebookServices.FacebookEventService;
import calendar.util.AbstractAction;
import calendar.util.FacebookErrors;


public class CalendarJob {


  private FacebookEventService _facebookEventService;
  private GoogleCalendarService _googleCalendarService;
  private FacebookErrors _facebookErrors;

  public CalendarJob(FacebookEventService facebookEventService, GoogleCalendarService googleCalendarService,
      FacebookErrors facebookErrors) {
    _facebookEventService = facebookEventService;
    _googleCalendarService = googleCalendarService;
    _facebookErrors = facebookErrors;
  }

  public boolean startJob() {

   try {
     FacebookResposne facebookResposne = _facebookEventService.getEvents();
     FacebookErrorResponse facebookErrorResponse = facebookResposne.error;

     if(facebookErrorResponse != null) {
       int errorCode = FacebookErrors.boundResponseCode(facebookErrorResponse.code);
       AbstractAction errorAction = _facebookErrors.getAction(errorCode);
       return errorAction.action(facebookErrorResponse);
     }
     else {
       _googleCalendarService.AddEventsFromFacebook(facebookResposne.data);
     }
   } catch(Exception e) {
     return false;
   }

   return true;

  }

}

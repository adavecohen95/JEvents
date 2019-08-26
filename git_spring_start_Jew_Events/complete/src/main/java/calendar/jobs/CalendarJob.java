package calendar.jobs;

import calendar.common.GeneralHelper;
import calendar.models.FacebookResposne;
import calendar.models.facebookEvents.FacebookErrorResponse;
import calendar.scheduler.CalendarScheduler;
import calendar.services.GoogleCalendarService;
import calendar.services.facebookServices.FacebookEventService;
import calendar.util.AbstractAction;
import calendar.util.FacebookErrors;
import calendar.util.ResonseActions.SuccessfulResponseAction;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CalendarJob {


  private FacebookEventService _facebookEventService;
  private GoogleCalendarService _googleCalendarService;
  private FacebookErrors _facebookErrors;
  private AbstractAction _okayResponse;

  private static final Logger log = LoggerFactory.getLogger(CalendarJob.class);

  public CalendarJob(FacebookEventService facebookEventService, GoogleCalendarService googleCalendarService,
    FacebookErrors facebookErrors, AbstractAction okayResponse) {
    _facebookEventService = facebookEventService;
    _googleCalendarService = googleCalendarService;
    _facebookErrors = facebookErrors;
    _okayResponse = okayResponse;
  }

  public boolean runJob() {

   try {
     FacebookResposne facebookResposne = _facebookEventService.getEvents();
     FacebookErrorResponse facebookErrorResponse = facebookResposne.error;

     if(facebookErrorResponse != null) {
       int errorCode = FacebookErrors.boundResponseCode(facebookErrorResponse.code);
       AbstractAction errorAction = _facebookErrors.getAction(errorCode);
       log.info("Job Failed");
       return errorAction.action(facebookResposne);
     }

     log.info("Successfully Ran Job");
     _okayResponse.action(facebookResposne);
     log.info("Number of events pulled: " + facebookResposne.data.size());

     _googleCalendarService.AddEventsFromFacebook(GeneralHelper.
             convertToCalendarEvents(facebookResposne));

   } catch(Exception e) {
     log.debug("Job caught an exception ",e);
     e.printStackTrace();
     return false;
   }

   return true;

  }

}

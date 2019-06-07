package calendar.jobs;

import calendar.services.GoogleCalendarService;
import calendar.services.facebookServices.FacebookEventService;


public class CalendarJob {


  private FacebookEventService _facebookEventService;
  private GoogleCalendarService _googleCalendarService;

  public CalendarJob(FacebookEventService facebookEventService, GoogleCalendarService googleCalendarService) {
    _facebookEventService = facebookEventService;
    _googleCalendarService = googleCalendarService;
  }

  public void startJob() {

  }

}

package calendar;

import calendar.factories.GoogleCalendarServiceFactory;
import calendar.models.CalendarEvent;
import calendar.services.GoogleCalendarService;
import com.google.api.client.util.DateTime;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  @RequestMapping("/greeting")
  public TestObject greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
    return new TestObject(
        counter.incrementAndGet(),
        String.format(template, name),
        new ArrayList<CalendarEvent>() {});
  }

  @RequestMapping("/sync")
  public SyncResponse sync(@RequestParam(value = "code", defaultValue = "") String code) {
     GoogleCalendarService googleEventService;
      try {
        googleEventService = GoogleCalendarServiceFactory.createInstance();
      } catch (IOException e) {
        return SyncResponse.CreateAuthNeeded("IOException: " + e);
      } catch (GeneralSecurityException e) {
        return SyncResponse.CreateAuthNeeded("GeneralSecurityException: " + e);
      }

      if (googleEventService == null) {
        return SyncResponse.CreateAuthNeeded("Error: GoogleCalendarServiceFactory returned null!");
      }

    if (code.compareTo("") != 0) {
      googleEventService.handleAuthCode(code);
      return SyncResponse.CreateAuthNeeded("Submitted code: " + code);
    }

    try {
      System.out.println("Authorizing...");
      if (!googleEventService.isAuthorized()) {
        return SyncResponse.CreateAuthNeeded(googleEventService.getAuthorizationUrl());
      }
      System.out.println("Setup...");
      if (!googleEventService.isSetup()) {
        googleEventService.PostAuthorizationSetup();
      }
      System.out.println("Fetching Events...");
      List<CalendarEvent> events = googleEventService.ListEvents();
      return SyncResponse.CreateSyncResponse(events);
    } catch (Exception e) {
      return SyncResponse.CreateAuthNeeded("Error: " + e);
    }
  }

  // Huge gaping security hole just for testing PLEASE FOR THE LOVE OF GOD
  // REMOVE THIS BEFORE PUBLISHING THE SERVICE.
  @RequestMapping("/add")
  public AddResponse add(@RequestParam(value = "title", defaultValue = "default title") String title,
                         @RequestParam(value="desc", defaultValue = "default description") String desc) {
    GoogleCalendarService googleEventService;
    try {
      googleEventService = GoogleCalendarServiceFactory.createInstance();
    } catch (IOException e) {
      return AddResponse.CreateAuthNeeded("IOException: " + e);
    } catch (GeneralSecurityException e) {
      return AddResponse.CreateAuthNeeded("GeneralSecurityException: " + e);
    }

    if (googleEventService == null) {
      return AddResponse.CreateAuthNeeded("Error: GoogleCalendarServiceFactory returned null!");
    }

    try {
      if (!googleEventService.isSetup()) {
        return AddResponse.CreateAuthNeeded("authorization not setup...");
      }
      CalendarEvent event = new CalendarEvent();
      event.title = title;
      event.description = desc;
      DateTime start = new DateTime(System.currentTimeMillis());
      DateTime end = new DateTime(System.currentTimeMillis() + 1000000);
      event.startTime = new Date(start.getValue());
      event.endTime = new Date(end.getValue());
      googleEventService.CreateGoogleEvent(event);
      return AddResponse.CreateAddResponse(event);
    } catch (Exception e) {
      return AddResponse.CreateAuthNeeded("Error: " + e);
    }
  }

  @RequestMapping("/list")
  public SyncResponse list() {
    GoogleCalendarService googleEventService;
    try {
      googleEventService = GoogleCalendarServiceFactory.createInstance();
    } catch (IOException e) {
      return SyncResponse.CreateAuthNeeded("IOException: " + e);
    } catch (GeneralSecurityException e) {
      return SyncResponse.CreateAuthNeeded("GeneralSecurityException: " + e);
    }

    if (googleEventService == null) {
      return SyncResponse.CreateAuthNeeded("Error: GoogleCalendarServiceFactory returned null!");
    }

    try {
      if (!googleEventService.isSetup()) {
        googleEventService.PostAuthorizationSetup();
      }
      List<CalendarEvent> events = googleEventService.ListEvents();
      return SyncResponse.CreateSyncResponse(events);
    } catch (Exception e) {
      return SyncResponse.CreateAuthNeeded("Error: " + e);
    }
  }
}

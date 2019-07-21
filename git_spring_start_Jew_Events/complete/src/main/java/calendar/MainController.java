package calendar;

import calendar.models.CalendarEvent;
import calendar.services.GoogleCalendarService;
import com.google.api.client.util.DateTime;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  public GoogleCalendarService googleEventService;

  @RequestMapping("/greeting")
  public TestObject greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
    return new TestObject(
        counter.incrementAndGet(),
        String.format(template, name),
        new ArrayList<CalendarEvent>() {});
  }

  @RequestMapping("/sync")
  public SyncResponse sync(@RequestParam(value = "code", defaultValue = "") String code) {
    if (googleEventService == null) {
      try {
        googleEventService = new GoogleCalendarService();
      } catch (IOException e) {
        return SyncResponse.CreateAuthNeeded("IOException: " + e);
      } catch (GeneralSecurityException e) {
        return SyncResponse.CreateAuthNeeded("GeneralSecurityException: " + e);
      }
    }

    if (code.compareTo("") != 0) {
      googleEventService.handleAuthCode(code);
      return SyncResponse.CreateAuthNeeded("Submitted code: " + code);
    }

    try {
      if (!googleEventService.isAuthorized()) {
        return SyncResponse.CreateAuthNeeded(googleEventService.getAuthorizationUrl());
      }
      if (!googleEventService.isSetup()) {
        googleEventService.PostAuthorizationSetup();
      }
      List<CalendarEvent> events = googleEventService.ListEvents();
      return SyncResponse.CreateSyncResponse(events);
    } catch (Exception e) {
      return SyncResponse.CreateAuthNeeded("Error: " + e);
    }
  }

  // Huge gaping security hole just for testing PLEASE FOR THE LOVE OF GOD
  // REMOVE THIS BEFORE PUBLISHING THE SERVICE.
  @RequestMapping("/add")
  public AddResponse add(@RequestParam(value = "title", defaultValue = "default title") String title, @RequestParam(value="desc", defaultValue = "default description") String desc) {
    if (googleEventService == null) {
      try {
        googleEventService = new GoogleCalendarService();
      } catch (IOException e) {
        return AddResponse.CreateAuthNeeded("IOException: " + e);
      } catch (GeneralSecurityException e) {
        return AddResponse.CreateAuthNeeded("GeneralSecurityException: " + e);
      }
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
      event.startTime = start;
      event.endTime = end;
      googleEventService.CreateGoogleEvent(event);
      return AddResponse.CreateAddResponse(event);
    } catch (Exception e) {
      return AddResponse.CreateAuthNeeded("Error: " + e);
    }
  }

  @RequestMapping("/list")
  public SyncResponse list() {
    if (googleEventService == null) {
      try {
        googleEventService = new GoogleCalendarService();
      } catch (IOException e) {
        return SyncResponse.CreateAuthNeeded("IOException: " + e);
      } catch (GeneralSecurityException e) {
        return SyncResponse.CreateAuthNeeded("GeneralSecurityException: " + e);
      }
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

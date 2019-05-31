package calendar;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import models.CalendarEvent;
import services.FacebookEventService;
import services.GoogleCalendarService;

@RestController
public class MainController {

  private static final String template = "Hello, %s!";
  private static final FacebookEventService facebookEventService = new FacebookEventService();
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
  public SyncResponse sync() {
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
}

package calendar;

import Models.CalendarEvent;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
  public TestObject sync() {
    if (googleEventService == null) {
      try {
        googleEventService = GoogleCalendarService.CreateSyncService();
      } catch (IOException e) {
        return new TestObject(
            counter.incrementAndGet(),
            "IO Error accessing Google Calendar",
            new ArrayList<CalendarEvent>() {});
      } catch (GeneralSecurityException e) {
        return new TestObject(
            counter.incrementAndGet(),
            "GeneralSecurityException accessing Google Calendar",
            new ArrayList<CalendarEvent>() {});
      }
    }

    List<CalendarEvent> events = googleEventService.ListEvents();
    return new TestObject(counter.incrementAndGet(), "Events in sync", events);
  }
}

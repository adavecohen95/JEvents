package calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.GeneralSecurityException;
import org.springframework.web.bind.annotation.RestController;
import services.AbstractCalendarEvent;
import services.FacebookEventService;
import services.GoogleCalendarService;

@RestController
public class MainController {

    private static final String template = "Hello, %s!";
    private static final FacebookEventService facebookEventService = new FacebookEventService();
    private final AtomicLong counter = new AtomicLong();

    public GoogleCalendarService googleEventService;

    @RequestMapping("/greeting")
    public TestObject greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new TestObject(counter.incrementAndGet(),
                            String.format(template, name), new ArrayList<AbstractCalendarEvent>(){});
    }

    @RequestMapping("/sync")
    public TestObject sync() {
      if (googleEventService == null) {
        try {
           googleEventService = GoogleCalendarService.CreateSyncService();
        } catch(IOException e) {
        return new TestObject(
            counter.incrementAndGet(),
            "IO Error accessing Google Calendar",
            new ArrayList<AbstractCalendarEvent>() {});
        } catch (GeneralSecurityException e) {
          return new TestObject(
              counter.incrementAndGet(),
              "GeneralSecurityException accessing Google Calendar",
              new ArrayList<AbstractCalendarEvent>() {});
        }
      }
      
      List<AbstractCalendarEvent> events = googleEventService.ListEvents();
      return new TestObject(counter.incrementAndGet(), "Events in sync", events);

    }


    @RequestMapping("/serviceCall")
    public TestObject fbServiceCall() {
        return new TestObject(counter.incrementAndGet(),
                String.format(template, facebookEventService.testMethod()), new ArrayList<AbstractCalendarEvent>());
    }

}

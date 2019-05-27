package calendar;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import services.FacebookEventService;

@RestController
public class MainController {

    private static final String template = "Hello, %s!";
    private static final String boratTemplate = "What kind of %s is this";
    private static final FacebookEventService facebookEventService = new FacebookEventService();
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public TestObject greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new TestObject(counter.incrementAndGet(),
                            String.format(template, name));
    }

    @RequestMapping("/serviceCall")
    public TestObject fbServiceCall() {
        return new TestObject(counter.incrementAndGet(),
                String.format(template, facebookEventService.testMethod()));
    }

}

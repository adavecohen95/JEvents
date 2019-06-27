package calendar.factories;

import UnitTests.Calendar.jobs.CalendarJob;
import IntegrationTests.Calendar.services.GoogleCalendarService;
import IntegrationTests.Calendar.services.facebookServices.FacebookEventService;
import calendar.util.FacebookErrors;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class CalendarJobFactory extends AbstractFactory{

  private CalendarJobFactory() {
  }

  /**
   * This class is the configuration class that holds the configuration.
   */
  @Configuration
  static class ConfigClass {

    @Bean
    CalendarJob calendarJob() throws GeneralSecurityException, IOException {
      return new CalendarJob(facebookEventService(),googleCalendarService(),facebookErrors());
    }

    FacebookEventService facebookEventService() {
      return FacebookEventServiceFactory.createInstance();
    }

    GoogleCalendarService googleCalendarService() throws GeneralSecurityException, IOException {
      return new GoogleCalendarService();
    }

    FacebookErrors facebookErrors() {
      return FacebookErrorsFactory.createInstance();
    }
  }

  public static CalendarJob createInstance() {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(CalendarJobFactory.ConfigClass.class);
    ctx.refresh();

    return ctx.getBean(CalendarJob.class);
  }
}

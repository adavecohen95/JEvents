package calendar.factories;

import calendar.jobs.CalendarJob;
import calendar.services.GoogleCalendarService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleCalendarServiceFactory extends AbstractFactory {


    @Configuration
    static class ConfigClass {

        private GoogleCalendarService googleCalendarService;

        @Bean
        GoogleCalendarService googleCalendarService() throws GeneralSecurityException, IOException {
            if(googleCalendarService == null)
                googleCalendarService = new GoogleCalendarService();

            return googleCalendarService;
        }


    }

    public static GoogleCalendarService createInstance() throws GeneralSecurityException, IOException{
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(GoogleCalendarServiceFactory.ConfigClass.class);
        ctx.refresh();
        return ctx.getBean(GoogleCalendarService.class);
    }

}

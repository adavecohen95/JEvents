package calendar.scheduler;

import calendar.factories.CalendarJobFactory;
import UnitTests.Calendar.jobs.CalendarJob;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;


@Configuration
@EnableScheduling
public class CalendarScheduler extends AbstractScheduler implements SchedulingConfigurer {

  private static final Logger log = LoggerFactory.getLogger(CalendarScheduler.class);

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  private static CalendarJob calendarJob = CalendarJobFactory.createInstance();

  @Bean
  public Executor taskExecutor() {
    return Executors.newScheduledThreadPool(100);
  }


//  @Scheduled(fixedRate = nextIteration)
//  public void job() {
//    calendarJob.startJob();
//
//  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(taskExecutor());
    taskRegistrar.addTriggerTask(
        new Runnable() {
          @Override public void run() {
            log.info("Calendar has been updated at", dateFormat.format(new Date()));
            calendarJob.startJob();
          }
        },
        new Trigger() {
          @Override public Date nextExecutionTime(TriggerContext triggerContext) {
            Calendar nextExecutionTime =  new GregorianCalendar();
            Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
            nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
            nextExecutionTime.add(Calendar.SECOND, 1); //you can get the value from wherever you want
            return nextExecutionTime.getTime();
          }
        }
    );
  }


}

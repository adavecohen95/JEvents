package calendar.scheduler;

import calendar.factories.CalendarJobFactory;
import calendar.jobs.CalendarJob;
import calendar.util.AbstractAction;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

    Executor executor = taskExecutor();
    taskRegistrar.setScheduler(executor);
    taskRegistrar.addTriggerTask(
        new Runnable() {
          @Override
          public void run() {
//            log.info("Calendar Job has started at " + dateFormat.format(new Date()));

//            if(false /*calendarJob.startJob()*/) {
//              ((ScheduledExecutorService) executor).shutdown();
//              taskRegistrar.destroy();
//              log.info("Calendar Job has terminated at " + dateFormat.format(new Date()));
//            }

//            log.info("Calendar has been updated at " + dateFormat.format(new Date()));


            calendarJob.runJob();
            log.info("Ran Job" + dateFormat.format(new Date()));

          }
        },
        new Trigger() {
          @Override public Date nextExecutionTime(TriggerContext triggerContext) {
            Calendar nextExecutionTime =  new GregorianCalendar();
            Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
            nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
            //nextExecutionTime.add(Calendar.MINUTE, AbstractAction.getIncreasedUpdateTime()); //you can get the value from wherever you want
            nextExecutionTime.add(Calendar.SECOND,10);
            return nextExecutionTime.getTime();
          }
        }
    );
  }


}

package calendar.util;

import calendar.models.general.Response;
import calendar.scheduler.CalendarScheduler;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractAction {

  public static final int MINS_IN_DAY = 1440;
  public static final int MINS_IN_SIX_HOURS = 350;
  public static final int STANDARD_UPDATE_TIME = 10;

  protected static int increasedUpdateTime = 10;
  protected static final int updateUnit = Calendar.SECOND;
  protected static boolean continueProcess = true;

  protected static final Logger log = LoggerFactory.getLogger(AbstractAction.class);
  protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  public static int getIncreasedUpdateTime() {
    log.info("Update speed is now " + increasedUpdateTime + " minutes");
    return increasedUpdateTime;
  }
  public static int getUpdateUnit() {
    return updateUnit;
  }
  public static boolean continueProcess() {
    return continueProcess;
  }

  public abstract boolean action(Response response);


}

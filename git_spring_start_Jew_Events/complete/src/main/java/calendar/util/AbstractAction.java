package calendar.util;

import calendar.models.general.Response;


public abstract class AbstractAction {

  public long miliSecondsInDay = 0x5265C00L;
  public long miliSecondsIn6Hours = 0x36EE80L;

  protected long increasedUpdateTime = 0L;
  protected boolean failProcess = false;

  public long getIncreasedUpdateTime() {
    return increasedUpdateTime;
  }

  public abstract boolean action(Response response);


}

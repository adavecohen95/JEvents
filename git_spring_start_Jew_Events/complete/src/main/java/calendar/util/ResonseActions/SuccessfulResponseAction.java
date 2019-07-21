package calendar.util.ResonseActions;

import calendar.models.general.Response;
import calendar.util.AbstractAction;
import java.util.Date;


public class SuccessfulResponseAction extends AbstractAction {

  public SuccessfulResponseAction () {

  }

  @Override
  public boolean action(Response response) {
    log.info("Successful response from Facebook at: " + dateFormat.format(new Date()));
    failProcess = false;
    increasedUpdateTime = STANDARD_UPDATE_TIME;
    return failProcess;
  }
}

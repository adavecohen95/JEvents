package calendar.util.ResonseActions;

import calendar.models.general.Response;
import calendar.util.AbstractAction;
import java.util.Date;


public class APIRateLimitFailureAction extends AbstractAction {

  public APIRateLimitFailureAction() {

  }

  @Override
  public boolean action(Response response) {
    log.debug("API Rate Limit failure at: " + dateFormat.format(new Date()));
    increasedUpdateTime = MINS_IN_SIX_HOURS;
    return continueProcess;
  }

}

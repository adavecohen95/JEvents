package calendar.util.ResonseActions;

import calendar.models.general.Response;
import calendar.util.AbstractAction;
import java.util.Date;


public class OauthFailureAction extends AbstractAction {

  public OauthFailureAction() {
  }

  @Override
  public boolean action(Response response) {
    log.debug("Oath failure at: " + dateFormat.format(new Date()));
    increasedUpdateTime = MINS_IN_DAY;
    return continueProcess;
  }
}

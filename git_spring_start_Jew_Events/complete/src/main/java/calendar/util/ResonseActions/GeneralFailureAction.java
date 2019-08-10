package calendar.util.ResonseActions;

import calendar.models.general.Response;
import calendar.util.AbstractAction;
import java.util.Date;


public class GeneralFailureAction extends AbstractAction {

  public GeneralFailureAction() {
  }

  @Override
  public boolean action(Response response) {
    log.debug("General failure at: " + dateFormat.format(new Date()));
    increasedUpdateTime = MINS_IN_SIX_HOURS;
    return continueProcess;
  }
}

package calendar.util.ResonseActions;

import calendar.models.general.Response;
import calendar.util.AbstractAction;


public class APIRateLimitFailureAction extends AbstractAction {

  public APIRateLimitFailureAction() {
    increasedUpdateTime = miliSecondsInDay;
  }

  @Override
  public boolean action(Response response) {
    return true;
  }

}

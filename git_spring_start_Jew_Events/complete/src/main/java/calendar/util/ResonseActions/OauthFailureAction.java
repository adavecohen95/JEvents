package calendar.util.ResonseActions;

import calendar.models.general.Response;
import calendar.util.AbstractAction;


public class OauthFailureAction extends AbstractAction {

  public OauthFailureAction() {
    increasedUpdateTime = miliSecondsInDay;
  }

  @Override
  public boolean action(Response response) {
    return failProcess;
  }
}

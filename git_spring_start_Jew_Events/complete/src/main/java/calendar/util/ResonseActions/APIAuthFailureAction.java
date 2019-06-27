package calendar.util.ResonseActions;

import calendar.models.general.Response;
import calendar.util.AbstractAction;


public class APIAuthFailureAction extends AbstractAction {

  public APIAuthFailureAction() {
    failProcess = true;
  }

  @Override
  public boolean action(Response response) {
    return failProcess;
  }
}

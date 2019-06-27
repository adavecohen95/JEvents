package calendar.util.ResonseActions;

import calendar.models.general.Response;
import calendar.util.AbstractAction;


public class AppAuthFailureAction extends AbstractAction {

  public AppAuthFailureAction() {
    failProcess = true;
  }

  @Override
  public boolean action(Response response) {
    return failProcess;
  }
}

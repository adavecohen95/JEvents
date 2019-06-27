package calendar.util.ResonseActions;

import calendar.models.general.Response;
import calendar.util.AbstractAction;


public class GeneralFailureAction extends AbstractAction {

  public GeneralFailureAction() {
    increasedUpdateTime = miliSecondsIn6Hours;
  }

  @Override
  public boolean action(Response response) {
    return failProcess;
  }
}

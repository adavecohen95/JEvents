package calendar.util.ResonseActions;

import calendar.models.general.Response;
import calendar.util.AbstractAction;
import java.util.Date;


public class AppAuthFailureAction extends AbstractAction {

  public AppAuthFailureAction() {

  }

  @Override
  public boolean action(Response response) {
    log.debug("App Authentication failure at: " + dateFormat.format(new Date()));
    failProcess = true;
    return failProcess;
  }
}

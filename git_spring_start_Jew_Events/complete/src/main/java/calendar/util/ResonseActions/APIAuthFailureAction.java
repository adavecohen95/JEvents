package calendar.util.ResonseActions;

import calendar.models.general.Response;
import calendar.util.AbstractAction;
import java.util.Date;
import org.apache.http.client.utils.DateUtils;


public class APIAuthFailureAction extends AbstractAction {

  public APIAuthFailureAction() {
  }

  @Override
  public boolean action(Response response) {
    log.debug("API Authentication failure occurred at: " + dateFormat.format(new Date()));
    continueProcess = false;
    return continueProcess;
  }
}

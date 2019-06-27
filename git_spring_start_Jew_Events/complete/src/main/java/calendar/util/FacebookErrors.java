package calendar.util;

import calendar.common.GeneralHelper;
//import com.sun.tools.javah.Gen;
import java.util.Map;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import sun.java2d.loops.FillRect;


@Configurable
public class FacebookErrors {

  private Map<Integer,AbstractAction> facebookErrorActions;
  public static final int API_PERMISSION_ERRORS_START = 200;
  public static final int API_PERMISSION_ERRORS_END = 299;

  public FacebookErrors(Map<Integer, AbstractAction> map) {
    facebookErrorActions = map;
  }

  public static int boundResponseCode(int code) {
     if(GeneralHelper.withinRange(code,API_PERMISSION_ERRORS_START,
         API_PERMISSION_ERRORS_END)) {
       return API_PERMISSION_ERRORS_START;
     }
     return code;
  }

  public AbstractAction getAction(int errorCode) {
    return facebookErrorActions.get(errorCode);
  }


}

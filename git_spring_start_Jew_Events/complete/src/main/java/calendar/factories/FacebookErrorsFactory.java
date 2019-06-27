package calendar.factories;

import calendar.util.AbstractAction;
import calendar.util.FacebookErrors;
import calendar.util.ResonseActions.APIAuthFailureAction;
import calendar.util.ResonseActions.APIRateLimitFailureAction;
import calendar.util.ResonseActions.GeneralFailureAction;
import calendar.util.ResonseActions.OauthFailureAction;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * This class is a factory for creating FacebookGraphService Objects.
 */
public class FacebookErrorsFactory extends AbstractFactory{

  private FacebookErrorsFactory() {
  }


  /**
   * This class is the configuration class that holds the configuration.
   */
  @Configuration
  static class ConfigClass {

    @Bean
    FacebookErrors facebookErrors() {
      return new FacebookErrors(facebookErrorsMap());
    }

    Map<Integer, AbstractAction> facebookErrorsMap() {
      Map<Integer, AbstractAction> facebookActions = new HashMap<>();
      facebookActions.put(1,       new GeneralFailureAction());
      facebookActions.put(2,       new GeneralFailureAction());
      facebookActions.put(4,       new APIRateLimitFailureAction());
      facebookActions.put(10,      new APIAuthFailureAction());
      facebookActions.put(17,      new APIRateLimitFailureAction());
      facebookActions.put(32,      new APIRateLimitFailureAction());
      facebookActions.put(102,     new OauthFailureAction());
      facebookActions.put(190,     new OauthFailureAction());
      facebookActions.put(200,     new APIAuthFailureAction());
      facebookActions.put(341,     new APIRateLimitFailureAction());
      facebookActions.put(368,     new GeneralFailureAction());
      facebookActions.put(506,     new GeneralFailureAction());
      facebookActions.put(1609005, new GeneralFailureAction());

      return facebookActions;
    }

  }

  /**
   * Creates an instance of FacebookEventService. Uses the factory method and dependency injection.
   * @return An instance of FacebookEventService with the configurations to make FacebookEvent batch calls.
   */
  public static FacebookErrors createInstance() {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(FacebookErrorsFactory.ConfigClass.class);
    ctx.refresh();

   return ctx.getBean(FacebookErrors.class);
  }
}





package factories;

import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.facebookServices.FacebookEventService;
import services.facebookServices.FacebookGraphService;


/**
 * This class is a factory for creating FacebookGraphService Objects.
 */
public class FacebookEventServiceFactory {

  private FacebookEventServiceFactory() {
  }

  private static final String configFile = "config/FacebookConfig.xml";

  /**
   * This class is the configuration class that is configured from the config files.
   */
  @Configuration
  static class ConfigClass {

    private String authToken;
    private String graphUrl;
    private String userId;
    private Map<String, String> parameters;

    public void setAuthToken(String AuthToken) {
      this.authToken = AuthToken;
    }

    public void setGraphUrl(String GraphUrl) {
      this.graphUrl = GraphUrl;
    }

    public void setUserId(String userId) {
      this.userId = userId;
    }

    public void setParameters(Map<String, String> parameters) {
      this.parameters = parameters;
    }
  }

  /**
   * Creates an instance of FacebookEventService. Uses the factory method and dependency injection.
   * @return An instance of FacebookEventService with the configurations to make FacebookEvent batch calls.
   */
  public static FacebookEventService createInstance() {
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:" + configFile);
    ConfigClass configClass = applicationContext.getBean("ConfigClass", FacebookEventServiceFactory.ConfigClass.class);
    return new FacebookEventService(
        new FacebookGraphService(configClass.authToken, configClass.graphUrl, configClass.userId,
            configClass.parameters));
  }
}





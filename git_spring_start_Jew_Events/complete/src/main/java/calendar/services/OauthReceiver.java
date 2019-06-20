package calendar.services;

import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import java.util.concurrent.Semaphore;

public class OauthReceiver implements VerificationCodeReceiver {
  public OauthReceiver(String redirectUri) {
    semaphore_ = new Semaphore(1);
    semaphore_.drainPermits();
    code_ = "";
    redirectUri_ = redirectUri;
  }

  public void stop() {
    code_ = "";
    semaphore_.release();
  }

  public String getRedirectUri() {
    return redirectUri_;
  }

  public void submitCode(String code) {
    System.out.println("Releasing semaphore and adding code!");
    code_ = code;
    semaphore_.release();
  }

  public String waitForCode() {
    System.out.println("Waiting for code...");
    try {
      semaphore_.acquire();
      System.out.println("Code received!!!");
      return code_;
    } catch (InterruptedException e) {
      return "";
    }
  }

  String code_;
  String redirectUri_;
  Semaphore semaphore_;
}

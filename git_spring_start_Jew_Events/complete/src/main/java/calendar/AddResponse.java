package calendar;

import java.util.ArrayList;
import java.util.List;
import calendar.models.CalendarEvent;


public class AddResponse {

  public static AddResponse CreateAddResponse(CalendarEvent event) {
    return new AddResponse(event);
  }

  public static AddResponse CreateAuthNeeded(String authenticationUrl) {
    return new AddResponse(authenticationUrl);
  }

  private final CalendarEvent event_;
  private final String authenticationUrl_;
  private String message_ = "";

  private AddResponse(CalendarEvent event) {
    event_ = event;
    authenticationUrl_ = "";
  }

  private AddResponse(String authenticationUrl) {
    authenticationUrl_ = authenticationUrl;
    event_ = null;
  }

  void SetMessage(String message) {
    message_ = message;
  }

  public CalendarEvent getEvents() {
    return event_;
  }

  public String getAuthenticationUrl() {
    return authenticationUrl_;
  }

  public String getMessage() {
    return message_;
  }
}

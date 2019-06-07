package calendar;

import java.util.ArrayList;
import java.util.List;
import calendar.models.CalendarEvent;


public class SyncResponse {

  public static SyncResponse CreateSyncResponse(List<CalendarEvent> events) {
    return new SyncResponse(events);
  }

  public static SyncResponse CreateAuthNeeded(String authenticationUrl) {
    return new SyncResponse(authenticationUrl);
  }

  private final List<CalendarEvent> _events;
  private final String _authenticationUrl;
  private String _message = "";

  private SyncResponse(List<CalendarEvent> events) {
    _events = events;
    _authenticationUrl = "";
  }

  private SyncResponse(String authenticationUrl) {
    _authenticationUrl = authenticationUrl;
    _events = new ArrayList<CalendarEvent>();
  }

  void SetMessage(String message) {
    _message = message;
  }

  public List<CalendarEvent> getEvents() {
    return _events;
  }

  public String getAuthenticationUrl() {
    return _authenticationUrl;
  }

  public String getMessage() {
    return _message;
  }
}

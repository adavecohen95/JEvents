package calendar;

import java.util.List;
import calendar.models.CalendarEvent;


public class TestObject {

  private final long id;
  private final String content;
  private final List<CalendarEvent> _events;

  public TestObject(long id, String content, List<CalendarEvent> events) {
    this.id = id;
    this.content = content;
    this._events = events;
  }

  public long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  public List<CalendarEvent> getEvents() {
    return _events;
  }
}

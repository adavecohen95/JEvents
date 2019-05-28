package calendar;

import java.util.List;
import Models.AbstractCalendarEvent;

public class TestObject {

    private final long id;
    private final String content;
    private final List<AbstractCalendarEvent> _events;

    public TestObject(long id, String content, List<AbstractCalendarEvent> events) {
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

    public List<AbstractCalendarEvent> getEvents() {
      return _events;
    }
}

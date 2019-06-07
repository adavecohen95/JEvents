package calendar.models;

import com.google.api.client.util.DateTime;


public class CalendarEvent {
  public String facebookEventId;
  public String googleEventId;
  // Use this to detect if the event has been modified. Same event ID but
  // different event Etag implies that the event's details have been changed.
  public String googleEventEtag;

  public DateTime startTime;
  public DateTime endTime;

  public String description;
  public String title;

  public String googleEventUrl;

  public String toString() {
    return "{\nfbeid: "
        + facebookEventId
        + "\ngeid: "
        + googleEventId
        + "\ngoogleetag: "
        + googleEventEtag
        + "\nstart_t: "
        + startTime.toString()
        + "\nend_t: "
        + endTime.toString()
        + "\ndescription: "
        + description
        + "\ntitle: "
        + title
        + "\nURL: "
        + googleEventUrl
        + "\n";
  }
}

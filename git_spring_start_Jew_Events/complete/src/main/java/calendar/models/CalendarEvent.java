package calendar.models;

import calendar.models.facebookEvents.FacebookPlace;
import com.google.api.client.util.DateTime;
import com.google.gson.Gson;

public class CalendarEvent {

  /* really obsolete fields */
  public String description;
  public String end_time;
  public String name;
  public FacebookPlace place;
  public String start_time;
  public long id;
  public String rsvp_status;
  /* End facebook event fields */

  /* shiny new fields */
  public String facebookEventId;
  public DateTime startTime;
  public DateTime endTime;
  //public String description;
  public String title;
  /*End Obsolete fields */

  public String googleEventId;
  // Use this to detect if the event has been modified. Same event ID but
  // different event Etag implies that the event's details have been changed.
  public String googleEventEtag;
  public String googleEventUrl;

  public String toString() {
    return new Gson().toJson(this);
  }
}

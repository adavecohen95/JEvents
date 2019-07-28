package calendar.models;

import calendar.models.facebookEvents.FacebookPlace;
import com.google.api.client.util.DateTime;
import com.google.gson.Gson;

import java.util.Date;

public class CalendarEvent {

  /* shiny new fields */
  public Long facebookEventId;
  public Date startTime;
  public Date endTime;
  public String description;
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

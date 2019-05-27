package services;

import com.google.api.client.util.DateTime;

class AbstractCalendarEvent {
  public String facebookEventId;
  public String googleEventId;
  // Use this to detect if the event has been modified. Same event ID but
  // different event Etag implies that the event's details have been changed.
  public String googleEventEtag;

  public DateTime startTime;
  public DateTime endTime;

  public String description;
  public String title;
}

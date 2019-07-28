package calendar.services.facebookServices;

import calendar.models.facebookEvents.FacebookPlace;
import com.google.gson.Gson;

public class FacebookCalendarEvent {

    public String description;
    public String end_time;
    public String name;
    public FacebookPlace place;
    public String start_time;
    public long id;
    public String rsvp_status;

    public String toString() {
        return new Gson().toJson(this);
    }

}

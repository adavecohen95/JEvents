package calendar.models;

import calendar.models.facebookEvents.FacebookErrorResponse;
import calendar.models.general.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;


public class FacebookResposne extends Response {

  public FacebookErrorResponse error;
  public List<CalendarEvent> data;

  @Override
  public String toString() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(this);
  }
}

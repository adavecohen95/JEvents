package calendar.models.facebookEvents;

import calendar.models.general.Response;
import com.google.gson.Gson;


public class FacebookErrorResponse extends Response {
  public String message;
  public String type;
  public int code;
  public String fbtrace_id;

  @Override
  public String toString(){
    return new Gson().toJson(this);
  }
}

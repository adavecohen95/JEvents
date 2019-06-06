package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import models.facebookEvents.FacebookEvent;


public class FacebookResposne {

  public Error error;
  public List<FacebookEvent> data;

  public class Error {
    String message;
    String type;
    int code;
    String fbtrace_id;
  }

  @Override
  public String toString() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(this);
  }
}

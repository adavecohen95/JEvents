package calendar.common;

import calendar.models.CalendarEvent;
import calendar.models.FacebookResposne;
import calendar.models.facebookEvents.FacebookLocation;
import calendar.models.facebookEvents.FacebookPlace;
import calendar.models.facebookEvents.FacebookCalendarEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneralHelper {

  // Example date: 2019-10-15T23:00:00+0200
  private static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

  public static boolean withinRange(int value, int start, int end) {
    return (value - start) * (value - end) >= 0;
  }

  public static List<CalendarEvent> convertToCalendarEvents(FacebookResposne facebookResposne) {
    List<CalendarEvent> calendarEvents = new ArrayList<>();
    facebookResposne.data.forEach( (facebookEvent) -> {
      try {
        calendarEvents.add(convertToCalendarEvent(facebookEvent));
      } catch (ParseException e) {
        e.printStackTrace();
        return;
      }
    });

    return calendarEvents;
  }


  public static CalendarEvent convertToCalendarEvent(FacebookCalendarEvent facebookCalendarEvent) throws ParseException {

    CalendarEvent calendarEvent = new CalendarEvent();
    calendarEvent.description = facebookCalendarEvent.description;
    calendarEvent.title = facebookCalendarEvent.name;
    calendarEvent.startTime = parseISO8601ToDate(facebookCalendarEvent.start_time);
    calendarEvent.endTime = parseISO8601ToDate(facebookCalendarEvent.end_time);
    calendarEvent.facebookEventId = facebookCalendarEvent.id;
    calendarEvent.location = convertFacebookPlaceToString(facebookCalendarEvent.place);

    return calendarEvent;
  }

  private static Date parseISO8601ToDate(String input) throws ParseException {
    DateFormat df1 = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
    return  df1.parse(input);
  }

  public static String convertFacebookPlaceToString(FacebookPlace facebookPlace) {

    StringBuilder location = new StringBuilder();

    if (facebookPlace == null) {
      return "";
    }

    location.append(blankString(facebookPlace.name));

    FacebookLocation facebookLocation = facebookPlace.location;

    if (facebookLocation != null) {

      location.append(blankString(facebookLocation.street) + blankString(facebookLocation.city) +
        blankString(facebookLocation.state) + blankString(facebookLocation.zip) +
        blankString(facebookLocation.country));
    }

    return location.toString();
  }
  public static String blankString(String value) {
    if(value == null)
      return "";
    return value;
  }

}

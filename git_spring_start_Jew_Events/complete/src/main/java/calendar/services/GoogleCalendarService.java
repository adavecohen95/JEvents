package calendar.services;

import calendar.models.CalendarEvent;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.core.io.ClassPathResource;


class GoogleCalendarSync {
  public GoogleCalendarSync(Calendar cal, String calendarId) {
    _calendar = cal;
    _calendarId = calendarId;
    _facebookIdMap = new HashMap<String, CalendarEvent>();
  }

  public List<CalendarEvent> ListEvents() {
    return new ArrayList<CalendarEvent>(_facebookIdMap.values());
  }

  // Events with facebook information get added to the GoogleCalendarSync, which
  public void AddEventsFromFacebook(List<CalendarEvent> events) throws IOException {
    for (CalendarEvent e : events) {
      System.out.println(e);
      if (_facebookIdMap.containsKey(e.facebookEventId)) {
        System.out.println("Found event " + e + " in facebookIdMap");
        // If the event already exists, update details and then continue.
        CalendarEvent existingEvent = _facebookIdMap.get(e.facebookEventId);
        if (!CompareEventDetails(existingEvent, e)) {
          _facebookIdMap.put(e.facebookEventId, e);
          UpdateGoogleEvent(existingEvent);
        }
        continue;
      }

      // Create a new Google Calendar event.
      CreateGoogleEvent(e);
      _facebookIdMap.put(e.facebookEventId, e);
    }
  }

  private void UpdateGoogleEvent(CalendarEvent event) throws IOException {
    // Find the google event corresponding to this event and replace it with the
    // new event details.
    DateTime now = new DateTime(System.currentTimeMillis());
    Events events =
        _calendar
            .events()
            .list(_calendarId)
            .setMaxResults(10000)
            .setTimeMin(now)
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute();
    List<Event> items = events.getItems();
    if (items.isEmpty()) {
      System.out.println("No upcoming events found. Didn't update anything");
      return;
    }
    System.out.println("Upcoming events");
    for (Event e : items) {
      if (e.getId().compareTo(event.googleEventId) == 0) {
        EventDateTime startTime = new EventDateTime();
        startTime.setDateTime(event.startTime);
        EventDateTime endTime = new EventDateTime();
        endTime.setDateTime(event.endTime);
        // Update event.
        e.setSummary(event.title);
        e.setDescription(event.description);
        e.setStart(startTime);
        e.setEnd(endTime);
        System.out.println("Creating new event! Pre-creation ID: " + e.getId());
        _calendar.events().patch(_calendarId, event.googleEventId, e).execute();
        event.googleEventId = e.getId();
        System.out.println("New event ID: " + e.getId());
        event.googleEventEtag = e.getEtag();
        event.googleEventUrl = e.getHtmlLink();
        return;
      }
    }
  }

  private void CreateGoogleEvent(CalendarEvent event) throws IOException {
    System.out.println("Creating new event: " + event + ", title: " + event.title);
    Event e = new Event();
    e.setSummary(event.title);
    e.setDescription(event.description);
    EventDateTime startTime = new EventDateTime();
    startTime.setDateTime(event.startTime);
    e.setStart(startTime);
    EventDateTime endTime = new EventDateTime();
    endTime.setDateTime(event.endTime);
    e.setEnd(endTime);
    _calendar.events().insert(_calendarId, e).execute();
  }

  // Compares whether two elements have the same (startTime, endTime,
  // description, title).
  private Boolean CompareEventDetails(CalendarEvent e1, CalendarEvent e2) {
    return (e1.startTime.hashCode() == e2.startTime.hashCode())
        && (e1.endTime.hashCode() == e2.endTime.hashCode())
        && e1.description.compareTo(e2.description) == 0
        && e1.title.compareTo(e2.title) == 0;
  }

  // facebook id -> event.
  private HashMap<String, CalendarEvent> _facebookIdMap;
  private Calendar _calendar;
  private String _calendarId;
}

public class GoogleCalendarService {
  private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  private GoogleCalendarSync calendarSynchronizer_;
  private GoogleAuthorizationCodeFlow flow_;

  public GoogleCalendarService() throws GeneralSecurityException, IOException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    flow_ = getFlow(HTTP_TRANSPORT);
    calendarSynchronizer_ = null;
  }

  public void AddEventsFromFacebook(List<CalendarEvent> events)
      throws IOException, GeneralSecurityException {
    if (!isSetup()) {
      throw new GeneralSecurityException(
          "Need to authorize w/ user first before running AddEventsFromFacebook()!");
    }
    calendarSynchronizer_.AddEventsFromFacebook(events);
  }

  public List<CalendarEvent> ListEvents() throws GeneralSecurityException, IOException {
    if (!isSetup()) {
      throw new GeneralSecurityException(
          "Need to authorize w/ user first before running AddEventsFromFacebook()!");
    }
    return calendarSynchronizer_.ListEvents();
  }

  /**
   * Global instance of the scopes required by this quickstart. If modifying these scopes, delete
   * your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

  private static final String CREDENTIALS_FILE_PATH = "resources/credentials.json";

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static GoogleAuthorizationCodeFlow getFlow(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    // Load client secrets.
    // InputStream in = GoogleCalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    InputStream in = new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream();
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    return new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline")
        .build();
  }

  public Boolean isAuthorized() throws IOException {
    return flow_.loadCredential("user") != null;
  }

  public String getAuthorizationUrl() {
    return flow_
        .newAuthorizationUrl()
        .setScopes(SCOPES)
        .setRedirectUri("http://localhost:8080/sync")
        .build();
  }

  public Boolean isSetup() {
    return calendarSynchronizer_ != null;
  }

  public void handleAuthCode(String code) {
    try {
      flow_.createAndStoreCredential(flow_.newTokenRequest(code).setRedirectUri("http://localhost:8080/sync").execute(), "user");
    } catch (IOException e) {
      System.out.println("Unable to create credential from received authorization code: " + e);
      return;
    }
  }

  public void PostAuthorizationSetup() throws IOException, GeneralSecurityException {
    if (!isAuthorized()) {
      throw new GeneralSecurityException("Need to authorize w/ user first before running setup()!");
    }
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    Calendar service =
        new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, flow_.loadCredential("user"))
            .setApplicationName(APPLICATION_NAME)
            .build();
    calendarSynchronizer_ =
        new GoogleCalendarSync(service, "6rmr17ucm6jeo35in8k90n5kj8@group.calendar.google.com");
  }
}

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
import java.util.*;
import org.mortbay.log.Log;
import org.springframework.core.io.ClassPathResource;

class GoogleCalendarSync {
  public GoogleCalendarSync(Calendar cal, String calendarId) {
    _calendar = cal;
    _calendarId = calendarId;
    _facebookIdMap = new HashMap<>();
  }

  public List<CalendarEvent> ListEvents() throws IOException {
    List<Event> events = LoadCalendarEvents();
    System.out.println("Events fetched! Converting to CalendarEvent type...");
    ArrayList<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
    for (Event e : events) {
      CalendarEvent event = new CalendarEvent();
      event.title = e.getSummary();
      event.description = e.getDescription();
      calendarEvents.add(event);
    }
    return calendarEvents;
  }

  public List<Event> LoadCalendarEvents() throws IOException {
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
    List<Event> results = events.getItems();
    _eventRecoveryMap = new HashMap<String, Event>();
    for (Event e : results) {
      _eventRecoveryMap.put(e.getSummary() + e.getDescription(), e);
    }
    return events.getItems();
  }

  private List<Event> eventCache_;

  // Events with facebook information get added to the GoogleCalendarSync, which
  public void AddEventsFromFacebook(List<CalendarEvent> events) throws IOException {
    Log.info("AddEventsFromFacebook(# events: " + events.size() + ")");
    eventCache_ = LoadCalendarEvents();
    for (CalendarEvent e : events) {
      if (_eventRecoveryMap.containsKey(e.title + e.description)) {
        Event rawEvent = _eventRecoveryMap.get(e.title + e.description);
        e.googleEventId = rawEvent.getId();
        e.googleEventEtag = rawEvent.getEtag();
        e.googleEventUrl = rawEvent.getHtmlLink();
        _facebookIdMap.put(e.facebookEventId, e);
      }
      if (_facebookIdMap.containsKey(e.facebookEventId)) {
        // If the event already exists, update details and then continue.
        CalendarEvent existingEvent = _facebookIdMap.get(e.facebookEventId);
        if (!CompareEventDetails(existingEvent, e)) {
          existingEvent.description = e.description;
          existingEvent.title = e.title;
          existingEvent.startTime = e.startTime;
          existingEvent.endTime = e.endTime;
          UpdateGoogleEvent(existingEvent);
        }
        continue;
      }

      // Create a new Google Calendar event.
      CreateGoogleEvent(e);
      _facebookIdMap.put(e.facebookEventId, e);
    }
  }

  // First compares by Etag. Then tries ID and finally the HTML link if the
  // former methods don't work. Returns false if no method works.
  private boolean compareEvents(Event a, CalendarEvent b) {
    if ((a.getEtag() != null) && (b.googleEventEtag != null)) {
      return a.getEtag().compareTo(b.googleEventEtag) == 0;
    }
    if ((a.getId() != null) && (b.googleEventId != null)) {
      return a.getId().compareTo(b.googleEventId) == 0;
    }
    if ((a.getHtmlLink() != null) && (b.googleEventUrl != null)) {
      return a.getHtmlLink().compareTo(b.googleEventUrl) == 0;
    }
    return false;
  }

  private void UpdateGoogleEvent(CalendarEvent event) throws IOException {
    // Find the google event corresponding to this event and replace it with the
    // new event details.
    if ((eventCache_ == null) || eventCache_.isEmpty()) {
      eventCache_ = LoadCalendarEvents();
    }

    if (eventCache_.isEmpty()) {
      System.out.println("No upcoming events found. Didn't update anything");
      return;
    }

    System.out.println("Upcoming events");
    for (Event e : eventCache_) {
      if (compareEvents(e, event)) {
        EventDateTime startTime = new EventDateTime();
        startTime.setDateTime(new DateTime(event.startTime));
        EventDateTime endTime = new EventDateTime();
        endTime.setDateTime(new DateTime(event.endTime));
        // Update event.
        e.setSummary(event.title);
        e.setDescription(event.description);
        e.setStart(startTime);
        e.setEnd(endTime);
        e.setLocation(event.location);
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

  public void CreateGoogleEvent(CalendarEvent event) throws IOException {
    if ((event.endTime == null) || (event.startTime == null)) {
      Log.warn("Unable to create new event due to null start or end time. Event name: " + event.title);
      return;
    }
    Log.info("Creating new event: " + event.title);
    Event e = new Event();
    e.setSummary(event.title);
    e.setDescription(event.description);
    e.setLocation(event.location);
    EventDateTime startTime = new EventDateTime();
    startTime.setDateTime(new DateTime(event.startTime));
    e.setStart(startTime);
    EventDateTime endTime = new EventDateTime();
    endTime.setDateTime(new DateTime(event.endTime));
    e.setEnd(endTime);
    _calendar.events().insert(_calendarId, e).execute();
    event.googleEventId = e.getId();
    event.googleEventEtag = e.getEtag();
    event.googleEventUrl = e.getHtmlLink();
  }

  // Compares whether two elements have the same (startTime, endTime,
  // description, title).
  private Boolean CompareEventDetails(CalendarEvent e1, CalendarEvent e2) {
    return (e1.startTime.hashCode() == e2.startTime.hashCode())
        && (e1.endTime.hashCode() == e2.endTime.hashCode())
        && e1.description.compareTo(e2.description) == 0
        && e1.title.compareTo(e2.title) == 0
        && e1.location.equals(e2.location);
  }

  // facebook id -> event.
  private HashMap<Long, CalendarEvent> _facebookIdMap;
  // "title" + "description" used to detect pre-existing events to prevent
  // duplicates.
  private HashMap<String, Event> _eventRecoveryMap;
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
      PostAuthorizationSetup();
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

  private static final String CREDENTIALS_FILE_PATH = "credentials.json";

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
      flow_.createAndStoreCredential(
          flow_.newTokenRequest(code).setRedirectUri("http://localhost:8080/sync").execute(),
          "user");
    } catch (IOException e) {
      System.out.println("Unable to create credential from received authorization code: " + e);
      return;
    }
  }

  public void CreateGoogleEvent(CalendarEvent event) throws IOException, GeneralSecurityException {
    if (!isAuthorized()) {
      throw new GeneralSecurityException("Need to authorize w/ user first before running add()");
    }
    calendarSynchronizer_.CreateGoogleEvent(event);
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

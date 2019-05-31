package models.facebookEvents;

public class FacebookEvent {

    public String description;
    public String end_time;
    public String name;
    public FacebookPlace place;
    public String start_time;
    public long id;
    public String rsvp_status;

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder()
        .append("\n\n description: " + description)
        .append("\n\n end_time: "    + end_time)
        .append("\n\n name: "        + name)
        .append("\n\n place: "       + place)
        .append("\n\n start_time: "  + start_time)
        .append("\n\n id: "          + id)
        .append("\n\n rsvp_status: " + rsvp_status);

        return toString.toString();
    }
}

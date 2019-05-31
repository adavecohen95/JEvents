package models.facebookEvents;

public class FacebookPlace {

    public String name;
    public long id;
    public FacebookLocation location;

    @Override
    public String toString() {

        StringBuilder toString = new StringBuilder()
            .append("\n\n name: " + name)
            .append("\n\n id: " + id)
            .append("\n\n location:  " + location);

        return toString.toString();
    }
}

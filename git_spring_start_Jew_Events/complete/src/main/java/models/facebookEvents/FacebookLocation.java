package models.facebookEvents;

public class FacebookLocation {

    public String city;
    public String country;
    public double latitude;
    public double longitude;
    public String state;
    public String street;
    public String zip;



    @Override
    public String toString() {

        StringBuilder toString = new StringBuilder()
                .append("\n\n city: " + city)
                .append("\n\n country: " + country)
                .append("\n\n latitude:  " + latitude)
                .append("\n\n longitude: " + longitude)
                .append("\n\n state: " + state)
                .append("\n\n street: " + street)
                .append("\n\n zip: " + zip);

        return toString.toString();
    }

}

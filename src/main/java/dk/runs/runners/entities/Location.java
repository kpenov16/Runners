package dk.runs.runners.entities;

public class Location {
    private String id = "none";
    private int SRID = 0;
    private double x = 0;
    private double y = 0;
    private String city = "none";
    private String country = "none";
    private String streetName = "none";
    private String streetNumber = "none";
    private String title = "none";

    public Location(){}

    public Location(String id, int SRID, double x, double y, String city, String country, String streetName, String streetNumber) {
        this.id = id;
        this.SRID = SRID;
        this.x = x;
        this.y = y;
        this.city = city;
        this.country = country;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
    }

    public Location(String id) {
        this();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSRID() {
        return SRID;
    }

    public void setSRID(int sRID) {
        this.SRID = sRID;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString(){
        return String.format("Location: streetName: %s, streetNumber: %s, city: %s, country %s, x: %.6f, y: %.6f, SRID: %d, title: %s",
                                        streetName,     streetNumber,     city,     country,    x,       y,       SRID, title);
    }
}

package dk.runs.runners.entities;

public class Location {
    private String id;
    private int SRID = 0;
    private double x = 0;
    private double y = 0;
    private String city = "";
    private String country = "";
    private String streetName = "";
    private String streetNumber = "";
    public Location(String id) {
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

    @Override
    public String toString(){
        return String.format("Location: streetName: %s, streetNumber: %s, city: %s, country %s, x: %.6f, y: %.6f, SRID: %d",
                                        streetName,     streetNumber,     city,     country,    x,       y,       SRID);
    }
}

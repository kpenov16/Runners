package dk.runs.runners.entities;

public class Run {
    private final int id;
    private String location;

    public Run(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

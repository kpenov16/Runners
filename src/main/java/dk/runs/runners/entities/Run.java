package dk.runs.runners.entities;

public class Run {
    private final int id;
    private String location;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Run(int id) {
        this.id = id;
    }
    public Run(int id, String location) {
        this.id = id;
        this.location = location;
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

    @Override
    public String toString(){
        return String.format("id: %d, location: %s", id, location);
    }
}

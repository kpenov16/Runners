package dk.runs.runners.entities;

public class Run {
    private final int id;
    private String location;
    private String description;

    public int getAttendancies() {
        return attendancies;
    }

    public void setAttendancies(int attendancies) {
        this.attendancies = attendancies;
    }

    private int attendancies;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

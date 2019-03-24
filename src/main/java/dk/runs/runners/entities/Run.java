package dk.runs.runners.entities;

import java.util.Date;
import java.util.UUID;

public class Run {
    private String id = "";
    private String title = "";
    private String location = "";
    private Date date = null;
    private int distance = 0;
    private long duration = 0;
    private String description = "";
    private String status = "active";

    public Run(){}

    public Run(String id){
        setId(id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return String.format("id: %s, title: %s, location: %s, date: %s, distance: %d, duration: %s, description: %s, status: %s",
                                id, title, location, String.valueOf(date.getTime()), distance, String.valueOf(duration), description, status );
    }
}

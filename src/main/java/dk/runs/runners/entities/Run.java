package dk.runs.runners.entities;

import java.util.Date;

public class Run {
    private long id;
    private String title;
    private String location;
    private Date date;
    private int distance;
    private long duration;
    private String description;
    private String status = "active";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        return String.format("id: %d, title: %s, location: %s, date: %s, distance: %d, duration: %s, description: %s, status: %s",
                                id, title, location, String.valueOf(date.getTime()), distance, duration.toString(),description, status );
    }
}

package dk.runs.runners.entities;

import java.util.*;

public class Route implements Locational {
    private String id = "";
    private String title = "";
    private Location location;
    private Date date = null;
    private int distance = 0;
    private long duration = 0;
    private String description = "";
    private String status = "active";
    private List<WayPoint> wayPoints = new LinkedList<>();
    private int maxParticipants = -1;
    private int minParticipants = -1;

    public Route(){}

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    public Route(String id){
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

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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
    public String toString() {
        return "id: " +id+
                ", title: "+title+
                ", location: "+location+
                ", date: "+date+
                ", distance: "+distance+
                ", duration: "+duration+
                ", description: "+description+
                ", status: "+status+
                ", maxParticipants"+maxParticipants+
                ", minParticipants"+minParticipants+
                ", " +wayPoints.toString();
    }

    public void setWayPoints(List<WayPoint> wayPoints) {
        this.wayPoints = wayPoints;
    }

    public List<WayPoint> getWayPoints() {
        return wayPoints;
    }
}

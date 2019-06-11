package dk.runs.runners.entities;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Run {
    private String id;
    private Route route;
    private List<Checkpoint> checkpoints = new LinkedList<>();
    private Date startTime;
    private Date endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return  "Run " +
                "id: " + id + ", " +
                "route: " + route + ", " +
                "checkpoints: " + checkpoints + ", " +
                "startDate: " + startTime + ", " +
                "endDate: " + endTime;
    }
}

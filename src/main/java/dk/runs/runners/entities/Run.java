package dk.runs.runners.entities;

import java.util.LinkedList;
import java.util.List;

public class Run {
    private String id;
    private Route route;
    private List<Checkpoint> checkpoints = new LinkedList<>();

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

    @Override
    public String toString() {
        return  "Run " +
                "id: " + id + ", " +
                "route: " + route + ", " +
                "checkpoints: " + checkpoints;
    }
}

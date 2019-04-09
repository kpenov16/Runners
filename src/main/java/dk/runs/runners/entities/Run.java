package dk.runs.runners.entities;

public class Run {
    private String id;
    private Route route;

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

    @Override
    public String toString() {
        return String.format("Run id: %s, route: %s", id, route.toString());
    }
}

package dk.runs.runners.resources;

import dk.runs.runners.entities.Route;

public class RouteRequest {
    private Route route;
    private String creatorId;

    public RouteRequest(){}
    public Route getRoute() {
        return route;
    }
    public void setRoute(Route route) {
        this.route = route;
    }
    public String getCreatorId() {
        return creatorId;
    }
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}

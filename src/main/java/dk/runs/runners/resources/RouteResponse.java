package dk.runs.runners.resources;

import dk.runs.runners.entities.Route;

public class RouteResponse {
    private String error;
    private Route route;
    private String creatorId;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

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

    public RouteResponse(){}
}

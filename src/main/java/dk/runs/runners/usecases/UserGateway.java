package dk.runs.runners.usecases;

import dk.runs.runners.entities.Route;
import dk.runs.runners.entities.User;
import dk.runs.runners.entities.WayPoint;

import java.util.List;

public interface UserGateway {
    //try gateway interface

    void createUser(User user);
    void addRoute(Route route, String userId);
    void addWayPoint(WayPoint wayPoint, String routeId);

    void addRun(String run, String routeId, String userId);
    List<String> getRunsByUserId(String userId);
    List<String> getRunsByRouteId(String routeId);

    void addCheckpoint(String checkpoint, String runId);
    List<String> getCheckpoints(String runId);
}

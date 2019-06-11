package dk.runs.runners.services.interfaceServices;

import dk.runs.runners.entities.Route;

import java.util.List;

public interface RouteService {
    Route getRoute(String id) throws RouteServiceException;
    List<Route> getRoutesList();
    Route createRoute(Route route, String creatorId) throws RouteServiceException;
    void deleteRoute(String id) throws RouteServiceException;
    List<Route> getUserRoutesList(String creatorId) throws RouteServiceException;
    Route updateRoute(Route route) throws RouteServiceException;

    class RouteServiceException extends RuntimeException{
        public RouteServiceException(String msg){
            super(msg);
        }
    }

}

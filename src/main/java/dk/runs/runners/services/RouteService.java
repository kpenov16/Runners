package dk.runs.runners.services;

import dk.runs.runners.entities.Route;

import java.util.List;

public interface RouteService {
    Route getRoute(String id);
    List<Route> getRoutesList();
    void createRoute(Route route, String creatorId) throws RouteServiceException;
    void deleteRoute(String id) throws RouteServiceException;
    List<Route> getUserRoutesList(String creatorId) throws RouteServiceException;

    class RouteServiceException extends RuntimeException{
        public RouteServiceException(String msg){
            super(msg);
        }
    }

}

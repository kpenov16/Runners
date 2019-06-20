package dk.runs.runners.services.interfaceServices;

import dk.runs.runners.entities.Route;
import dk.runs.runners.entities.User;

import java.util.List;

public interface RouteService {

    /**
     * Retrieves a desired route by id
     * @param id is the id of the desired route
     * @return a route object with id matching the search query
     * @throws RouteServiceException Something went wrong
     */
    Route getRoute(String id) throws RouteServiceException;

    /**
     *Retrieves a list of routes
     * @return a list of route objects
     * @throws RouteServiceException Something went wrong
     */
    List<Route> getRoutesList() throws RouteServiceException;

    /**
     * Creates a new route in the data layer
     * @param route is a route object, with id, title, location object,
     *              date, distance, duration, description, status, waypoints,
     *              max participants and minimum participants
     * @param creatorId is the id of the user who is creating the route
     * @throws RouteServiceException Something went wrong
     */
    Route createRoute(Route route, String creatorId) throws RouteServiceException;

    /**
     * Deletes an existing route in the data layer
     * @param id is the id of the route to be deleted
     * @throws RouteServiceException Something went wrong
     */
    void deleteRoute(String id) throws RouteServiceException;

    /**
     *Retrieves a list of routes made by a certain user
     * @param creatorId is the id of the user authoring the routes
     * @return a list of route objects by searched user
     * @throws RouteServiceException Something went wrong
     */
    List<Route> getUserRoutesList(String creatorId) throws RouteServiceException;

    /**
     * Updates an existing route in the data layer
     * @param route is a route object, with id, title, location object,
     *                    date, distance, duration, description, status, waypoints,
     *                    max participants and minimum participants
     * @throws RouteServiceException Something went wrong
     */
    Route updateRoute(Route route) throws RouteServiceException;

    /**
     * Retrieves a list of route participants
     * @param routeId is the id of the route
     * @return list of participants
     * @throws RouteServiceException Something went wrong
     */
    List<User> getRouteParticipants(String routeId);

    class RouteServiceException extends RuntimeException{
        public RouteServiceException(String msg){
            super(msg);
        }
    }

}

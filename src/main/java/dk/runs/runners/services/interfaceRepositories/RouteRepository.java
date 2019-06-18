package dk.runs.runners.services.interfaceRepositories;

import dk.runs.runners.entities.Route;
import dk.runs.runners.entities.User;

import java.util.Date;
import java.util.List;

public interface RouteRepository {

    /**
     * Creates a new route in the data layer
     * @param route is a route object, with id, title, location object,
     *              date, distance, duration, description, status, waypoints,
     *              max participants and minimum participants
     * @param creatorId is the id of the user who is creating the route
     * @throws CreateRouteException other not predefined exceptions are wrapped in this exception
     * @throws RouteIdDuplicationException if there already is a route with the same id in the data layer
     */
    void createRoute(Route route, String creatorId) throws CreateRouteException, RouteIdDuplicationException;

    /**
     * Retrieves a desired route by id
     * @param routeId is the id of the desired route
     * @return a route object with id matching the search query
     * @throws RouteNotFoundException if there is no route with matching id existing in the data layer
     */
    Route getRoute(String routeId) throws RouteNotFoundException;

    /**
     *Retrieves a list of routes made by a certain user
     * @param creatorId is the id of the user authoring the routes
     * @return a list of route objects by searched user
     * @throws RouteNotFoundException if there are no routes with matching creator-id existing in the data layer
     */
    List<Route> getRoutes(String creatorId) throws RouteNotFoundException;

    /**
     * Updates an existing route in the data layer
     * @param updatedRoute is a route object, with id, title, location object,
     *                    date, distance, duration, description, status, waypoints,
     *                    max participants and minimum participants
     * @throws RouteIdDuplicationException if there already is a route with the same id in the data layer
     * @throws UpdateRouteException other not predefined exceptions are wrapped in this exception
     */
    void updateRoute(Route updatedRoute) throws RouteIdDuplicationException, UpdateRouteException;

    /**
     *Retrieves a list of a certain amount of routes by date
     * @param count is the amount of routes retrieved
     * @param since retrieves routes after the given date
     * @return a list of route objects
     * @throws GetRoutesException if the routes could not be retrieved
     */
    List<Route> getRouteList(int count, Date since) throws GetRoutesException;

    /**
     * Deletes an existing route in the data layer
     * @param routeId is the id of the route to be deleted
     * @throws DeleteRouteException if any error occurred while deleting the route
     */
    void deleteRoute(String routeId)throws DeleteRouteException;

    /**
     * Retrieves a list of the most popular routes (has most participants)
     * @param top is the amount of most popular routes desired
     * @param since retrieves routes after the given date
     * @return a list of sorted route objects by number of participants in decreasing order
     */
    List<Route> getMostPopular(int top, Date since);

    /**
     * Retrieves a list of route participants
     * @param routeId is the id of the route
     * @return list of participants
     * @throws GetParticipantsException other not predefined exceptions are wrapped in this exception
     */
    List<User> getRouteParticipants(String routeId) throws RouteNotFoundException;

    class UpdateRouteException extends RuntimeException{
        public UpdateRouteException(String msg){
            super(msg);
        }
    }
    class RouteNotFoundException extends RuntimeException{
        public RouteNotFoundException(String msg){
            super(msg);
        }
    }
    class CreateRouteException extends RuntimeException{
        public CreateRouteException(String msg){
            super(msg);
        }
    }
    class DeleteRouteException extends RuntimeException{
        public DeleteRouteException(String msg){
            super(msg);
        }
    }
    class GetRoutesException extends RuntimeException{
        public GetRoutesException(String msg) {super(msg);}
    }
    class RouteIdDuplicationException extends RuntimeException{
        public RouteIdDuplicationException(String msg) {super(msg);}
    }
    class RouteMissingLocationException extends RuntimeException{
        public RouteMissingLocationException(String msg) {super(msg);}
    }
    class GetParticipantsException extends RuntimeException{
        public GetParticipantsException(String msg) {super(msg);}
    }
}

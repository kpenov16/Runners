package dk.runs.runners.usecases;

import dk.runs.runners.entities.Route;

import java.util.Date;
import java.util.List;

public interface RouteRepository {

    void createRoute(Route route, String creatorId) throws CreateRouteException, RouteIdDuplicationException;

    Route getRoute(String routeId) throws RouteNotFoundException;

    List<Route> getRoutes(String creatorId) throws RouteNotFoundException;

    void updateRoute(Route updatedRoute) throws RouteIdDuplicationException, UpdateRouteException;

    List<Route> getRouteList(int numberOfRoutes, Date sinceDate) throws GetRoutesException;

    void deleteRoute(String routeId)throws DeleteRouteException;

    List<Route> getMostPopular(int top, Date since);

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
}
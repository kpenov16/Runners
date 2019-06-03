package dk.runs.runners.services;

import dk.runs.runners.entities.Route;
import dk.runs.runners.usecases.RouteRepository;

import java.util.Calendar;
import java.util.List;

public class RouteServiceImpl implements RouteService {

    private RouteRepository routeRepository;

    public RouteServiceImpl(RouteRepository routeRepository){
        this.routeRepository = routeRepository;
    }

    @Override
    public Route getRoute(String id) {
        try{
            return routeRepository.getRoute(id);
        } catch (RouteRepository.GetRoutesException e){
            throw new RouteService.RouteServiceException("Error retrieving the route");
        }
    }

    @Override
    public List<Route> getRoutesList() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        try {
            return routeRepository.getRouteList(1, calendar.getTime());
        }catch (RouteRepository.GetRoutesException e){
            throw new RouteServiceException("Could not receive routes from database");
        }
    }

    @Override
    public void createRoute(Route route, String creatorId) {
        try {
            routeRepository.createRoute(route, creatorId);
        }catch (RouteRepository.RouteIdDuplicationException e){
            if(e.getMessage().contains("PRIMARY")){
                throw new RouteServiceException("Route already created!");
            } else {
                throw new RouteServiceException("Error creating a route. Try again later.");
            }
        }

    }

    @Override
    public void deleteRoute(String id) {
        try{
            routeRepository.deleteRoute(id);
        } catch(RouteRepository.DeleteRouteException e){
            throw new RouteService.RouteServiceException("Error deleting the route");
        }
    }

    @Override
    public List<Route> getUserRoutesList(String creatorId) {
        try{
            return routeRepository.getRoutes(creatorId);
        } catch(RouteRepository.GetRoutesException e){
            throw new RouteServiceException("Error retrieving your created routes.");
        }
    }

}

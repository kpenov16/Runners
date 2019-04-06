package dk.runs.runners.services;

import dk.runs.runners.entities.Route;
import dk.runs.runners.usecases.RouteRepository;

import java.util.List;

public class RouteServiceImpl implements RouteService {

    private RouteRepository routeRepository;

    public RouteServiceImpl(RouteRepository routeRepository){
        this.routeRepository = routeRepository;
    }

    @Override
    public Route getRoute(String id) {
        return routeRepository.getRoute(id);
    }

    @Override
    public List<Route> getRoutesList() {
        try {
            return routeRepository.getRouteList();
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
        routeRepository.deleteRoute(id);
    }
}

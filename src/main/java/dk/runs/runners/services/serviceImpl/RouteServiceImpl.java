package dk.runs.runners.services.serviceImpl;

import dk.runs.runners.entities.Route;
import dk.runs.runners.services.interfaceServices.RouteService;
import dk.runs.runners.services.interfaceRepositories.RouteRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        }catch (Throwable e){
            throw new RouteService.RouteServiceException("Error retrieving the route");
        }
    }

    @Override
    public List<Route> getRoutesList() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        try {
        // TODO: uncomment these    return routeRepository.getRouteList(1, calendar.getTime());
               return routeRepository.getRouteList(100, new Date(1));
        }catch (RouteRepository.GetRoutesException e){
            throw new RouteServiceException("Could not receive routes from database");
        }catch (Throwable e){
            throw new RouteServiceException("Could not receive routes from database");
        }
    }

    @Override
    public Route createRoute(Route route, String creatorId) {
        try {
            route.setId(UUID.randomUUID().toString());
            route.getLocation().setId(UUID.randomUUID().toString());
            routeRepository.createRoute(route, creatorId);
            return route;
        }catch (RouteRepository.RouteIdDuplicationException e){
            if(e.getMessage().contains("PRIMARY")){
                throw new RouteServiceException("Route already created!");
            } else {
                throw new RouteServiceException("Error creating a route. Try again later.");
            }
        } catch (Throwable e){
                throw new RouteServiceException("Error creating a route. Try again later.");
        }

    }

    @Override
    public void deleteRoute(String id) {
        try{
            routeRepository.deleteRoute(id);
        } catch(RouteRepository.DeleteRouteException e){
            throw new RouteService.RouteServiceException("Error deleting the route");
        }  catch(Throwable e){
            throw new RouteService.RouteServiceException("Error deleting the route");
        }
    }

    @Override
    public List<Route> getUserRoutesList(String creatorId) {
        try{
            return routeRepository.getRoutes(creatorId);
        } catch(RouteRepository.GetRoutesException e){
            throw new RouteServiceException("Error retrieving your created routes.");
        } catch(Throwable e){
            throw new RouteServiceException("Error retrieving your created routes.");
        }
    }

    @Override
    public Route updateRoute(Route route) throws RouteServiceException {
        try{
            routeRepository.updateRoute(route);
            return route;
        } catch (RouteRepository.UpdateRouteException e){
            throw new RouteService.RouteServiceException("Error updating route.");
        } catch (Throwable e){
            throw new RouteService.RouteServiceException("Error updating route.");
        }
    }

}

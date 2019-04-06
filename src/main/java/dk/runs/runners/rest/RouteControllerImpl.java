package dk.runs.runners.rest;

import dk.runs.runners.entities.Route;
import dk.runs.runners.resources.RouteRequest;
import dk.runs.runners.resources.RouteResponse;
import dk.runs.runners.services.RouteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RouteControllerImpl {
    //@Resource(name="routeService")
    //RouteService routeService = config.getRouteService();
    private RouteService routeService;

    public RouteControllerImpl(RouteService routeService){
        this.routeService = routeService;
    }

    @GetMapping("/routes/{id}")
    public RouteResponse getRun(@PathVariable String id){
        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setRoute(routeService.getRoute(id));

        return routeResponse;
    }

    @DeleteMapping("/routes/{id}")
    public void deleteRun(@PathVariable String id){
        routeService.deleteRoute(id);
    }

    @GetMapping("/routes")
    public List<Route> getRuns(){
        return routeService.getRoutesList();
    }

    @PostMapping(path = "/routes")//@PostMapping(path = "/createRoute")
    public RouteResponse addRun(@RequestBody RouteRequest routeRequest) {
        routeService.createRoute(routeRequest.getRoute(), routeRequest.getCreatorId());
        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setError("");
        routeResponse.setRoute(routeRequest.getRoute());
        return routeResponse;
    }

}


package dk.runs.runners.rest;

import dk.runs.runners.entities.Route;
import dk.runs.runners.resources.RouteRequest;
import dk.runs.runners.resources.RouteResponse;
import dk.runs.runners.services.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="http://localhost:4200")
@RestController
public class RouteControllerImpl {
    //@Resource(name="routeService")
    //RouteService routeService = config.getRouteService();
    private RouteService routeService;

    public RouteControllerImpl(RouteService routeService){
        this.routeService = routeService;
    }

//    @GetMapping("/routes/{id}")
//    public RouteResponse getRoute(@PathVariable String id){
//        RouteResponse routeResponse = new RouteResponse();
//        routeResponse.setRoute(routeService.getRoute(id));
//
//        return routeResponse;
//    }

    @GetMapping("/routes/{id}")
    public Route getRoute(@PathVariable String id){
        return routeService.getRoute(id);//TODO: try with ResponseEntity.ok(route) . Or RouteResponse
    }

    @DeleteMapping("/routes/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable String id){
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();

        // return ResponseEntity.notFound().build() is route is not found
    }

    @GetMapping("/routes")
    public List<Route> getRoutes(){
        return routeService.getRoutesList();
    }

    @PostMapping(path = "/routes")//@PostMapping(path = "/createRoute")
    public RouteResponse addRoute(@RequestBody RouteRequest routeRequest) {
        routeService.createRoute(routeRequest.getRoute(), routeRequest.getCreatorId());
        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setError("");
        routeResponse.setRoute(routeRequest.getRoute());
        return routeResponse;
    }

    @GetMapping("/users/{creator_id}/routes") //TODO: should be use userid in future
    public List<Route> getUserRoutesList(@PathVariable String creator_id){
        return routeService.getUserRoutesList(creator_id);
    }


}


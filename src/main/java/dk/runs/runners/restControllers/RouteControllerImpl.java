package dk.runs.runners.restControllers;

import dk.runs.runners.entities.Route;
import dk.runs.runners.services.interfaceServices.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping("users/{userId}/routes/{id}")
    public Route getRoute(@PathVariable String userId, @PathVariable String id){
        return routeService.getRoute(id);//TODO: try with ResponseEntity.ok(route) . Or RouteResponse
    }

    @DeleteMapping("/routes/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable String id){
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();

        // return ResponseEntity.notFound().build() is route is not found. Step 66
    }

    @GetMapping("/routes")
    public List<Route> getRoutes(){
        return routeService.getRoutesList();
    }

//    @PostMapping(path = "/routes")//@PostMapping(path = "/createRoute")
//    public RouteResponse addRoute(@RequestBody RouteRequest routeRequest) {
//        routeService.createRoute(routeRequest.getRoute(), routeRequest.getCreatorId());
//        RouteResponse routeResponse = new RouteResponse();
//        routeResponse.setError("");
//        routeResponse.setRoute(routeRequest.getRoute());
//        return routeResponse;
//    }

    @PostMapping(path = "/users/{creatorId}/routes")
    public ResponseEntity<Route> createRoute(@PathVariable String creatorId, @RequestBody Route route) {
        Route createdRoute = routeService.createRoute(route, creatorId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdRoute.getId()).toUri();
        return ResponseEntity.created(uri).body(createdRoute);
    }

    @GetMapping("/users/{creatorId}/routes")
    public List<Route> getUserRoutesList(@PathVariable String creatorId){
        return routeService.getUserRoutesList(creatorId);
    }

    @PutMapping("/routes/{id}")
    public ResponseEntity<Route> updateRoute(
            @PathVariable String id, @RequestBody Route route
    ){
        Route routeUpdated = routeService.updateRoute(route);
        return new ResponseEntity<>(routeUpdated, HttpStatus.OK);
    }

}


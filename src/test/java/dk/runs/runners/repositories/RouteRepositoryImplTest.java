package dk.runs.runners.repositories;

import dk.runs.runners.entities.Route;
import dk.runs.runners.usecases.RouteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouteRepositoryImplTest {
    private RouteRepository routeRepository;
    private Route route;
    private Route secondRoute;
    private final long ms = System.currentTimeMillis();
    private final long ONE_HOUR = 60*60*1_000;
    private final int DISTANCE = 5_000;
    private String creatorId = UUID.randomUUID().toString();

    @BeforeEach
    public void beforeEach(){
        routeRepository = new RouteRepositoryImpl();
        route = new Route(UUID.randomUUID().toString());
        route.setTitle("Route three");
        route.setLocation("Stockholm");
        route.setDescription("It is going to be very fun!!!");
        route.setDate(new Date(ms));
        route.setStatus("active");
        route.setDuration(ONE_HOUR);
        route.setDistance(DISTANCE);

        secondRoute = new Route(UUID.randomUUID().toString());
        secondRoute.setTitle("Slow Route");
        secondRoute.setLocation("Copenhagen");
        secondRoute.setDescription("It is going to be very fun!!!");
        secondRoute.setDate(new Date(ms));
        secondRoute.setStatus("active");
        secondRoute.setDuration(ONE_HOUR);
        secondRoute.setDistance(DISTANCE);
    }

    @AfterEach
    public void tearDown(){
        routeRepository.deleteRoute(route.getId());
        routeRepository.deleteRoute(secondRoute.getId());
    }


    @Test
     public void givenCreateRun_returnRunCreated() {
        //act
        routeRepository.createRoute(route, creatorId);

        Route returnedRoute = routeRepository.getRoute(route.getId());

        //assert
        assertEquals(route.toString(), returnedRoute.toString());

        //clean up
        RouteRepositoryImpl runRepositoryImpl = (RouteRepositoryImpl) routeRepository;
        runRepositoryImpl.deleteRoute(route.getId());
     }

    @Test
    public void givenRunUpdated_returnRunUpdated() {
        //arrange
        routeRepository.createRoute(route, creatorId);

        Route updatedRoute = route;
        updatedRoute.setTitle("new Title");
        updatedRoute.setLocation("new Location");
        updatedRoute.setDate(new Date());
        updatedRoute.setDistance(1000);
        updatedRoute.setDuration(2000);
        updatedRoute.setDescription("new Dwscription");
        updatedRoute.setStatus("new Status");

        //act
        routeRepository.updateRoute(updatedRoute);

        //assert
        Route returnedRoute = routeRepository.getRoute(route.getId());
        assertEquals(updatedRoute.toString(), returnedRoute.toString());

        //clean up
        RouteRepositoryImpl runRepositoryImpl = (RouteRepositoryImpl) routeRepository;
        runRepositoryImpl.deleteRoute(route.getId());
    }

    @Test
    public void givenRequestingNonExistingRunById_returnRunNotFoundException() {
        assertThrows(RouteRepository.RouteNotFoundException.class,
                () -> routeRepository.getRoute(UUID.randomUUID().toString())
        );
    }

    @Test
    public void givenCreateRunWithExistingId_returnRunIdDuplicationException() {
        //Arrange
        routeRepository.createRoute(route, creatorId);

        //Act, Assert
        assertThrows(RouteRepository.RouteIdDuplicationException.class,
                () -> routeRepository.createRoute(route, creatorId)
        );

    }

    @Test
    public void givenGetRunsList_returnListIsNotEmpty(){
        routeRepository.createRoute(route, creatorId);
        routeRepository.createRoute(secondRoute, creatorId);
        assertTrue(routeRepository.getRouteList().size() > 0);
        //clean up
        RouteRepositoryImpl runRepositoryImpl = (RouteRepositoryImpl) routeRepository;
        runRepositoryImpl.deleteRoute(route.getId());
        runRepositoryImpl.deleteRoute(secondRoute.getId());
    }
}

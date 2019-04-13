package dk.runs.runners.repositories;

import dk.runs.runners.entities.Route;
import dk.runs.runners.entities.User;
import dk.runs.runners.entities.WayPoint;
import dk.runs.runners.usecases.RouteRepository;
import dk.runs.runners.usecases.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouteRepositoryImplTest {
    private UserRepository userRepository;
    private RouteRepository routeRepository;
    private Route route;
    private User user;
    private Route secondRoute;
    private final long ms = System.currentTimeMillis();
    private final long ms2 = System.currentTimeMillis() + 500;

    private final long ONE_HOUR = 60*60*1_000;
    private final int DISTANCE = 5_000;

    @BeforeEach
    public void beforeEach(){
        user = new User(UUID.randomUUID().toString());
        user.setEmail("runner@runner.com");
        user.setUserName("BillGates");
        user.setPassword("bananas");

        userRepository = new UserRepositoryImpl();
        routeRepository = new RouteRepositoryImpl();
        route = new Route(UUID.randomUUID().toString());
        route.setTitle("Route three");
        route.setLocation("Stockholm");
        route.setDescription("It is going to be very fun!!!");
        route.setDate(new Date(ms));
        route.setStatus("active");
        route.setDuration(ONE_HOUR);
        route.setDistance(DISTANCE);
        route.setMaxParticipants(5);
        route.setMinParticipants(2);
        List<WayPoint> wayPoints = new LinkedList<>();
        WayPoint startWayPoint = new WayPoint(1.12, 1.13, 0);
        WayPoint endWayPoint = new WayPoint(5.12, 5.13, 1);
        wayPoints.add(startWayPoint);
        wayPoints.add(endWayPoint);
        route.setWayPoints(wayPoints);

        secondRoute = new Route(UUID.randomUUID().toString());
        secondRoute.setTitle("Slow Route");
        secondRoute.setLocation("Copenhagen");
        secondRoute.setDescription("It is going to be very fun!!!");
        secondRoute.setDate(new Date(ms2));
        secondRoute.setStatus("active");
        secondRoute.setDuration(ONE_HOUR);
        secondRoute.setDistance(DISTANCE);
    }

    @AfterEach
    public void tearDown(){
        routeRepository.deleteRoute(route.getId());
        routeRepository.deleteRoute(secondRoute.getId());

        userRepository.deleteUser(user.getId());
    }

    @Test
     public void givenCreateRoute_returnRouteCreated() {
        //act
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());//route and waypoints

        Route returnedRoute = routeRepository.getRoute(route.getId());

        //assert
        assertEquals(route.toString() , returnedRoute.toString());
     }

    @Test
    public void givenUserCreatesTwoRoutes_returnRoutesCreated() {
        //act
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());//route and waypoints
        routeRepository.createRoute(secondRoute, user.getId());//route and waypoints

        List<Route> returnedRoutes = routeRepository.getRoutes(user.getId());

        List<Route> expectedRoutes = new LinkedList<Route>(){{
            add(route);
            add(secondRoute);
        }};

        Collections.sort(expectedRoutes, (r1,r2)->Long.compare(r1.getDate().getTime(),r2.getDate().getTime()));
        Collections.sort(returnedRoutes, (r1,r2)->Long.compare(r1.getDate().getTime(),r2.getDate().getTime()));

        //assert
        assertEquals( expectedRoutes.toString() , returnedRoutes.toString() );
    }

    @Test
    public void givenRouteUpdated_returnRouteUpdated() {
        //arrange
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());

        Route updatedRoute = route;
        updatedRoute.setTitle("new Title");
        updatedRoute.setLocation("new Location");
        updatedRoute.setDate(new Date());
        updatedRoute.setDistance(1000);
        updatedRoute.setDuration(2000);
        updatedRoute.setDescription("new Dwscription");
        updatedRoute.setStatus("new Status");
        updatedRoute.setMaxParticipants(10);
        updatedRoute.setMinParticipants(5);

        updatedRoute.setWayPoints(new LinkedList<WayPoint>(){{
                add(new WayPoint(0.12, 0.13, 0));
                add(new WayPoint(2.12, 2.13, 1));
                add(new WayPoint(5.12, 5.13, 2));
        }});

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
    public void givenRequestingNonExistingRouteById_returnRouteNotFoundException() {
        assertThrows(RouteRepository.RouteNotFoundException.class,
                () -> routeRepository.getRoute(UUID.randomUUID().toString())
        );
    }

    @Test
    public void givenCreateRouteWithExistingId_returnRouteIdDuplicationException() {
        //Arrange
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());

        //Act, Assert
        assertThrows(RouteRepository.RouteIdDuplicationException.class,
                () -> routeRepository.createRoute(route, user.getId())
        );

    }

    @Test
    public void givenGetRoutesList_returnListIsNotEmpty(){
        userRepository.createUser(user);

        routeRepository.createRoute(route, user.getId());
        routeRepository.createRoute(secondRoute, user.getId());
        assertTrue(routeRepository.getRouteList().size() > 0);
        //clean up
        RouteRepositoryImpl runRepositoryImpl = (RouteRepositoryImpl) routeRepository;
        runRepositoryImpl.deleteRoute(route.getId());
        runRepositoryImpl.deleteRoute(secondRoute.getId());
    }
}

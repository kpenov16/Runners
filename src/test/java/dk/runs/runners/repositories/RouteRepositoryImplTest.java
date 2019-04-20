package dk.runs.runners.repositories;

import dk.runs.runners.entities.*;
import dk.runs.runners.usecases.RouteRepository;
import dk.runs.runners.usecases.RunRepository;
import dk.runs.runners.usecases.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        userRepository = new UserRepositoryImpl();
        routeRepository = new RouteRepositoryImpl();

        runRepository = new RunRepositoryImpl();
        ((RunRepositoryImpl) runRepository).setRouteRepository(routeRepository);

        user = new User(UUID.randomUUID().toString());
        user.setEmail("runner@runner.com");
        user.setUserName("BillGates");
        user.setPassword("bananas");
        Location userLocation = new Location(UUID.randomUUID().toString());
        user.setLocation(userLocation);

        route = new Route(UUID.randomUUID().toString());
        Location location = new Location(UUID.randomUUID().toString());
        location.setX(2.2123);
        location.setY(2.3123);
        location.setCity("Stockholm");
        location.setCountry("Sweden");
        location.setStreetName("Main street");
        location.setStreetNumber("5A");
        route.setTitle("Route three");
        route.setLocation(location);
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
        Location secondRouteLocation = new Location(UUID.randomUUID().toString());
        secondRouteLocation.setX(2.2123);
        secondRouteLocation.setY(2.3123);
        secondRouteLocation.setCity("Copenhagen");
        secondRouteLocation.setCountry("Denmark");
        secondRouteLocation.setStreetName("Skolegade");
        secondRouteLocation.setStreetNumber("10");
        secondRoute.setLocation(secondRouteLocation);
        secondRoute.setDescription("It is going to be very fun!!!");
        secondRoute.setDate(new Date(ms2));
        secondRoute.setStatus("active");
        secondRoute.setDuration(ONE_HOUR);
        secondRoute.setDistance(DISTANCE);
    }

    /*
    @AfterEach
    public void tearDown(){
        routeRepository.deleteRoute(route.getId());
        routeRepository.deleteRoute(secondRoute.getId());
        userRepository.deleteUser(user.getId());
    }*/


    private RunRepository runRepository = null;
    private Location location = null;
    private List<Run> runsToBeDeleted = new ArrayList<>();
    private List<User> usersToBeDeleted = new ArrayList<>();
    private List<User> participantsToBeDeleted = new ArrayList<>();
    private List<Route> routesToBeDeleted = new ArrayList<>();

    @Test
    public void givenRequestForUserHavingMostParticipantsByRoute_returnUserHavingMostParticipants() throws InterruptedException {
        //arrange
        User mostPopularUser = constructUser();
        userRepository.createUser(mostPopularUser);
        Route mostPopularRoute1 = constructRoute();
        Route mostPopularRoute2 = constructRoute();
        routeRepository.createRoute(mostPopularRoute1, mostPopularUser.getId());
        routeRepository.createRoute(mostPopularRoute2, mostPopularUser.getId());
        registerUsersForRoute(mostPopularRoute1, 9);
        registerUsersForRoute(mostPopularRoute2, 8);

        User popularUser = constructUser();
        userRepository.createUser(popularUser);
        Route popularRoute1 = constructRoute();
        Route popularRoute2 = constructRoute();
        routeRepository.createRoute(popularRoute1, popularUser.getId());
        routeRepository.createRoute(popularRoute2, popularUser.getId());
        registerUsersForRoute(popularRoute1, 7);
        registerUsersForRoute(popularRoute2, 6);

        User lessPopularUser = constructUser();
        userRepository.createUser(lessPopularUser);
        Route lessPopularRoute1 = constructRoute();
        Route lessPopularRoute2 = constructRoute();
        routeRepository.createRoute(lessPopularRoute1, lessPopularUser.getId());
        routeRepository.createRoute(lessPopularRoute2, lessPopularUser.getId());
        registerUsersForRoute(lessPopularRoute1, 4);
        registerUsersForRoute(lessPopularRoute2, 5);

        User nonPopularUser = constructUser();
        userRepository.createUser(nonPopularUser);
        Route nonPopularRoute1 = constructRoute();
        Route nonPopularRoute2 = constructRoute();
        routeRepository.createRoute(nonPopularRoute1, nonPopularUser.getId());
        routeRepository.createRoute(nonPopularRoute2, nonPopularUser.getId());
        registerUsersForRoute(nonPopularRoute1, 1);
        registerUsersForRoute(nonPopularRoute2, 2);

        Thread.sleep(500);

        //act
        int top = 3;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        System.out.println(calendar.getTime() + " " + calendar.getTime().getTime());
        List<Route> returnedRoutes = routeRepository.getMostPopular(top, calendar.getTime());

        //assert
        assertEquals(mostPopularRoute1.toString(), returnedRoutes.get(0).toString());
        assertEquals(mostPopularRoute2.toString(), returnedRoutes.get(1).toString());
        assertEquals(popularRoute1.toString(), returnedRoutes.get(2).toString());

        //clean up
        usersToBeDeleted.add(mostPopularUser);
        usersToBeDeleted.add(popularUser);
        usersToBeDeleted.add(lessPopularUser);
        usersToBeDeleted.add(nonPopularUser);

        deleteCreatedEntities();

    }

    private void deleteCreatedEntities() {
        runsToBeDeleted.stream().forEach(run->runRepository.deleteRun(run.getId()));
        participantsToBeDeleted.stream().forEach(p->userRepository.deleteUser(p.getId()));
        routesToBeDeleted.stream().forEach(route -> routeRepository.deleteRoute(route.getId()));
        usersToBeDeleted.stream().forEach(u->userRepository.deleteUser(u.getId()));
    }
    private void registerUsersForRoute(Route route, int numberOfParticipants) {
        for (int i=0; i<numberOfParticipants; i++){
            Run run = constructRun(route);
            User user = constructUser();
            userRepository.createUser(user);
            run.setRoute(route);
            runRepository.createRun(run, user.getId());

            participantsToBeDeleted.add(user);
        }
    }
    private Run constructRun(Route route) {
        Run run = new Run();
        run.setRoute(route);
        run.setId(UUID.randomUUID().toString());
        run.setCheckpoints(new LinkedList<Checkpoint>());
        runsToBeDeleted.add(run);
        return run;
    }
    private Route constructRoute(){
        Route route = new Route(UUID.randomUUID().toString());
        Location location = new Location(UUID.randomUUID().toString());
        location.setX(2.2123);
        location.setY(2.3123);
        location.setCity("Stockholm");
        location.setCountry("Sweden");
        location.setStreetName("Main street");
        location.setStreetNumber("5A");
        route.setTitle("Route three");
        route.setLocation(location);
        route.setDescription("It is going to be very fun!!!");
        route.setDate(new Date( System.currentTimeMillis() ));
        route.setStatus("active");
        route.setDuration(60*60*1_000);
        route.setDistance(3_000);
        route.setMaxParticipants(10);
        route.setMinParticipants(2);

        List<WayPoint> wayPoints = new LinkedList<>();
        WayPoint startWayPoint = new WayPoint(1.12, 1.13, 0);
        WayPoint endWayPoint = new WayPoint(5.12, 5.13, 1);
        wayPoints.add(startWayPoint);
        wayPoints.add(endWayPoint);
        route.setWayPoints(wayPoints);

        routesToBeDeleted.add(route);

        return route;
    }
    private User constructUser(){
        User user = new User(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        user.setUserName(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID().toString());

        location = new Location(UUID.randomUUID().toString());
        location.setX(2.2123);
        location.setY(2.3123);
        location.setCity("Stockholm");
        location.setCountry("Sweden");
        location.setStreetName("Main street");
        location.setStreetNumber("5A");
        user.setLocation(location);

        return user;
    }


    @Test
     public void givenCreateRoute_returnRouteCreated() {
        //act
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());//route and waypoints

        Route returnedRoute = routeRepository.getRoute(route.getId());

        //assert
        assertEquals(route.toString() , returnedRoute.toString());

        //clean-up
        routeRepository.deleteRoute(route.getId());
        userRepository.deleteUser(user.getId());
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

        //clean-up
        routeRepository.deleteRoute(route.getId());
        routeRepository.deleteRoute(secondRoute.getId());
        userRepository.deleteUser(user.getId());
    }

    @Test
    public void givenRouteUpdated_returnRouteUpdated() {
        //arrange
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());

        Route updatedRoute = route;
        updatedRoute.setTitle("new Title");
        Location newLocation = new Location(route.getLocation().getId());
        newLocation.setX(11.2123);
        newLocation.setY(33.3123);
        newLocation.setCity("Germany");
        newLocation.setStreetName("Alabala street");
        newLocation.setStreetNumber("6B");
        newLocation.setCountry("Berlin");
        updatedRoute.setLocation(newLocation);
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
        routeRepository.deleteRoute(route.getId());
        userRepository.deleteUser(user.getId());
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

        //clean up
        routeRepository.deleteRoute(route.getId());
        userRepository.deleteUser(user.getId());
    }

    @Test
    public void givenGetRoutesList_returnListIsNotEmpty(){
        userRepository.createUser(user);

        routeRepository.createRoute(route, user.getId());
        routeRepository.createRoute(secondRoute, user.getId());
        assertTrue(routeRepository.getRouteList().size() > 0);

        //clean up
        routeRepository.deleteRoute(route.getId());
        routeRepository.deleteRoute(secondRoute.getId());
        userRepository.deleteUser(user.getId());
    }

    @Test
    public void givenRouteWithMissingLocation_returnRouteMissingLocationException(){
        String routeId = UUID.randomUUID().toString();
        Route route = new Route(routeId);
        //physical assert 1
        RouteRepository.RouteMissingLocationException routeMissingLocationException =
                assertThrows(RouteRepository.RouteMissingLocationException.class,
                () -> routeRepository.createRoute(route, UUID.randomUUID().toString())
        );
        assertEquals("Route with id: " + routeId + " is missing location.",
                routeMissingLocationException.getMessage());

        //physical assert 2
        Location location = new Location("");
        route.setLocation(location);
        routeMissingLocationException = assertThrows(RouteRepository.RouteMissingLocationException.class,
                () -> routeRepository.createRoute(route, UUID.randomUUID().toString())
        );
        assertEquals("Route with id: " + routeId + " is missing location.",
                routeMissingLocationException.getMessage());

        //physical assert 3
        location.setId(null);
        route.setLocation(location);
        routeMissingLocationException = assertThrows(RouteRepository.RouteMissingLocationException.class,
                () -> routeRepository.createRoute(route, UUID.randomUUID().toString())
        );
        assertEquals("Route with id: " + routeId + " is missing location.",
                routeMissingLocationException.getMessage());
    }
}

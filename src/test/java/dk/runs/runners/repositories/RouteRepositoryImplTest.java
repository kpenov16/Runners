package dk.runs.runners.repositories;

import dk.runs.runners.entities.*;
import dk.runs.runners.repositories.mysqlImpl.RouteRepositoryImpl;
import dk.runs.runners.repositories.mysqlImpl.RunRepositoryImpl;
import dk.runs.runners.repositories.mysqlImpl.UserRepositoryImpl;
import dk.runs.runners.services.interfaceRepositories.RouteRepository;
import dk.runs.runners.services.interfaceRepositories.RunRepository;
import dk.runs.runners.services.interfaceRepositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static junit.framework.TestCase.fail;
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

        runRepository = new RunRepositoryImpl(new RouteRepositoryImpl());
        ((RunRepositoryImpl) runRepository).setRouteRepository(routeRepository);

        user = new User(UUID.randomUUID().toString());
        user.setEmail("runner@runner.com");
        user.setUserName("BillGates");
        user.setPassword("bananas");
        Location userLocation = new Location(UUID.randomUUID().toString());
        user.setLocations(new ArrayList<Location>(){{add(userLocation);}} );

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
        route.setStatus("released");
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
        secondRoute.setStatus("released");
        secondRoute.setDuration(ONE_HOUR);
        secondRoute.setDistance(DISTANCE);
    }


//    @AfterEach
//    public void tearDown(){
//        routeRepository.deleteRoute(route.getId());
//        routeRepository.deleteRoute(secondRoute.getId());
//        userRepository.deleteUser(user.getId());
//    }


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
        int mostPopular1Number = 5;
        int mostPopular2Number = 4;
        routeRepository.createRoute(mostPopularRoute1, mostPopularUser.getId());
        routeRepository.createRoute(mostPopularRoute2, mostPopularUser.getId());
        mostPopularRoute1.setNumberOfParticipants(mostPopular1Number);
        mostPopularRoute2.setNumberOfParticipants(mostPopular2Number);
        registerUsersForRoute(mostPopularRoute1, mostPopular1Number);
        registerUsersForRoute(mostPopularRoute2, mostPopular2Number);

        User popularUser = constructUser();
        userRepository.createUser(popularUser);
        Route popularRoute1 = constructRoute();
        Route popularRoute2 = constructRoute();
        routeRepository.createRoute(popularRoute1, popularUser.getId());
        routeRepository.createRoute(popularRoute2, popularUser.getId());
        int popular1Number = 3;
        int popular2Number = 2;
        popularRoute1.setNumberOfParticipants(popular1Number);
        //popularRoute2.setNumberOfParticipants(6);
        registerUsersForRoute(popularRoute1, popular1Number);
        registerUsersForRoute(popularRoute2, popular2Number);
/*
        User lessPopularUser = constructUser();
        userRepository.createUser(lessPopularUser);
        Route lessPopularRoute1 = constructRoute();
        Route lessPopularRoute2 = constructRoute();
        routeRepository.createRoute(lessPopularRoute1, lessPopularUser.getId());
        routeRepository.createRoute(lessPopularRoute2, lessPopularUser.getId());
        registerUsersForRoute(lessPopularRoute1, 4);
        registerUsersForRoute(lessPopularRoute2, 5);
*/
        User nonPopularUser = constructUser();
        userRepository.createUser(nonPopularUser);
        Route nonPopularRoute1 = constructRoute();
        Route nonPopularRoute2 = constructRoute();
        routeRepository.createRoute(nonPopularRoute1, nonPopularUser.getId());
        routeRepository.createRoute(nonPopularRoute2, nonPopularUser.getId());
        registerUsersForRoute(nonPopularRoute1, 0);
        //System.out.println(nonPopularRoute1.getId());
        registerUsersForRoute(nonPopularRoute2, 1);

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
  //      usersToBeDeleted.add(lessPopularUser);
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
        user.setLocations( new ArrayList<Location>(){{add(location);}} );

        return user;
    }

    @Test
     public void givenCreateRoute_returnRouteCreated() {
        //act
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());//route and waypoints
        Run run = constructRun(route);
        runRepository.createRun(run, user.getId());
        route.setNumberOfParticipants(route.getNumberOfParticipants()+1);

        Route returnedRoute = routeRepository.getRoute(route.getId());
        //assert
        assertEquals(route.toString() , returnedRoute.toString());

        //clean-up
        runRepository.deleteRun(run.getId());
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
        Location newLocation = new Location(route.getLocations().get(0).getId());
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
        //Arrange
        class RouteComparator implements Comparator<Route>{
            @Override
            public int compare(Route route1, Route route2) {
                return route1.getId().compareTo(route2.getId());
            }
        }
        try {
            userRepository.createUser(user);
            routeRepository.createRoute(route, user.getId());
            routeRepository.createRoute(secondRoute, user.getId());
            List<Route> expectedRoutes = new ArrayList<>();
            expectedRoutes.add(route);
            expectedRoutes.add(secondRoute);
            Collections.sort(expectedRoutes, new RouteComparator());
            //Act
            List<Route> actualRoutes = routeRepository.getRouteList(2, new Date(1));
            Collections.sort(actualRoutes, new RouteComparator());
            assertEquals(expectedRoutes.toString(), actualRoutes.toString());
        } catch (Throwable t){
            t.printStackTrace();
            fail("givenGetRoutesList_returnListIsNotEmpty");
        } finally {
            //clean up
            routeRepository.deleteRoute(route.getId());
            routeRepository.deleteRoute(secondRoute.getId());
            userRepository.deleteUser(user.getId());
        }
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

    @Test
    public void givenRouteWithPatricipants_returnParticipants(){

        User user1 = constructUser();
        User user2 = constructUser();
        User user3 = constructUser();
        Route route = constructRoute();
        Run run1 = constructRun(route);
        Run run2 = constructRun(route);
        Run run3 = constructRun(route);
        try{
            userRepository.createUser(user1);
            userRepository.createUser(user2);
            userRepository.createUser(user3);
            routeRepository.createRoute(route, user1.getId());
            runRepository.createRun(run1, user1.getId());
            runRepository.createRun(run2, user2.getId());
            runRepository.createRun(run3, user3.getId());
            List<String> exceptedUserNames = new ArrayList<>();
            exceptedUserNames.add(user1.getUserName());
            exceptedUserNames.add(user2.getUserName());
            exceptedUserNames.add(user3.getUserName());
            Collections.sort(exceptedUserNames);
            //Act
            List<User> participants = routeRepository.getRouteParticipants(route.getId());
            List<String> participantsUserNames = new ArrayList<>();
            for(User user: participants){
                participantsUserNames.add(user.getUserName());
            }
            Collections.sort(participantsUserNames);
            assertEquals(exceptedUserNames, participantsUserNames);
        } catch (Throwable t){
            t.printStackTrace();
            fail("givenRouteWithPatricipants_returnParticipants");
        } finally {
            runRepository.deleteRun(run1.getId());
            runRepository.deleteRun(run2.getId());
            runRepository.deleteRun(run3.getId());
            routeRepository.deleteRoute(route.getId());
            userRepository.deleteUser(user1.getId());
            userRepository.deleteUser(user2.getId());
            userRepository.deleteUser(user3.getId());
        }
    }
}

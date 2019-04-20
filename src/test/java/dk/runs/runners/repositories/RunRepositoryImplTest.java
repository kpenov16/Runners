package dk.runs.runners.repositories;

import dk.runs.runners.entities.*;
import dk.runs.runners.usecases.RouteRepository;
import dk.runs.runners.usecases.RunRepository;
import dk.runs.runners.usecases.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RunRepositoryImplTest {
    private Run run;
    private Run secondRun;
    private RunRepositoryImpl runRepository;
    private UserRepository userRepository;
    private RouteRepository routeRepository;
    private Route route;
    private Route secondRoute;
    private User user;
    private final long ms = System.currentTimeMillis();
    private final long ONE_HOUR = 60*60*1_000;
    private final int DISTANCE = 5_000;

    @BeforeEach
    void beforeEach(){
        userRepository = new UserRepositoryImpl();
        user = new User(UUID.randomUUID().toString());
        user.setEmail("runner@runner.com");
        user.setUserName("BillGates");
        user.setPassword("bananas");
        Location userLocation = new Location(UUID.randomUUID().toString());
        userLocation.setX(2.2123);
        userLocation.setY(2.3123);
        userLocation.setCity("Stockholm");
        userLocation.setCountry("Sweden");
        userLocation.setStreetName("Main street");
        userLocation.setStreetNumber("5A");
        user.setLocation(userLocation);
        userRepository.createUser(user);

        routeRepository = new RouteRepositoryImpl();


        route = new Route(UUID.randomUUID().toString());
        route.setTitle("Route three");
        route.setDescription("It is going to be very fun!!!");
        route.setDate(new Date(ms));
        route.setStatus("active");
        route.setDuration(ONE_HOUR);
        route.setDistance(DISTANCE);
        route.setMaxParticipants(5);
        route.setMinParticipants(2);


        List<WayPoint> wayPoints = new LinkedList<>();
        WayPoint startWayPoint = new WayPoint(1.12, 1.13, 0);
        WayPoint middleWayPoint = new WayPoint(3.22, 4.22, 1);
        WayPoint endWayPoint = new WayPoint(5.12, 5.13, 2);
        wayPoints.add(startWayPoint);
        wayPoints.add(middleWayPoint);
        wayPoints.add(endWayPoint);
        route.setWayPoints(wayPoints);
        routeRepository.createRoute(route, user.getId());


        secondRoute = new Route(UUID.randomUUID().toString());
        secondRoute.setTitle("Route three");
        secondRoute.setDescription("It is going to be very fun!!!");
        secondRoute.setDate(new Date(ms));
        secondRoute.setStatus("active");
        secondRoute.setDuration(ONE_HOUR);
        secondRoute.setDistance(DISTANCE);
        secondRoute.setMaxParticipants(5);
        secondRoute.setMinParticipants(2);

        Location secondLocation = new Location(UUID.randomUUID().toString());
        secondLocation.setX(2.2123);
        secondLocation.setY(2.3123);
        secondLocation.setCity("Stockholm");
        secondLocation.setCountry("Sweden");
        secondLocation.setStreetName("Main street");
        secondLocation.setStreetNumber("5A");
        secondRoute.setLocation(secondLocation);

        secondRoute.setWayPoints(wayPoints);




        run = new Run();
        run.setRoute(route);
        run.setId(UUID.randomUUID().toString());
        run.setCheckpoints(new LinkedList<Checkpoint>());

        secondRun = new Run();
        secondRun.setRoute(secondRoute);
        secondRun.setId(UUID.randomUUID().toString());
        secondRun.setCheckpoints(new LinkedList<Checkpoint>());

        runRepository = new RunRepositoryImpl();
        runRepository.setRouteRepository(routeRepository);

    }

    @AfterEach
    void afterEach(){
        runRepository.deleteRun(run.getId());
        routeRepository.deleteRoute(route.getId());
        userRepository.deleteUser(user.getId());
    }

    @Test
    void givenRunWithSomeCheckedWaypoint_ReturnMissingWaypoints(){
        //Arrange
        routeRepository.createRoute(secondRoute, user.getId());

        runRepository.createRun(run, route.getId(), user.getId());
        runRepository.addCheckpointIfValid(run.getId(), 1, 1, 1);

        runRepository.createRun(secondRun, secondRoute.getId(), user.getId());
        runRepository.addCheckpointIfValid(secondRun.getId(), 1, 1, 1);
        runRepository.addCheckpointIfValid(secondRun.getId(), 3, 4, 1);
        runRepository.addCheckpointIfValid(secondRun.getId(), 5, 5, 1);

        List<WayPoint> expectedMissingWayPoints = new LinkedList<>();
        WayPoint middleWayPoint = new WayPoint(3.22, 4.22, 1);
        WayPoint endWayPoint = new WayPoint(5.12, 5.13, 2);
        expectedMissingWayPoints.add(middleWayPoint);
        expectedMissingWayPoints.add(endWayPoint);
        //Act & assert
        assertEquals(expectedMissingWayPoints.toString(), runRepository.getMissingWaypoints(run.getId()).toString());

        // clean up
        runRepository.deleteRun(secondRun.getId());
        routeRepository.deleteRoute(secondRoute.getId());
    }

    @Test
    void givenDifferentRunsForSameRouteWithSomeCheckedWaypoint_ReturnMissingWaypoints(){
        //Arrange
        constructUser();



        routeRepository.createRoute(secondRoute, user.getId());

        runRepository.createRun(run, route.getId(), user.getId());
        runRepository.addCheckpointIfValid(run.getId(), 1, 1, 1);

        runRepository.createRun(secondRun, secondRoute.getId(), user.getId());
        runRepository.addCheckpointIfValid(secondRun.getId(), 1, 1, 1);
        runRepository.addCheckpointIfValid(secondRun.getId(), 3, 4, 1);
        runRepository.addCheckpointIfValid(secondRun.getId(), 5, 5, 1);

        List<WayPoint> expectedMissingWayPoints = new LinkedList<>();
        WayPoint middleWayPoint = new WayPoint(3.22, 4.22, 1);
        WayPoint endWayPoint = new WayPoint(5.12, 5.13, 2);
        expectedMissingWayPoints.add(middleWayPoint);
        expectedMissingWayPoints.add(endWayPoint);
        //Act & assert
        assertEquals(expectedMissingWayPoints.toString(), runRepository.getMissingWaypoints(run.getId()).toString());

        // clean up
        runRepository.deleteRun(secondRun.getId());
        routeRepository.deleteRoute(secondRoute.getId());
    }


    @Test
    void givenCreateRun_returnRunCreated(){
        // Arrange
        runRepository.createRun(run, route.getId(), user.getId());
        Run returnedRun = runRepository.getRunWithAllCheckpoints(run.getId());
        // Act
        assertEquals(run.toString(), returnedRun.toString());
    }

    @Test
    void givenRunnerAddsCheckpoints_returnCreatedCheckpointsAddedForARun(){
        // Arrange
        runRepository.createRun(run, route.getId(), user.getId());

        //runRepository.isACheckpoint(run, currentX, currentY);
        double currentX = 1.1;
        double currentY = 1.1;
        int precision = 1;
        runRepository.addCheckpointIfValid(run.getId(), currentX, currentY, precision);

        currentX = 3.21;
        currentY = 4.21;
        precision = 1;
        runRepository.addCheckpointIfValid(run.getId(), currentX, currentY, precision);

        Run returnedRun = runRepository.getRunWithAllCheckpoints(run.getId());
        List<Checkpoint> returnedCheckpoints = returnedRun.getCheckpoints();

        List<Checkpoint> expectedCheckpoint = new LinkedList<>();
        expectedCheckpoint.add(new Checkpoint( new WayPoint(1.12, 1.13, 0) ));
        expectedCheckpoint.add(new Checkpoint( new WayPoint(3.22, 4.22, 1) ));

        // Act
        assertEquals(expectedCheckpoint.size(), returnedCheckpoints.size());
        for (int i=0; i < returnedCheckpoints.size(); i++){
            assertEquals(expectedCheckpoint.get(i).getWayPoint().toString(),
                         returnedCheckpoints.get(i).getWayPoint().toString());
        }
  }

    @Test
    void givenGetRunWithMoreThanOneCheckpointForAWaypoint_returnRunWithTheNewestCheckpointForWaypoint() throws InterruptedException {
        // Arrange
        runRepository.createRun(run, route.getId(), user.getId());

        //runRepository.isACheckpoint(run, currentX, currentY);
        double currentX = 1.1;
        double currentY = 1.1;
        int precision = 1;
        runRepository.addCheckpointIfValid(run.getId(), currentX, currentY, precision);

        currentX = 3.21;
        currentY = 4.21;
        precision = 1;
        runRepository.addCheckpointIfValid(run.getId(), currentX, currentY, precision);
        runRepository.addCheckpointIfValid(run.getId(), currentX, currentY, precision);

        Run returnedRun = runRepository.getRunWithLastCheckpoints(run.getId());
        List<Checkpoint> returnedCheckpoints = returnedRun.getCheckpoints();

        List<Checkpoint> expectedCheckpoint = new LinkedList<>();
        expectedCheckpoint.add(new Checkpoint( new WayPoint(1.12, 1.13, 0) ));
        expectedCheckpoint.add(new Checkpoint( new WayPoint(3.22, 4.22, 1) ));

        // Act
        assertEquals(expectedCheckpoint.size(), returnedCheckpoints.size());
        for (int i=0; i < returnedCheckpoints.size(); i++){
            assertEquals(expectedCheckpoint.get(i).getWayPoint().toString(),
                    returnedCheckpoints.get(i).getWayPoint().toString());
        }
    }

    private Location newLocation = null;
    private List<Run> runsToBeDeleted = new ArrayList<>();
    private List<User> usersToBeDeleted = new ArrayList<>();
    private List<User> participantsToBeDeleted = new ArrayList<>();
    private List<Route> routesToBeDeleted = new ArrayList<>();

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
            runRepository.createRun(run, route.getId(), user.getId());

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

        newLocation = new Location(UUID.randomUUID().toString());
        newLocation.setX(2.2123);
        newLocation.setY(2.3123);
        newLocation.setCity("Stockholm");
        newLocation.setCountry("Sweden");
        newLocation.setStreetName("Main street");
        newLocation.setStreetNumber("5A");
        user.setLocation(newLocation);

        return user;
    }


}
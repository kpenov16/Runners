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
    private boolean ignoreAfterEach = false;

    @BeforeEach
    void beforeEach(){
        ignoreAfterEach = false;
        userRepository = new UserRepositoryImpl();
        routeRepository = new RouteRepositoryImpl();
        runRepository = new RunRepositoryImpl();
        runRepository.setRouteRepository(routeRepository);

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
        //userRepository.createUser(user);

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
        Location location = new Location(UUID.randomUUID().toString());
        location.setX(2.2123);
        location.setY(2.3123);
        location.setCity("Stockholm");
        location.setCountry("Sweden");
        location.setStreetName("Main street");
        location.setStreetNumber("5A");
        route.setLocation(location);
        //routeRepository.createRoute(route, user.getId());

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
    }

    @AfterEach
    void afterEach(){
        if(!ignoreAfterEach){
            runRepository.deleteRun(run.getId());
            routeRepository.deleteRoute(route.getId());
            userRepository.deleteUser(user.getId());
        }
    }

    @Test
    void givenRunWithSomeCheckedWaypoint_ReturnMissingWaypoints(){
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());

        //Arrange
        routeRepository.createRoute(secondRoute, user.getId());
        run.setRoute(route);

        runRepository.createRun(run, user.getId());
        runRepository.addCheckpointIfValid(run.getId(), 1, 1, 1);

        secondRun.setRoute(secondRoute);
        runRepository.createRun(secondRun, user.getId());
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
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());
        run.setRoute(route);

        // Arrange
        runRepository.createRun(run, user.getId());
        Run returnedRun = runRepository.getRunWithAllCheckpoints(run.getId());
        run.getRoute().setNumberOfParticipants(run.getRoute().getNumberOfParticipants()+1);
        // Act
        assertEquals(run.toString(), returnedRun.toString());
    }

    @Test
    void givenCreateRunWithIdThatIsAlreadyUsed_returnRunIdDuplicationException(){
        // Arrange
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());
        run.setRoute(route);
        runRepository.createRun(run, user.getId());

        //act, assert
        RunRepository.RunIdDuplicationException runIdDuplicationException =
                assertThrows(RunRepository.RunIdDuplicationException.class,
                        () -> runRepository.createRun(run, user.getId())
                );
    }

    @Test
    void givenCreateRunWithUnknownRouteId_returnUnknownRouteException(){
        // Arrange
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());

        run.setRoute(constructRoute());

        //act, assert
        RunRepository.UnknownRouteException runUnknownRouteException =
                assertThrows(RunRepository.UnknownRouteException.class,
                        () -> runRepository.createRun(run, user.getId())
                );
    }

    @Test
    void givenCreateRunWithUnknownUserId_returnUnknownUserException(){
        // Arrange
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());
        run.setRoute(route);

        //act, assert
        RunRepository.UnknownUserException runUnknownUserException =
                assertThrows(RunRepository.UnknownUserException.class,
                        () -> {
                            String unknownParticipantId = UUID.randomUUID().toString();
                            runRepository.createRun(run, unknownParticipantId);
                        }
                );
    }

    @Test
    void givenCreateRunWithNullId_returnRunValidationException(){
        // Arrange
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());
        run.setRoute(route);
        run.setId(null);

        //act, assert
        RunRepository.RunValidationException runValidationException =
                assertThrows(RunRepository.RunValidationException.class,
                        () -> runRepository.createRun(run, user.getId())
                );
        assertEquals(RunRepositoryImpl.RUN_ID_IS_NOT_DEFINED ,
                runValidationException.getMessage());
    }

    @Test
    void givenCreateRunWithNullRoute_returnRunValidationException(){
        // Arrange
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());
        run.setRoute(null);

        //act, assert
        RunRepository.RunValidationException runValidationException =
                assertThrows(RunRepository.RunValidationException.class,
                        () -> runRepository.createRun(run, user.getId())
                );
        assertEquals(
                String.format(RunRepositoryImpl.RUN_WITH_ID_S_IS_MISSING_ROUTE_OBJECT, run.getId()),
                runValidationException.getMessage()
        );
    }

    @Test
    void givenRunnerAddsCheckpoints_returnCreatedCheckpointsAddedForARun(){
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());
        run.setRoute(route);

        // Arrange
        runRepository.createRun(run, user.getId());

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
        userRepository.createUser(user);
        routeRepository.createRoute(route, user.getId());
        run.setRoute(route);

        // Arrange
        runRepository.createRun(run, user.getId());

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

    @Test
    void givenDifferentRunsForSameRouteWithSomeCheckedWaypoint_ReturnMissingWaypoints(){
        ignoreAfterEach = true;
        //Arrange
        User routeUser = constructUser(); usersToBeDeleted.add(routeUser);
        userRepository.createUser(routeUser);
        Route newRoute = constructRoute();
        routeRepository.createRoute(newRoute, routeUser.getId());

        User user1 = constructUser(); participantsToBeDeleted.add(user1);
        userRepository.createUser(user1);
        Run runUser1 = constructRun(newRoute);
        runRepository.createRun(runUser1, user1.getId());
        runRepository.addCheckpointIfValid(runUser1.getId(), 1, 1, 1);

        User user2 = constructUser(); participantsToBeDeleted.add(user2);
        userRepository.createUser(user2);
        Run runUser2 = constructRun(newRoute);
        runRepository.createRun(runUser2, user2.getId());
        runRepository.addCheckpointIfValid(runUser2.getId(), 1, 1, 1);
        runRepository.addCheckpointIfValid(runUser2.getId(), 5, 5, 1);

        List<WayPoint> user1ExpectedMissingWayPoints = new LinkedList<WayPoint>(){{
            add(new WayPoint(3.22, 4.22, 1)); add(new WayPoint(5.12, 5.13, 2));
        }};
        List<WayPoint> user2ExpectedMissingWayPoints = new LinkedList<WayPoint>(){{
            add(new WayPoint(3.22, 4.22, 1));
        }};

        //Act
        List<WayPoint> user1PendingWaypoints = runRepository.getMissingWaypoints(runUser1.getId());
        List<WayPoint> user2PendingWaypoints = runRepository.getMissingWaypoints(runUser2.getId());

        //Assert
        assertEquals(user1ExpectedMissingWayPoints.toString(), user1PendingWaypoints.toString());
        assertEquals(user2ExpectedMissingWayPoints.toString(), user2PendingWaypoints.toString());

        // clean up
        deleteCreatedEntities();
    }

    @Test
    void givenAUserTryToRegisterRouteThatHasReachedMaxParticipants_returnMaxParticipansReachedException(){
        ignoreAfterEach = true;
        //Arrange
        User routeUser = constructUser(); usersToBeDeleted.add(routeUser);
        userRepository.createUser(routeUser);
        Route newRoute = constructRoute();
        newRoute.setMinParticipants(10);
        routeRepository.createRoute(newRoute, routeUser.getId());

        registerUsersForRoute(newRoute, 10);

        //Act
        //registerUsersForRoute(newRoute, 1);

        //Assert
        RunRepository.MaxParticipansReachedException maxParticipansReachedException =
                assertThrows(RunRepository.MaxParticipansReachedException.class,
                        () -> registerUsersForRoute(newRoute, 1)
                );
        assertEquals(
               String.format(RunRepositoryImpl.ROUTE_ID_S_HAS_REACHED_MAX_PARTICIPANTS_D,newRoute.getId(),newRoute.getMaxParticipants()),
                maxParticipansReachedException.getMessage());

        // clean up
        deleteCreatedEntities();
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
        WayPoint middleWayPoint = new WayPoint(3.22, 4.22, 1);
        WayPoint endWayPoint = new WayPoint(5.12, 5.13, 2);
        wayPoints.add(startWayPoint);
        wayPoints.add(middleWayPoint);
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
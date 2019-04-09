package dk.runs.runners.repositories;

import dk.runs.runners.entities.*;
import dk.runs.runners.usecases.RouteRepository;
import dk.runs.runners.usecases.RunRepository;
import dk.runs.runners.usecases.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RunRepositoryImplTest {

    private Run run;
    private RunRepositoryImpl runRepository;
    private UserRepository userRepository;
    private RouteRepository routeRepository;
    private Route route;
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
        userRepository.createUser(user);

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
        routeRepository.createRoute(route, user.getId());

        run = new Run();
        run.setRoute(route);
        run.setId(UUID.randomUUID().toString());
        run.setCheckpoints(new LinkedList<Checkpoint>());
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
    void givenCreateRun_returnRunCreated(){

        // Arrange
        runRepository.createRun(run, route.getId(), user.getId());
        Run returnedRun = runRepository.getRun(run.getId());
        // Act
        assertEquals(run.toString(), returnedRun.toString());
    }


    @Test
    void givenRunnerAddsACheckpoint_returnCheckpointAddedForARun(){

        // Arrange
        runRepository.createRun(run, route.getId(), user.getId());

        //runRepository.isACheckpoint(run, currentX, currentY);
        double currentY = 5.1;
        double currentX = 5.1;
        int precision = 1;
        runRepository.addCheckpointIfValid(run.getId(), currentX, currentY, precision);

        Run returnedRun = runRepository.getRun(run.getId());
        List<Checkpoint> returnedCheckpoints = returnedRun.getCheckpoints();
        List<Checkpoint> extecptedCheckpoint = new LinkedList<>();
        extecptedCheckpoint.add(new Checkpoint( new WayPoint(5.12, 5.13, 1) ) );

        // Act
        assertEquals(returnedCheckpoints.toString(), extecptedCheckpoint.toString());
    }
}
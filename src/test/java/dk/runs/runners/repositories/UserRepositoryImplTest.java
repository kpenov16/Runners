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

public class UserRepositoryImplTest {
    private User user;
    private Location location;
    private boolean afterToBeLaunched = true;
    private UserRepository userRepository = null;
    private RouteRepository routeRepository = null;
    private RunRepository runRepository = null;

    @BeforeEach
    public void beforeEach(){
        afterToBeLaunched = true;
        userRepository = new UserRepositoryImpl();

        user = new User(UUID.randomUUID().toString());
        user.setEmail("runner@runner.com");
        user.setUserName("BillGates");
        user.setPassword("bananas");

        location = new Location(UUID.randomUUID().toString());
        location.setX(2.2123);
        location.setY(2.3123);
        location.setCity("Stockholm");
        location.setCountry("Sweden");
        location.setStreetName("Main street");
        location.setStreetNumber("5A");
        user.setLocation(location);

        routeRepository = new RouteRepositoryImpl();
        runRepository = new RunRepositoryImpl();
        ((RunRepositoryImpl) runRepository).setRouteRepository(routeRepository);
    }

    @AfterEach
    public void tearDown(){
        if(afterToBeLaunched){
            userRepository.deleteUser(user.getId());
        }
    }

    @Test
    public void givenRequestForUserHavingMostParticipantsByRoute_returnUserHavingMostParticipants() throws InterruptedException {
        afterToBeLaunched = false;

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

    private List<Run> runsToBeDeleted = new ArrayList<>();
    private List<User> usersToBeDeleted = new ArrayList<>();
    private List<User> participantsToBeDeleted = new ArrayList<>();
    private List<Route> routesToBeDeleted = new ArrayList<>();

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
    public void givenCreateUser_returnUserCreated() {
        userRepository.createUser(user);

        User createdUser = userRepository.getUser(user.getId());

        //assert
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getPassword(), createdUser.getPassword());
        assertEquals(user.getUserName(), createdUser.getUserName());
        assertEquals(user.getLocation().toString(), createdUser.getLocation().toString());

        //tear down
    }

    @Test
    public void givenUserUpdated_returnUserUpdated() {
        //arrange
        userRepository.createUser(user);

        User updatedUser = user;
        updatedUser.setUserName("updated_UserName");
        updatedUser.setEmail("updated_Email");
        updatedUser.setPassword("new_Password");
        updatedUser.getLocation().setStreetName("Prince street");
        updatedUser.getLocation().setStreetNumber("99a");
        updatedUser.getLocation().setCity("Munchen");
        updatedUser.getLocation().setCountry("Germany");
        updatedUser.getLocation().setX(22.22);
        updatedUser.getLocation().setY(11.11);
        //act
        userRepository.updateUser(updatedUser);

        //assert
        User returnedUser = userRepository.getUser(user.getId());
        assertEquals(updatedUser.getUserName(), returnedUser.getUserName());
        assertEquals(updatedUser.getEmail(), returnedUser.getEmail());
        assertEquals(updatedUser.getPassword(), returnedUser.getPassword());
        assertEquals(updatedUser.getLocation().toString(), returnedUser.getLocation().toString());
    }


    @Test
    public void givenUserUpdateWithExistingUserName_returnUserNameDuplicationException(){
        //arrange
        String userName = UUID.randomUUID().toString();

        userRepository.createUser(user);
        User updatedUser = user;
        updatedUser.setUserName(userName);

        User userWithDuplicateUserName = new User(UUID.randomUUID().toString());
        userWithDuplicateUserName.setUserName(userName);
        userWithDuplicateUserName.setEmail("userWithDuplicateUserName@banana.com");
        userWithDuplicateUserName.setPassword("userWithDuplicateUserName_password");
        userWithDuplicateUserName.setLocation(new Location(UUID.randomUUID().toString()));
        userRepository.createUser(userWithDuplicateUserName);

        //Act, Assert
        try{
            assertThrows(UserRepository.UserNameDuplicationException.class,
                    () -> userRepository.updateUser(updatedUser)
            );
        } catch(Throwable e){
            e.printStackTrace();
        }
        //Tear down
        finally {
            userRepository.deleteUser(userWithDuplicateUserName.getId());
        }
    }



    @Test
    public void givenUserUpdateWithExistingEmail_returnEmailDuplicationException(){
        //arrange
        String email = UUID.randomUUID().toString();

        userRepository.createUser(user);
        User updatedUser = user;
        updatedUser.setEmail(email);

        User userWithDuplicateEmail = new User(UUID.randomUUID().toString());
        userWithDuplicateEmail.setUserName(UUID.randomUUID().toString());
        userWithDuplicateEmail.setEmail(email);
        userWithDuplicateEmail.setPassword("userWithDuplicateUserName_password");
        userWithDuplicateEmail.setLocation(new Location(UUID.randomUUID().toString()));
        userRepository.createUser(userWithDuplicateEmail);

        //Act, Assert
        try{
            assertThrows(UserRepository.UserEmailDuplicationException.class,
                    () -> userRepository.updateUser(updatedUser)
            );
        } catch(Throwable e){
            e.printStackTrace();
        }
        //Tear down
        finally {
            userRepository.deleteUser(userWithDuplicateEmail.getId());
        }
    }


    @Test
    public void givenRequestingNonExistingUserById_returnUserNotFoundException() {
        afterToBeLaunched = false;
        assertThrows(UserRepository.UserNotFoundException.class,
                () -> userRepository.getUser(UUID.randomUUID().toString())
        );
    }

    @Test
    public void givenCreateUserWithExistingId_returnUserIdDuplicationException() {
        //Arrange
        userRepository.createUser(user);

        //Act, Assert
        assertThrows(UserRepository.UserIdDuplicationException.class,
                () -> userRepository.createUser(user)
        );
    }

    @Test
    public void givenCreateUserWithExistingUserName_returnUserNameDuplicationException() {
        //Arrange
        userRepository.createUser(user);

        User userWithDuplicateUserName = new User(UUID.randomUUID().toString());
        userWithDuplicateUserName.setUserName(user.getUserName());
        userWithDuplicateUserName.setEmail("userWithDuplicateUserName@banana.com");
        userWithDuplicateUserName.setPassword("userWithDuplicateUserName_password");
        userWithDuplicateUserName.setLocation(new Location(UUID.randomUUID().toString()));

        //Act, Assert
        assertThrows(UserRepository.UserNameDuplicationException.class,
                () -> userRepository.createUser(userWithDuplicateUserName)
        );
    }

    @Test
    public void givenCreateUserWithExistingEmail_returnUserEmailDuplicationException() {
        //Arrange
        userRepository.createUser(user);

        User userWithDuplicateEmail = new User(UUID.randomUUID().toString());
        userWithDuplicateEmail.setEmail(user.getEmail());
        userWithDuplicateEmail.setUserName(UUID.randomUUID().toString());
        userWithDuplicateEmail.setPassword(UUID.randomUUID().toString());
        userWithDuplicateEmail.setLocation(new Location(UUID.randomUUID().toString()));


        //Act, Assert
        assertThrows(UserRepository.UserEmailDuplicationException.class,
                () -> userRepository.createUser(userWithDuplicateEmail)
        );
    }

    @Test
    public void givenUserWithMissingLocation_returnUserMissingLocationException(){
        afterToBeLaunched = false;
        String userId = UUID.randomUUID().toString();
        User user = new User(userId);
        //physical assert 1
        UserRepository.UserMissingLocationException userMissingLocationException =
                assertThrows(UserRepository.UserMissingLocationException.class,
                        () -> userRepository.createUser(user)
                );
        assertEquals("User with id: " + userId + " is missing location.",
                userMissingLocationException.getMessage());

        //physical assert 2
        Location location = new Location("");
        user.setLocation(location);
        userMissingLocationException = assertThrows(UserRepository.UserMissingLocationException.class,
                () -> userRepository.createUser(user)
        );
        assertEquals("User with id: " + userId + " is missing location.",
                userMissingLocationException.getMessage());

        //physical assert 3
        location.setId(null);
        user.setLocation(location);
        userMissingLocationException = assertThrows(UserRepository.UserMissingLocationException.class,
                () -> userRepository.createUser(user)
        );
        assertEquals("User with id: " + userId + " is missing location.",
                userMissingLocationException.getMessage());
    }
}

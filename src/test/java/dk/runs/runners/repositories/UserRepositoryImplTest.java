package dk.runs.runners.repositories;
import dk.runs.runners.entities.*;
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
    private UserRepositoryImpl userRepository = null;
    //private RouteRepository routeRepository = null;
    //private RunRepository runRepository = null;

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

        //routeRepository = new RouteRepositoryImpl();
        //runRepository = new RunRepositoryImpl();
        //((RunRepositoryImpl) runRepository).setRouteRepository(routeRepository);
    }

    @AfterEach
    public void tearDown(){
        if(afterToBeLaunched){
            userRepository.deleteUser(user.getId());
        }
    }


    @Test
    public void givenCreateUserById_returnUserCreated() {
        userRepository.createUser(user);

        User createdUser = userRepository.getUserById(user.getId());

        //assert
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getPassword(), createdUser.getPassword());
        assertEquals(user.getUserName(), createdUser.getUserName());
        assertEquals(user.getLocation().toString(), createdUser.getLocation().toString());

        //tear down
    }

    @Test
    public void givenRequestOfUserByUserName_returnUser() {
        userRepository.createUser(user);

        User createdUser = userRepository.getUser(user.getUserName());

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
        User returnedUser = userRepository.getUserById(user.getId());
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
    public void givenUpdateOfNonExistingUser_returnUserNotFoundException(){
        //arrange
        userRepository.createUser(user);

        User unknownUser = new User(UUID.randomUUID().toString());
        unknownUser.setUserName(user.getUserName());
        unknownUser.setEmail(user.getEmail());
        unknownUser.setPassword(user.getPassword());
        unknownUser.setLocation(user.getLocation());

        //Act, Assert
        assertThrows(UserRepository.UserNotFoundException.class,
                () -> userRepository.updateUser(unknownUser)
        );
    }


    @Test
    public void givenRequestingNonExistingUserById_returnUserNotFoundException() {
        afterToBeLaunched = false;
        assertThrows(UserRepository.UserNotFoundException.class,
                () -> userRepository.getUserById(UUID.randomUUID().toString())
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

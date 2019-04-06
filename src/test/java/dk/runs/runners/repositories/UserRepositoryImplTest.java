package dk.runs.runners.repositories;
import dk.runs.runners.entities.User;
import dk.runs.runners.usecases.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryImplTest {
    private User user;
    UserRepository userRepository = null;

    @BeforeEach
    public void beforeEach(){
        userRepository = new UserRepositoryImpl();

        user = new User(UUID.randomUUID().toString());
        user.setEmail("runner@runner.com");
        user.setUserName("BillGates");
        user.setPassword("bananas");
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteUser(user.getId());
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
    }

    @Test
    public void givenUserUpdated_returnUserUpdated() {
        //arrange
        userRepository.createUser(user);

        User updatedUser = user;
        updatedUser.setUserName("updated_UserName");
        updatedUser.setEmail("updated_Email");
        updatedUser.setPassword("new_Password");

        //act
        userRepository.updateUser(updatedUser);

        //assert
        User returnedUser = userRepository.getUser(user.getId());
        assertEquals(updatedUser.getUserName(), returnedUser.getUserName());
        assertEquals(updatedUser.getEmail(), returnedUser.getEmail());
        assertEquals(updatedUser.getPassword(), returnedUser.getPassword());
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
        userRepository.createUser(userWithDuplicateUserName);

        //Act, Assert
        try{
            assertThrows(UserRepository.UserNameDuplicationException.class,
                    () -> userRepository.updateUser(updatedUser)
            );
        } catch(Exception e){
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

        User userWithDuplicateUserName = new User(UUID.randomUUID().toString());
        userWithDuplicateUserName.setUserName(UUID.randomUUID().toString());
        userWithDuplicateUserName.setEmail(email);
        userWithDuplicateUserName.setPassword("userWithDuplicateUserName_password");
        userRepository.createUser(userWithDuplicateUserName);

        //Act, Assert
        try{
            assertThrows(UserRepository.UserEmailDuplicationException.class,
                    () -> userRepository.updateUser(updatedUser)
            );
        } catch(Exception e){
            e.printStackTrace();
        }
        //Tear down
        finally {
            userRepository.deleteUser(userWithDuplicateUserName.getId());
        }
    }


    @Test
    public void givenRequestingNonExistingUserById_returnUserNotFoundException() {
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

        //Act, Assert
        assertThrows(UserRepository.UserEmailDuplicationException.class,
                () -> userRepository.createUser(userWithDuplicateEmail)
        );
    }

    @Test
    public void givenGetUsersList_returnListIsNotEmpty(){

    }

}

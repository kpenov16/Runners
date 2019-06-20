package dk.runs.runners.services.serviceImpl;

import dk.runs.runners.entities.User;
import dk.runs.runners.services.interfaceRepositories.UserRepository;
import dk.runs.runners.services.interfaceServices.UserService.UserServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceImplTest {
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    public void beforeEach(){
        userService = new UserServiceImpl(null);
        user = new User();
        user.setId(UUID.randomUUID().toString());
        user = new User();
    }

    @Test
    public void givenGetUserByNotExistingUserName_returnUserServiceExceptionWithProperMsg(){
        //arrange
        userService.setUserRepository( getUserRepositoryCastingUserNonFoundEx() );

        String userName = "Hidi";

        UserServiceException userServiceException =
                assertThrows(UserServiceException.class,
                        ()->{
                            userService.getUser(userName);
                        }, "expected UserServiceException, but didn't :(");

        //act, //assert
        assertEquals( String.format( UserServiceException.USER_WITH_USER_NAME_S_NOT_FOUND, userName ),
                userServiceException.getMessage() );
    }


    @Test
    public void givenCreateUserWithExistingUserName_returnUserServiceExceptionWithProperMsg(){
        //arrange
        userService.setUserRepository( getUserRepositoryCastingUserNameDuplicationEx() );

        String userName = "Hidi";
        user.setUserName(userName);

        UserServiceException userServiceException =
                assertThrows(UserServiceException.class,
                        ()->{
                            userService.createUser(user);
                        }, "expected UserServiceException, but didn't :(");

        //act, //assert
        assertEquals( String.format( UserServiceException.USER_WITH_USER_NAME_S_ALREADY_EXIST, userName ),
                      userServiceException.getMessage() );
    }

    @Test
    public void givenCreateUserWithExistingEmail_returnUserServiceExceptionWithProperMsg(){
        //arrange
        userService.setUserRepository( getUserRepositoryCastingEmailDuplicationEx() );
        String userName = "Hidi";
        String email = "hidi@gmail.com";
        user.setUserName(userName);
        user.setEmail(email);

        UserServiceException userServiceException =
                assertThrows(UserServiceException.class,
                        ()->{
                            userService.createUser(user);
                        }, "expected UserServiceException, but didn't :(");

        //act, //assert
        assertEquals( String.format( UserServiceException.USER_WITH_EMAIL_S_ALREADY_EXIST, email ),
                userServiceException.getMessage() );
    }

    private UserRepository getUserRepositoryCastingEmailDuplicationEx() {
        return new UserRepository() {
            @Override
            public void createUser(User user) throws UserIdDuplicationException, UserNameDuplicationException, UserEmailDuplicationException, CreateUserException, UserMissingLocationException {
                throw new UserEmailDuplicationException("any");
            }

            @Override
            public User getUser(String userName) throws GetUserException, UserNotFoundException {
                return null;
            }

            @Override
            public User getUserById(String userId) throws GetUserException, UserNotFoundException {
                return null;
            }

            @Override
            public void deleteUser(String userId) throws DeleteUserException {

            }

            @Override
            public void updateUser(User updatedUser) throws UserNameDuplicationException, UserEmailDuplicationException, UpdateUserException, UserNotFoundException {

            }
        };
    }
    private UserRepository getUserRepositoryCastingUserNameDuplicationEx() {
        return new UserRepository() {
            @Override
            public void createUser(User user) throws UserIdDuplicationException, UserNameDuplicationException, UserEmailDuplicationException, CreateUserException, UserMissingLocationException {
                throw new UserNameDuplicationException("");
            }

            @Override
            public User getUser(String userName) throws GetUserException, UserNotFoundException {
                return null;
            }

            @Override
            public User getUserById(String userId) throws GetUserException, UserNotFoundException {
                return null;
            }

            @Override
            public void deleteUser(String userId) throws DeleteUserException {

            }

            @Override
            public void updateUser(User updatedUser) throws UserNameDuplicationException, UserEmailDuplicationException, UpdateUserException, UserNotFoundException {

            }
        };
    }

    private UserRepository getUserRepositoryCastingUserNonFoundEx() {
        return new UserRepository() {
            @Override
            public void createUser(User user) throws UserIdDuplicationException, UserNameDuplicationException, UserEmailDuplicationException, CreateUserException, UserMissingLocationException {

            }

            @Override
            public User getUser(String userName) throws GetUserException, UserNotFoundException {
                throw new UserNotFoundException("");
            }

            @Override
            public User getUserById(String userId) throws GetUserException, UserNotFoundException {
                return null;
            }

            @Override
            public void deleteUser(String userId) throws DeleteUserException {

            }

            @Override
            public void updateUser(User updatedUser) throws UserNameDuplicationException, UserEmailDuplicationException, UpdateUserException, UserNotFoundException {

            }
        };
    }
}

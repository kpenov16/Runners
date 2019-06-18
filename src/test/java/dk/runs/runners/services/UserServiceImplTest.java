package dk.runs.runners.services;

import dk.runs.runners.entities.User;
import dk.runs.runners.services.interfaceRepositories.UserRepository;
import dk.runs.runners.services.interfaceServices.UserService.UserServiceException;
import dk.runs.runners.services.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceImplTest {

    @BeforeEach
    public void beforeEach(){

    }

    @Test
    public void givenUserWithExistingUserName_returnUserServiceExceptionWithMsg(){
        //arrange
        UserRepository userRepository = getUserRepository();
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        String userName = "Hidi";
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUserName(userName);

        UserServiceException userServiceException =
                assertThrows(UserServiceException.class,
                        ()->{
                            userService.createUser(user);
                        }, "expected UserServiceException, but didn't :(");

        //act, //assert
        assertEquals( String.format( UserServiceException.USER_WITH_USER_NAME_S_ALREADY_EXIST, user.getUserName() ),
                      userServiceException.getMessage() );
    }



    private UserRepository getUserRepository() {
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
}

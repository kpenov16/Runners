package dk.runs.runners.services.interfaceServices;

import dk.runs.runners.entities.User;

public interface UserService {
    void createUser(User user);
    User getUserById(String userId);
    User getUser(String userName);
    User updateUser(User user);

    class UserServiceException extends RuntimeException{
        public static final String USER_WITH_USER_NAME_S_ALREADY_EXIST = "User with user name: %s already exist.";

        public UserServiceException(String msg){
            super(msg);
        }
    }
}

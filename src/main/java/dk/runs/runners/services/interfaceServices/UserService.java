package dk.runs.runners.services.interfaceServices;

import dk.runs.runners.entities.User;

public interface UserService {

    /**
     * Creates a user in the data layer.
     * @param user an instance of the User class with mandatory fields id, userName, email, and password
     * @throws UserServiceException Something went wrong
     */
    void createUser(User user);

    /**
     * Retrieves a user from the data layer.
     * @param userId is the user id of an existing user in the data layer
     * @return User object retrieved from the data layer
     * @throws UserServiceException Something went wrong
     */
    User getUserById(String userId);

    /**
     * Retrieves a user from the data layer.
     * @param userName is the user name of an existing user in the data layer
     * @return User object retrieved from the data layer
     * @throws UserServiceException Something went wrong
     */
    User getUser(String userName);

    /**
     * Updates an existing user in the data layer.
     * @param user the updated User object
     * @throws UserServiceException Something went wrong
     */
    User updateUser(User user);

    class UserServiceException extends RuntimeException{
        public static final String USER_WITH_USER_NAME_S_ALREADY_EXIST = "User with user name: %s already exist.";
        public static final String USER_WITH_EMAIL_S_ALREADY_EXIST = "User with email: %s already exist.";
        public static final String USER_WITH_USER_NAME_S_NOT_FOUND = "User with user name: %s not found.";
        public static final String OTHER = "Something wrong happened: %s";

        public UserServiceException(String msg){
            super(msg);
        }
    }
}

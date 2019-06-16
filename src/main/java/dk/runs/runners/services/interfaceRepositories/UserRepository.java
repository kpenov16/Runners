package dk.runs.runners.services.interfaceRepositories;

import dk.runs.runners.entities.User;

public interface UserRepository {
    /**
     * Creates a user in the data layer.
     * @param user an instance of the User class with mandatory fields id, userName, email, and password
     * @throws UserMissingLocationException if user object is missing valid location object (only unique location id is required)
     * @throws UserIdDuplicationException if user has id already existing in the data layer
     * @throws UserNameDuplicationException if user has userName already existing in the data layer
     * @throws UserEmailDuplicationException if user has email already existing in the data layer
     * @throws CreateUserException if some other unexpected error occurred
     */
    void createUser(User user) throws UserIdDuplicationException,
            UserNameDuplicationException, UserEmailDuplicationException,
            CreateUserException, UserMissingLocationException ;

    /**
     * Retrieves a user from the data layer.
     * @param userName is the user name of an existing user in the data layer
     * @return User object retrieved from the data layer
     * @throws UserNotFoundException if the user with that userName is not found in the data layer
     * @throws GetUserException if other exceptions occurred
     */
    User getUser(String userName) throws GetUserException, UserNotFoundException;

    /**
     * Retrieves a user from the data layer.
     * @param userId is the user id of an existing user in the data layer
     * @return User object retrieved from the data layer
     * @throws UserNotFoundException if the user with that userName is not found in the data layer
     * @throws GetUserException if other exceptions occurred
     */
    User getUserById(String userId) throws GetUserException, UserNotFoundException;

    /**
     * Deletes user from the data layer
     * @param userId is the user id of an existing user in the data layer
     * @throws DeleteUserException  if some unexpected error occurred
     */
    void deleteUser(String userId) throws DeleteUserException;

    /**
     * Updates an existing user in the data layer.
     * @param updatedUser the updated User object
     * @throws UserNameDuplicationException if the new userName is already taken by another user
     * @throws UserEmailDuplicationException if the new email is already taken by another user
     * @throws UserNotFoundException if the id of updatedUser is not found in the data layer
     * @throws UpdateUserException if other exceptions occurred
     */
    void updateUser(User updatedUser) throws UserNameDuplicationException, UserEmailDuplicationException, UpdateUserException, UserNotFoundException;

    class UserRepositoryException extends RuntimeException{
        public UserRepositoryException(String msg){
            super(msg);
        }
    }
    class UpdateUserException extends RuntimeException{
        public UpdateUserException(String msg){
            super(msg);
        }
    }
    class UserNotFoundException extends RuntimeException{
        public UserNotFoundException(String msg){
            super(msg);
        }
    }
    class CreateUserException extends RuntimeException{
        public CreateUserException(String msg){
            super(msg);
        }
    }
    class DeleteUserException extends RuntimeException{
        public DeleteUserException(String msg){
            super(msg);
        }
    }
    class GetUsersException extends RuntimeException{
        public GetUsersException(String msg) {super(msg);}
    }
    class UserIdDuplicationException extends RuntimeException{
        public UserIdDuplicationException(String msg) {super(msg);}
    }
    class UserNameDuplicationException extends RuntimeException{
        public UserNameDuplicationException(String msg) {super(msg);}
    }
    class UserEmailDuplicationException extends RuntimeException{
        public UserEmailDuplicationException(String msg) {super(msg);}
    }
    class UserMissingLocationException extends RuntimeException{
        public UserMissingLocationException(String msg) {super(msg);}
    }
    class GetUserException extends RuntimeException{
        public GetUserException(String msg) {super(msg);}
    }
}

package dk.runs.runners.usecases;

import dk.runs.runners.entities.User;

public interface UserRepository {
    void createUser(User user);

    User getUser(String userId);

    void deleteUser(String userId);

    void updateUser(User updatedUser);

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
}

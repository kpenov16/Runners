package dk.runs.runners.repositories.arrayListImpl;

import dk.runs.runners.entities.User;
import dk.runs.runners.services.interfaceRepositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserRepoArrayListImpl implements UserRepository {

    private static List<User> users = new ArrayList<>();

    static {
        users.add(new User("1", "John", "john@email.com", "123", null, null));
        users.add(new User("2", "Tom", "tom@email.com", "123", null, null));
        users.add(new User("3", "Jan", "jan@email.com", "123", null, null));
    }

    @Override
    public void createUser(User user) throws UserIdDuplicationException, UserNameDuplicationException, UserEmailDuplicationException, CreateUserException, UserMissingLocationException {
        for(User u: users){
            if(u.getUserName().equalsIgnoreCase(user.getUserName())){
                throw new UserNameDuplicationException("User name duplication");
            }
            if(u.getId().equalsIgnoreCase(user.getId())){
                throw new UserIdDuplicationException("User id duplication");
            }
            if(u.getEmail().equalsIgnoreCase(user.getEmail())){
                throw new UserEmailDuplicationException("User email duplication");
            }
        }

        users.add(user);
    }

    @Override
    public User getUser(String userName) throws GetUserException, UserNotFoundException {
        for(User user: users){
            if(user.getUserName().equalsIgnoreCase(userName)){
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUserById(String userId) throws GetUserException, UserNotFoundException {
        for(User user:users){
            if(user.getId().equalsIgnoreCase(userId)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void deleteUser(String userId) throws DeleteUserException {
        User userToRemove = null;
        for(User user:users){
            if(user.getId().equalsIgnoreCase(userId)){
                userToRemove = user;
            }
        }
        users.remove(userToRemove);
    }

    @Override
    public void updateUser(User updatedUser) throws UserNameDuplicationException, UserEmailDuplicationException, UpdateUserException, UserNotFoundException {
        User userToRemove = null;
        for(User u: users){
            if(u.getUserName().equalsIgnoreCase(updatedUser.getUserName())){
                throw new UserNameDuplicationException("User name duplication");
            }
            if(u.getId().equalsIgnoreCase(updatedUser.getId())){
                userToRemove = u;
            }
            if(u.getEmail().equalsIgnoreCase(updatedUser.getEmail())){
                throw new UserEmailDuplicationException("User email duplication");
            }
        }
        users.remove(userToRemove);
        users.add(updatedUser);
    }
}

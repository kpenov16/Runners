package dk.runs.runners.repositories.arrayListImpl;

import dk.runs.runners.entities.Location;
import dk.runs.runners.entities.User;
import dk.runs.runners.services.interfaceRepositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepoArrayListImpl implements UserRepository {

    private static List<User> users = new ArrayList<>();

    // some dummy data
    static {
        users.add(new User("1", "John", "john@email.com", "123", null,
                new ArrayList<Location>(){{add(new Location(UUID.randomUUID().toString(), 0, 2,2, "New York", "USA", "Main street", "55C"));}}));
        users.add(new User("2", "Tom", "tom@email.com", "123", null,
                new ArrayList<Location>(){{add(new Location(UUID.randomUUID().toString(), 0, 2,2, "Copenhagen", "Denmark", "Hovedgaden", "1"));}}));
        users.add(new User("3", "Jan", "jan@email.com", "123", null,
                new ArrayList<Location>(){{add(new Location(UUID.randomUUID().toString(), 0, 2,2, "Vilnius", "Lithuania", "Gedemino pr.", "99"));}}));
    }

    @Override
    public void createUser(User user) throws UserIdDuplicationException, UserNameDuplicationException, UserEmailDuplicationException, CreateUserException, UserMissingLocationException {
        validateUser(user);
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
    private void validateUser(User user) {
        if(user.getLocations() == null || user.getLocations().size() == 0){
            throw new UserMissingLocationException("User with id: " + user.getId() + " is missing location.");
        }

        for(Location location : user.getLocations()){
            if(location.getId() == null || location.getId().isEmpty()){
                throw new UserMissingLocationException("User with id: " + user.getId() + " is missing location.");
            }
        }
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
        User userToReturn = null;
        for(User user:users){
            if(user.getId().equalsIgnoreCase(userId)){
                userToReturn = user;
            }
        }

        if(userToReturn == null){
            throw new UserNotFoundException("User not found");
        } else{
            return userToReturn;
        }
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
        int indexOfUpdateUser = 0;

        for(User u: users){
            if(u.getId().equalsIgnoreCase(updatedUser.getId())){
                indexOfUpdateUser = users.indexOf(u);
            }
        }

        if(indexOfUpdateUser == 0){
            throw new UserNotFoundException("User not found");
        }


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

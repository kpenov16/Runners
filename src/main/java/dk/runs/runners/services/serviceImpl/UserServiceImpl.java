package dk.runs.runners.services.serviceImpl;

import dk.runs.runners.entities.Location;
import dk.runs.runners.entities.User;
import dk.runs.runners.services.interfaceRepositories.UserRepository;
import dk.runs.runners.services.interfaceRepositories.UserRepository.UserNameDuplicationException;
import dk.runs.runners.services.interfaceRepositories.UserRepository.UserEmailDuplicationException;
import dk.runs.runners.services.interfaceRepositories.UserRepository.UserNotFoundException;
import dk.runs.runners.services.interfaceServices.UserService;

import java.util.ArrayList;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User updateUser(User updatedUser) {
        setupLocationsIds(updatedUser);
        try{
            this.userRepository.updateUser(updatedUser);
        }catch (Throwable t){
            handleExceptions(t, updatedUser);
        }
        return this.userRepository.getUser(updatedUser.getUserName());
    }


    @Override
    public void createUser(User user) {
        setupIds(user);
        try{
            this.userRepository.createUser(user);
        }catch (Throwable t){
            handleExceptions(t, user);
        }
    }


    void handleExceptions(Throwable t, String userName){
        User fakeUser = new User();
        fakeUser.setUserName(userName);
        handleExceptions(t, fakeUser);
    }

    void handleExceptions(Throwable t, User user){
        if(t instanceof UserNameDuplicationException){
            throw new UserServiceException(String.format(UserServiceException.USER_WITH_USER_NAME_S_ALREADY_EXIST, user.getUserName()));
        }else if(t instanceof UserEmailDuplicationException){
            throw new UserServiceException(String.format(UserServiceException.USER_WITH_EMAIL_S_ALREADY_EXIST, user.getEmail()));
        }else if(t instanceof UserNotFoundException){
            throw new UserServiceException(String.format(UserServiceException.USER_WITH_USER_NAME_S_NOT_FOUND, user.getUserName()));        }
        else{
            throw new UserServiceException(String.format(UserServiceException.OTHER, t.getMessage()));
        }
    }

    private void setupIds(User user) {
        user.setId(UUID.randomUUID().toString());
        setupLocationsIds(user);
    }

    private void setupLocationsIds(User user) {
        if(user.getLocations()==null || user.getLocations().size()==0){
            user.setLocations(new ArrayList<Location>(){{add(new Location());}});
        }
        for (Location l : user.getLocations()) {
            l.setId(UUID.randomUUID().toString());
        }
    }

    @Override
    public User getUserById(String userId) {
        return this.userRepository.getUserById(userId);
    }

    @Override
    public User getUser(String userName) {
        User user = null;
        try{
            user = this.userRepository.getUser(userName);
        }catch (Throwable t){
            handleExceptions(t, userName);
        }
        return user;
    }


    protected void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

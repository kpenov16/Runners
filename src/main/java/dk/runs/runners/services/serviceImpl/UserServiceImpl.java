package dk.runs.runners.services.serviceImpl;

import dk.runs.runners.entities.Location;
import dk.runs.runners.entities.User;
import dk.runs.runners.services.interfaceRepositories.UserRepository;
import dk.runs.runners.services.interfaceRepositories.UserRepository.UserNameDuplicationException;
import dk.runs.runners.services.interfaceServices.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User updateUser(User updatedUser) {
        setupLocationsIds(updatedUser);
        this.userRepository.updateUser(updatedUser);
        return this.userRepository.getUser(updatedUser.getUserName());
    }


    @Override
    public void createUser(User user) {
        setupIds(user);
        try{
            this.userRepository.createUser(user);
        }catch (UserNameDuplicationException e){
            throw new UserServiceException(String.format(UserServiceException.USER_WITH_USER_NAME_S_ALREADY_EXIST, user.getUserName()));
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
        return this.userRepository.getUser(userName);
    }


}

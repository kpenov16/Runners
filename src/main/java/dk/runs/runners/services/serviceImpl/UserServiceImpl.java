package dk.runs.runners.services.serviceImpl;

import dk.runs.runners.entities.User;
import dk.runs.runners.services.interfaceRepositories.UserRepository;
import dk.runs.runners.services.interfaceServices.UserService;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(User user) {
        this.userRepository.createUser(user);
    }

    @Override
    public User getUserById(String userId) {
        return this.userRepository.getUserById(userId);
    }
}

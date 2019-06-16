package dk.runs.runners.services.interfaceServices;

import dk.runs.runners.entities.User;

public interface UserService {
    void createUser(User user);
    User getUserById(String userId);

    User getUser(String userName);

    User updateUser(User user);
}

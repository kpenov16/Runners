package dk.runs.runners.usecases;

import dk.runs.runners.entities.User;

public class ShowUserCreatedRoutesImpl {
    private UserRepository userRepository;
    private RouteRepository routeRepository;

    public User execute(String userId) {
        User user = userRepository.getUserById(userId);
        user.setRoutes( routeRepository.getRoutes(userId) );
        return user;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setRouteRepository(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public RouteRepository getRouteRepository() {
        return routeRepository;
    }
}

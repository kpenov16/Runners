package dk.runs.runners.usecases;

import dk.runs.runners.entities.Route;
import dk.runs.runners.repositories.RouteRepositoryImpl;
import dk.runs.runners.repositories.UserRepositoryImpl;

public class CreateRouteImpl {
    private RouteRepository routeRepository;
    private UserRepository userRepository;

    public CreateRouteImpl(RouteRepositoryImpl routeRepository, UserRepositoryImpl userRepository) {
        setRouteRepository(routeRepository);
        setUserRepository(userRepository);
    }

    public void execute(Route route, String userId){
        // services
    }

    public void setRouteRepository(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

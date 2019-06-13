package dk.runs.runners.config;

import dk.runs.runners.repositories.mysqlImpl.RouteRepositoryImpl;
import dk.runs.runners.repositories.mysqlImpl.RunRepositoryImpl;
import dk.runs.runners.repositories.mysqlImpl.UserRepositoryImpl;
import dk.runs.runners.services.interfaceRepositories.RouteRepository;
import dk.runs.runners.services.interfaceRepositories.RunRepository;
import dk.runs.runners.services.interfaceRepositories.UserRepository;
import dk.runs.runners.services.interfaceServices.RouteService;
import dk.runs.runners.services.interfaceServices.RunService;
import dk.runs.runners.services.interfaceServices.UserService;
import dk.runs.runners.services.serviceImpl.RouteServiceImpl;
import dk.runs.runners.services.serviceImpl.RunServiceImpl;
import dk.runs.runners.services.serviceImpl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DIConfig {

    @Bean
    @Scope("prototype")
    public RouteService getRouteService(){
        return new RouteServiceImpl(getRouteRepository());
    }

    @Bean
    @Scope("prototype")
    public RouteRepository getRouteRepository(){
        return new RouteRepositoryImpl();
    }
    @Bean
    @Scope("prototype")
    public RunRepository getRunRepository(){
        return new RunRepositoryImpl(getRouteRepository());
    }

    @Bean
    @Scope("prototype")
    public RunService getRunController(){
        return new RunServiceImpl(getRunRepository());
    }

    @Bean
    @Scope("prototype")
    public UserRepository getUserRepository(){
        return new UserRepositoryImpl();
    }

    @Bean
    @Scope("prototype")
    public UserService getUserService(){
        return new UserServiceImpl(getUserRepository());
    }

}

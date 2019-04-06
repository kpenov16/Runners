package dk.runs.runners.config;

import dk.runs.runners.repositories.RouteRepositoryImpl;
import dk.runs.runners.services.RouteService;
import dk.runs.runners.services.RouteServiceImpl;
import dk.runs.runners.usecases.RouteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RoutesConfig {

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
}

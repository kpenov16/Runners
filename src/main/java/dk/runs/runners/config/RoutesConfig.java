package dk.runs.runners.config;

import dk.runs.runners.repositories.mysqlImpl.RouteRepositoryImpl;
import dk.runs.runners.services.interfaceServices.RouteService;
import dk.runs.runners.services.serviceImpl.RouteServiceImpl;
import dk.runs.runners.services.interfaceRepositories.RouteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
//
//@Configuration
//public class RoutesConfig {
//
//    @Bean
//    @Scope("prototype")
//    public RouteService getRouteService(){
//        return new RouteServiceImpl(getRouteRepository());
//    }
//
//    @Bean
//    @Scope("prototype")
//    public RouteRepository getRouteRepository(){
//        return new RouteRepositoryImpl();
//    }
//
//}

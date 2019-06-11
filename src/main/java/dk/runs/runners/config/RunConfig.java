package dk.runs.runners.config;

import dk.runs.runners.repositories.mysqlImpl.RunRepositoryImpl;
import dk.runs.runners.services.interfaceServices.RunService;
import dk.runs.runners.services.serviceImpl.RunServiceImpl;
import dk.runs.runners.services.interfaceRepositories.RunRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
//
//@Configuration
//public class RunConfig {
//    @Bean
//    @Scope("prototype")
//    public RunRepository getRunRepository(){
//        return new RunRepositoryImpl(getRouteRepository());
//    }
//
//    @Bean
//    @Scope("prototype")
//    public RunService getRunController(){
//        return new RunServiceImpl(getRunRepository());
//    }
//}

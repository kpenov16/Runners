package dk.runs.runners.config;

import dk.runs.runners.repositories.RunRepositoryImpl;
import dk.runs.runners.services.RunController;
import dk.runs.runners.services.RunControllerImpl;
import dk.runs.runners.usecases.RunRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RunConfig {
    @Bean
    @Scope("prototype")
    public RunRepository getRunRepository(){
        return new RunRepositoryImpl();
    }

    @Bean
    @Scope("prototype")
    public RunController getRunController(){
        return new RunControllerImpl(getRunRepository());
    }
}

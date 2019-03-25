package dk.runs.runners.runsconfig;

import dk.runs.runners.repositories.RunRepositoryImpl;
import dk.runs.runners.services.run.RunService;
import dk.runs.runners.services.run.RunServiceImpl;
import dk.runs.runners.usecases.RunRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RunsConfig {

    @Bean
    @Scope("prototype")
    public RunService getRunService(){
        return new RunServiceImpl(getRunRepository());
    }

    @Bean
    @Scope("prototype")
    public RunRepository getRunRepository(){
        return new RunRepositoryImpl();
    }
}

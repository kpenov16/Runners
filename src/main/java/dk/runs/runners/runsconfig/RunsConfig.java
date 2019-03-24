package dk.runs.runners.runsconfig;

import dk.runs.runners.repositories.RunRepositoryImpl;
import dk.runs.runners.services.run.RunServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public abstract class RunsConfig {

    @Bean(name="runRepository")
    @Scope("prototype")
    public static RunRepositoryImpl getRunRepository(){
        return new RunRepositoryImpl();
    }

    @Bean(name="runService")
    @Scope("prototype")
    public static RunServiceImpl getRunService(){
        return new RunServiceImpl(getRunRepository());
    }
}

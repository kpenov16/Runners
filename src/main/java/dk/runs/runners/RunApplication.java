package dk.runs.runners;

import dk.runs.runners.RunsConfig.RunsConfig;
import dk.runs.runners.repositories.RunRepositoryImpl;
import dk.runs.runners.services.run.RunServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@SpringBootApplication
public class RunApplication {
    public static void main(String[] args) {
        /*AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(RunsConfig.class);
        ctx.refresh();*/
        SpringApplication.run(RunApplication.class, args);
    }

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

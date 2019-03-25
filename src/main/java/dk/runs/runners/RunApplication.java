package dk.runs.runners;

import dk.runs.runners.repositories.RunRepositoryImpl;
import dk.runs.runners.runsconfig.RunsConfig;
import dk.runs.runners.services.run.RunService;
import dk.runs.runners.services.run.RunServiceImpl;
import dk.runs.runners.usecases.RunRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

//@Configuration
@SpringBootApplication
public class RunApplication {
    public static void main(String[] args) {
        /*AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(RunsConfig.class);
        ctx.refresh();*/
        SpringApplication.run(RunApplication.class, args);

     //   AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
      //          RunsConfig.class);
    }

//    //@Bean(name="runRepository")
//    @Bean
//    @Scope("prototype")
//    public static RunRepository getRunRepository(){
//        return new RunRepositoryImpl();
//    }
//
//    //@Bean(name="runService")
//    @Bean
//    @Scope("prototype")
//    public static RunService getRunService(){
//        return new RunServiceImpl(getRunRepository());
//    }
}

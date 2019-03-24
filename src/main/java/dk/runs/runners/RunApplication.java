package dk.runs.runners;

import dk.runs.runners.RunsConfig.RunsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class RunApplication {
    public static void main(String[] args) {
        /*AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(RunsConfig.class);
        ctx.refresh();*/
        SpringApplication.run(RunApplication.class, args);
    }
}

package dk.runs.runners.rest;

import dk.runs.runners.entities.Run;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class RunController {
    private static final String template = "You are running in %s!";
    private final AtomicInteger counter = new AtomicInteger();

    @RequestMapping("/run")
    public Run greeting(@RequestParam(value="location", defaultValue = "Copenhagen") String location){
        Run run = new Run(counter.incrementAndGet());
        run.setLocation(location);
        return run;
    }
}

// String.format(template, name)

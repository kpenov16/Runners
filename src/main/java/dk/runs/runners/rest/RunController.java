package dk.runs.runners.rest;

import dk.runs.runners.entities.Run;
import dk.runs.runners.repositories.RunRepositoryImpl;
import dk.runs.runners.services.run.RunService;
import dk.runs.runners.services.run.RunServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class RunController {
    private static final String template = "You are running in %s!";
    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("/runs/{id}")
    public Run getRun(@PathVariable int id){
        RunService runService = new RunServiceImpl(new RunRepositoryImpl());
        Run run = runService.getRun(id);
        return run;
    }
}

// String.format(template, name)

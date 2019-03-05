package dk.runs.runners.rest;

import dk.runs.runners.entities.Run;
import dk.runs.runners.repositories.RunRepositoryImpl;
import dk.runs.runners.services.run.RunService;
import dk.runs.runners.services.run.RunServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
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


    @GetMapping("/runs")
    public List<Run> getRuns(){

        List<Run> runs = new LinkedList<>();

        Run run1 = new Run(5);
        run1.setLocation("Riga");
        Run run2 = new Run(6);
        run2.setLocation("Sofia");
        runs.add(run1);
        runs.add(run2);


        return runs;
    }
}

// String.format(template, name)

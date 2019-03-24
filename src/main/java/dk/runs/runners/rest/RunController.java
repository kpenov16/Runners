package dk.runs.runners.rest;

import dk.runs.runners.entities.Run;
import dk.runs.runners.repositories.RunRepositoryImpl;
import dk.runs.runners.services.run.RunService;
import dk.runs.runners.services.run.RunServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RunController {

    @GetMapping("/runs/{id}")  //FIXME crashes if id is not exists
    public Run getRun(@PathVariable String id){
        RunService runService = new RunServiceImpl(new RunRepositoryImpl());
        Run run = runService.getRun(id);
        return run;
    }

    @DeleteMapping("/runs/{id}")
    public void deleteRun(@PathVariable String id){
        RunService runService = new RunServiceImpl(new RunRepositoryImpl());
        runService.deleteRun(id);
    }

    @GetMapping("/runs")
    public List<Run> getRuns(){
        RunService runService = new RunServiceImpl(new RunRepositoryImpl());
        return runService.getRunsList();
    }

    @PostMapping(path = "/runs")//@PostMapping(path = "/createRun")
    public Run addRun(@RequestBody Run run, @PathVariable String creatorId) {
        RunService runService = new RunServiceImpl(new RunRepositoryImpl());
        runService.createRun(run, creatorId);
        return run;
    }
}

// String.format(template, name)

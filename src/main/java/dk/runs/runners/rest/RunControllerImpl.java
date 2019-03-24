package dk.runs.runners.rest;

import dk.runs.runners.resources.RunRequest;
import dk.runs.runners.resources.RunResponce;
import dk.runs.runners.entities.Run;
import dk.runs.runners.services.run.RunService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class RunControllerImpl implements RunController{
    @Resource(name="runService")  RunService runService;
    //RunService runService = runsconfig.getRunService();

    @GetMapping("/runs/{id}")
    public RunResponce getRun(@PathVariable String id){
        RunResponce runResponce = new RunResponce();
        runResponce.run = runService.getRun(id);

        return runResponce;
    }

    @DeleteMapping("/runs/{id}")
    public void deleteRun(@PathVariable String id){
        runService.deleteRun(id);
    }

    @GetMapping("/runs")
    public List<Run> getRuns(){
        return runService.getRunsList();
    }

    @PostMapping(path = "/runs")//@PostMapping(path = "/createRun")
    public RunResponce addRun(@RequestBody RunRequest RunRequest) {
        runService.createRun(RunRequest.run, RunRequest.creatorId);
        RunResponce runResponce = new RunResponce();
        runResponce.error = "";
        runResponce.run = RunRequest.run;
        return runResponce;
    }

}


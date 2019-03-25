package dk.runs.runners.rest;

import dk.runs.runners.resources.RunRequest;
import dk.runs.runners.resources.RunResponse;
import dk.runs.runners.entities.Run;
import dk.runs.runners.services.run.RunService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class RunControllerImpl implements RunController{
    //@Resource(name="runService")
    //RunService runService = runsconfig.getRunService();
    private RunService runService;

    public RunControllerImpl(RunService runService){
        this.runService = runService;
    }

    @GetMapping("/runs/{id}")
    public RunResponse getRun(@PathVariable String id){
        RunResponse runResponse = new RunResponse();
        runResponse.setRun(runService.getRun(id));

        return runResponse;
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
    public RunResponse addRun(@RequestBody RunRequest runRequest) {
        runService.createRun(runRequest.getRun(), runRequest.getCreatorId());
        RunResponse runResponse = new RunResponse();
        runResponse.setError("");
        runResponse.setRun(runRequest.getRun());
        return runResponse;
    }

}


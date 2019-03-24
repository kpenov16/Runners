package dk.runs.runners.rest;

import dk.runs.runners.Resources.RunRequest;
import dk.runs.runners.Resources.RunResponce;
import dk.runs.runners.RunsConfig.RunsConfig;
import dk.runs.runners.entities.Run;
import dk.runs.runners.entities.User;
import dk.runs.runners.repositories.RunRepositoryImpl;
import dk.runs.runners.services.run.RunService;
import dk.runs.runners.services.run.RunServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class RunController {

    @Resource(name="runService")  RunService runService;
    //RunService runService = RunsConfig.getRunService();

    @GetMapping("/runs/{id}")  //FIXME crashes if id is not exists
    public RunResponce getRun(@PathVariable String runId){
        RunResponce runResponce = new RunResponce();
        runResponce.run = runService.getRun(runId);

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


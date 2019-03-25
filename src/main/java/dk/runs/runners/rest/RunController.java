package dk.runs.runners.rest;

import dk.runs.runners.entities.Run;
import dk.runs.runners.resources.RunRequest;
import dk.runs.runners.resources.RunResponse;
import dk.runs.runners.services.run.RunService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

public interface RunController {
//    @Resource(name="runService")
//    RunService runService = null;

    @GetMapping("/runs/{id}")  //FIXME crashes if id is not exists
    public RunResponse getRun(@PathVariable String id);

    @DeleteMapping("/runs/{id}")
    public void deleteRun(@PathVariable String id);

    @GetMapping("/runs")
    public List<Run> getRuns();

    @PostMapping(path = "/runs")//@PostMapping(path = "/createRun")
    public RunResponse addRun(@RequestBody RunRequest RunRequest);
}

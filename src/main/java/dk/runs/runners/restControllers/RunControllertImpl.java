package dk.runs.runners.restControllers;


import dk.runs.runners.entities.Checkpoint;
import dk.runs.runners.entities.Run;
import dk.runs.runners.entities.WayPoint;
import dk.runs.runners.services.interfaceServices.RunService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins="http://localhost:4200")
@RestController
public class RunControllertImpl {
    private RunService runService;

    public RunControllertImpl(RunService runService){
        this.runService = runService;
    }


    @GetMapping("runs/{runId}/waypoints")
    public List<WayPoint> getMissingWaypoints(@PathVariable String runId){
        return runService.getMissingWaypoints(runId);
    }

    @GetMapping("runs/{runId}/checkpoints")
    public List<Checkpoint> getLastestCheckpoints(@PathVariable String runId){
        return runService.getLastestCheckpoints(runId);
    }

    @PutMapping("runs/{runId}/checkpoints")
    public ResponseEntity<Void> addCheckPointIfValid(@PathVariable String runId,
                                                 @RequestParam double currentX,
                                                 @RequestParam double currentY,
                                                 @RequestParam int precision){
        runService.addCheckpointIfValid(runId, currentX, currentY, precision);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/{creatorId}/runs")
    public Run createRun(@PathVariable String creatorId, @RequestBody Run run) {
        Run createdRun = runService.createRun(run, creatorId);
       // URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/(id)").buildAndExpand(createdRun.getId()).toUri();
        return createdRun; //ResponseEntity.created(uri).body(createdRun);
    }

    @GetMapping("runs/{id}")
    public Run getRun(@PathVariable String id){
        return runService.getRun(id);
    }

    @GetMapping("users/{creatorId}/runs")
    public List<Run> getRuns(@PathVariable String creatorId){ return runService.getRuns(creatorId);}

    @GetMapping("runResults/{runId}")
    public Run getRunWithLatestCheckpoints(@PathVariable String runId){ return runService.getRunWithLatestCheckpoints(runId);}
}

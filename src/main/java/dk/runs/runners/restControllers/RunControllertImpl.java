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


    @GetMapping("run/{runId}/waypoint")
    public List<WayPoint> getMissingWaypoints(@PathVariable String runId){
        return runService.getMissingWaypoints(runId);
    }

    @GetMapping("run/{runId}/checkpoint")
    public List<Checkpoint> getLastestCheckpoints(@PathVariable String runId){
        return runService.getLastestCheckpoints(runId);
    }

    @PostMapping(path = "/users/(creatorid)/runs")
    public ResponseEntity<Run> createRun(@PathVariable String creatorid, @RequestBody Run run) {
        Run createdRun = runService.createRun(run, creatorid);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/(id)").buildAndExpand(createdRun.getId()).toUri();
        return ResponseEntity.created(uri).body(createdRun);
    }
}

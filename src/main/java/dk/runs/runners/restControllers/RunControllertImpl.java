package dk.runs.runners.restControllers;


import dk.runs.runners.entities.Checkpoint;
import dk.runs.runners.entities.WayPoint;
import dk.runs.runners.services.interfaceServices.RunService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}

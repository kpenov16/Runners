package dk.runs.runners.rest;


import dk.runs.runners.entities.WayPoint;
import dk.runs.runners.services.RunController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins="http://localhost:4200")
@RestController
public class RunRestImpl {
    private RunController runController;

    public RunRestImpl(RunController runController){
        this.runController = runController;
    }


    @GetMapping("run/{runId}/waypoint")
    public List<WayPoint> getMissingWaypoints(@PathVariable String runId){
        return runController.getMissingWaypoints(runId);
    }
}

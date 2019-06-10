package dk.runs.runners.services;


import dk.runs.runners.entities.WayPoint;
import dk.runs.runners.usecases.RunRepository;

import java.util.List;

public class RunControllerImpl implements RunController {

    private RunRepository runRepository;

    public RunControllerImpl(RunRepository runRepository){
        this.runRepository = runRepository;
    }

    @Override
    public List<WayPoint> getMissingWaypoints(String runId) throws RunServiceException {
        return null;
    }
}

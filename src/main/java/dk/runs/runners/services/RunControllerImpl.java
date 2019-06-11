package dk.runs.runners.services;


import dk.runs.runners.entities.Checkpoint;
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

        try{
            return runRepository.getMissingWaypoints(runId);
        } catch (RunRepository.GetMissingWaypointException e){
            throw new RunServiceException("Error. Could not retrieve waypoints");
        }
    }

    public List<Checkpoint> getLastestCheckpoints(String runId){
        try{
            return runRepository.getRunWithLastCheckpoints(runId).getCheckpoints();
        } catch (RunRepository.CheckpointException e){
            throw new RunController.RunServiceException("Error retrieving checkpoints");
        }
    }
}

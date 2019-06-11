package dk.runs.runners.services.serviceImpl;


import dk.runs.runners.entities.Checkpoint;
import dk.runs.runners.entities.WayPoint;
import dk.runs.runners.services.interfaceServices.RunService;
import dk.runs.runners.services.interfaceRepositories.RunRepository;

import java.util.List;

public class RunServiceImpl implements RunService {

    private RunRepository runRepository;

    public RunServiceImpl(RunRepository runRepository){
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
            throw new RunService.RunServiceException("Error retrieving checkpoints");
        }
    }
}

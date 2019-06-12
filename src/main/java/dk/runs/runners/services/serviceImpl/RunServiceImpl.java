package dk.runs.runners.services.serviceImpl;


import dk.runs.runners.entities.Checkpoint;
import dk.runs.runners.entities.Run;
import dk.runs.runners.entities.WayPoint;
import dk.runs.runners.services.interfaceServices.RunService;
import dk.runs.runners.services.interfaceRepositories.RunRepository;

import java.util.List;
import java.util.UUID;

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

    @Override
    public Run createRun(Run run, String creatorId) throws RunServiceException {
        try {
           run.setId(UUID.randomUUID().toString());
           runRepository.createRun(run, creatorId);
           return run;
        }catch (RunRepository.RunIdDuplicationException e){
            if (e.getMessage().contains("PRIMARY")){
                throw new RunServiceException("Run already created!");
            }else {
                throw new RunServiceException("An error occurred while creating run, try again later.");
            }
        }
    }

    @Override
    public Run getRun(String id) throws RunServiceException {
        try {
            return runRepository.getRunWithLastCheckpoints(id);
        }catch (RunRepository.CheckpointException e){
            throw new RunService.RunServiceException("An error occurred while retrieving run.");
        }
    }

    @Override
    public void addCheckpointIfValid(String runId, double currentX, double currentY, int precision) throws RunServiceException {
        try{
            runRepository.addCheckpointIfValid(runId, currentX, currentY, precision);
        } catch(RunRepository.CheckpointException e){
            throw new RunServiceException("Error. Could not sumbit your position.");
        }
    }

    @Override
    public Run updateRun(Run run) throws RunServiceException {
        return null;
    }

    @Override
    public Void deleteRun(String id) throws RunServiceException {
        return null;
    }

}

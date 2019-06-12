package dk.runs.runners.services.interfaceServices;

import dk.runs.runners.entities.Checkpoint;
import dk.runs.runners.entities.Run;
import dk.runs.runners.entities.WayPoint;

import java.util.List;

public interface RunService {

    List<WayPoint> getMissingWaypoints(String runId) throws RunServiceException;
    List<Checkpoint> getLastestCheckpoints(String runId) throws RunServiceException;

    Run createRun(Run run, String creatorId) throws RunServiceException;
    Run getRun(String id) throws RunServiceException;
    List<Run> getRuns(String creatorId) throws RunServiceException;
    void addCheckpointIfValid(String runId, double currentX, double currentY, int precision) throws RunServiceException;
    Run updateRun(Run run) throws RunServiceException;
    Void deleteRun(String id) throws RunServiceException;

    class RunServiceException extends RuntimeException{
        public RunServiceException(String msg){
            super(msg);
        }
    }
}

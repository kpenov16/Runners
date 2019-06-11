package dk.runs.runners.services.interfaceServices;

import dk.runs.runners.entities.Checkpoint;
import dk.runs.runners.entities.Run;
import dk.runs.runners.entities.WayPoint;

import java.util.List;

public interface RunService {

    List<WayPoint> getMissingWaypoints(String runId) throws RunServiceException;
    List<Checkpoint> getLastestCheckpoints(String runId) throws RunServiceException;

    Run createRun(Run run, String creatorId) throws RunServiceException;

    class RunServiceException extends RuntimeException{
        public RunServiceException(String msg){
            super(msg);
        }
    }
}

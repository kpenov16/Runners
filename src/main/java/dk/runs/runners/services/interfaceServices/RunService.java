package dk.runs.runners.services.interfaceServices;

import dk.runs.runners.entities.Checkpoint;
import dk.runs.runners.entities.WayPoint;

import java.util.List;

public interface RunService {

    List<WayPoint> getMissingWaypoints(String runId) throws RunServiceException;
    List<Checkpoint> getLastestCheckpoints(String runId) throws RunServiceException;

    class RunServiceException extends RuntimeException{
        public RunServiceException(String msg){
            super(msg);
        }
    }
}

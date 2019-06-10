package dk.runs.runners.services;

import dk.runs.runners.entities.WayPoint;

import java.util.List;

public interface RunController {

    List<WayPoint> getMissingWaypoints(String runId) throws RunServiceException;

    class RunServiceException extends RuntimeException{
        public RunServiceException(String msg){
            super(msg);
        }
    }
}
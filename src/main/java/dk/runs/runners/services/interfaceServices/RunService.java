package dk.runs.runners.services.interfaceServices;

import dk.runs.runners.entities.Checkpoint;
import dk.runs.runners.entities.Run;
import dk.runs.runners.entities.WayPoint;

import java.util.List;

public interface RunService {

    /**
     * Retrieves a list of waypoints the user has yet to pass
     * @param runId the id of the current run, which a user is running
     * @return a list of waypoint objects
     * @throws RunServiceException Something went wrong
     */
    List<WayPoint> getMissingWaypoints(String runId) throws RunServiceException;

    /**
     * Retrieves a list of checkpoints the user has obtained
     * @param runId the id of the current run, which a user is running
     * @return a list of checkpoint objects
     * @throws RunServiceException Something went wrong
     */
    List<Checkpoint> getLastestCheckpoints(String runId) throws RunServiceException;

    /**
     * Creates a run in the data layer for an existing route and a user.
     * @param run the new run object with unique id and attached existing route, the route object as minimum needs the correct route id
     * @param creatorId is the id of the user, if the id is not existing in the data layer UnknownUserException exception is thrown
     * @throws RunServiceException Something went wrong
     */
    Run createRun(Run run, String creatorId) throws RunServiceException;

    /**
     * Retrieves a run from the data layer
     * @param id is the id of the current run, which a user is running
     * @return a run object
     * @throws RunServiceException Something went wrong
     */
    Run getRun(String id) throws RunServiceException;

    /**
     * Retrieves a list of runs, which the user is signed up
     * @param creatorId is the id of the user which has signed up for runs
     * @return  a list of runs objects by searched user
     * @throws RunServiceException Something went wrong
     */
    List<Run> getRuns(String creatorId) throws RunServiceException;

    /** Adds a checkpoint to the run if the user is close to
     *  one of the waypoints of the route.
     *
     * @param runId the id of the current run, which a user is running
     * @param currentX the latitude value of the user's current coordinate
     * @param currentY the longitude value of the user's current coordinate
     * @param precision describes how close a user should be to the waypoint in order to mark a waypoint as checked
     * @throws RunServiceException Something went wrong
     */
    void addCheckpointIfValid(String runId, double currentX, double currentY, int precision) throws RunServiceException;

    /**
     * Updates an existing run in the data layer
     * @param run run object to be updated
     * @throws RunServiceException Something went wrong
     */
    void updateRun(Run run) throws RunServiceException;

    //Unused method
    Void deleteRun(String id) throws RunServiceException;

    class RunServiceException extends RuntimeException{
        public RunServiceException(String msg){
            super(msg);
        }
    }
}

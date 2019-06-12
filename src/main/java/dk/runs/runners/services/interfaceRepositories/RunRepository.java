package dk.runs.runners.services.interfaceRepositories;

import dk.runs.runners.entities.Run;
import dk.runs.runners.entities.WayPoint;

import java.util.List;

public interface RunRepository {
    /**
     * Creates a run in the data layer for an existing route and a user.
     * @param run the new run object with unique id and attached existing route, the route object as minimum needs the correct route id
     * @param participantId is the id of the user, if the id is not existing in the data layer UnknownUserException exception is thrown
     * @throws RunIdDuplicationException if there is a run with the same id in the data layer
     * @throws UnknownRouteException if the route by id is not an existing route in the data layer
     * @throws UnknownUserException if the participantId not the id of an existing user in the data layer
     * @throws CreateRunException other not predefined exceptions are wrapped in this exception
     * @throws RunValidationException if run object is not constructed properly
     * @throws MaxParticipansReachedException if route has reached max number of participants
     */
    void createRun(Run run, String participantId) throws RunIdDuplicationException,
            UnknownRouteException, UnknownUserException, CreateRunException, RunValidationException,
            MaxParticipansReachedException;

    /**
     * Retrieves a run from the data layer with all checkpoints
     * @param runId is the id of the current run, which a user is running
     * @return a run object
     * @throws CheckpointException if there occurred any error inserting a checkpoint
     */
    Run getRunWithAllCheckpoints(String runId) throws CheckpointException;

    /**
     * Retrieves a run from the data layer with the most recently passed checkpoints
     * @param runId is the id of the current run, which a user is running
     * @return a run object
     * @throws CheckpointException if there occurred any error inserting a checkpoint
     */
    Run getRunWithLastCheckpoints(String runId) throws CheckpointException;

    /**
     * Deletes an existing run in the data layer
     * @param runId is the id of the run to be deleted
     * @throws DeleteRunException if any error occurred while deleting the run
     */
    void deleteRun(String runId) throws DeleteRunException;

    /** Adds a checkpoint to the run if the user is close to
     *  one of the waypoints of the route.
     *
     * @param runId the id of the current run, which a user is running
     * @param currentX the latitude value of the user's current coordinate
     * @param currentY the longitude value of the user's current coordinate
     * @param precision describes how close a user should be to the waypoint in order to mark a waypoint as checked
     * @throws CheckpointException if there is occurred any error inserting a checkpoint
     */
    void addCheckpointIfValid(String runId, double currentX, double currentY, int precision) throws CheckpointException;

    /**
     * Retrieves a list of waypoints the user has yet to pass
     * @param runId the id of the current run, which a user is running
     * @return a list of waypoint objects
     * @throws GetMissingWaypointException if any error occurred while retrieving the waypoints
     */
    List<WayPoint> getMissingWaypoints(String runId) throws GetMissingWaypointException;

    /**
     * Retrieves a list of runs, which the user is signed up
     * @param userId is the id of the user which has signed up for runs
     * @return  a list of runs objects by searched user
     * @throws GetRunsException if there are no runs with matching user id existing in the data layer
     */
    List<Run> getRuns(String userId) throws GetRunsException;


    class RunValidationException extends RuntimeException{
        public RunValidationException(String msg) {super(msg);}
    }
    class CreateRunException extends RuntimeException{
        public CreateRunException(String msg){ super(msg); }
    }
    class UnknownUserException extends RuntimeException{
        public UnknownUserException(String msg) {super(msg);}
    }
    class UnknownRouteException extends RuntimeException{
        public UnknownRouteException(String msg) {super(msg);}
    }
    class RunIdDuplicationException extends RuntimeException{
        public RunIdDuplicationException(String msg) {super(msg);}
    }
    class DeleteRunException extends RuntimeException{
        public DeleteRunException(String msg) {super(msg);}
    }
    class CheckpointException extends RuntimeException{
        public CheckpointException(String msg) {super(msg);}
    }
    class GetMissingWaypointException extends RuntimeException{
        public GetMissingWaypointException(String msg) {super(msg);}
    }
    class MaxParticipansReachedException extends RuntimeException{
        public MaxParticipansReachedException(String msg) {super(msg);}
    }
    class GetRunsException extends RuntimeException{
        public GetRunsException(String msg) {super(msg);}
    }
}

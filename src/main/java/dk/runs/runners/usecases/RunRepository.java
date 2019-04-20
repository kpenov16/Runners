package dk.runs.runners.usecases;

import dk.runs.runners.entities.Run;
import dk.runs.runners.entities.WayPoint;

import java.util.List;

public interface RunRepository {
    /**
     * Creates a run in the data layer for an existing route and a user.
     * @param run the new run object with unique id and attached existing route
     * @param participantId is the id of the user, if the id is not existing in the data layer UnknownUserException exception is thrown
     * @throws RunIdDuplicationException if there is a run with the same id in the data layer
     * @throws UnknownRouteException if the route by id is not an existing route in the data layer
     * @throws UnknownUserException if the participantId not the id of an existing user in the data layer
     * @throws CreateRunException other not predefined exceptions are wrapped in this exception
     * @throws RunValidationException if run object is not constructed properly
     */
    void createRun(Run run, String participantId) throws RunIdDuplicationException,
            UnknownRouteException, UnknownUserException, CreateRunException, RunValidationException;

    Run getRunWithAllCheckpoints(String runId);

    Run getRunWithLastCheckpoints(String runId);

    void deleteRun(String runId);

    void addCheckpointIfValid(String runId, double currentX, double currentY, int precision);

    List<WayPoint> getMissingWaypoints(String runId);

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



}

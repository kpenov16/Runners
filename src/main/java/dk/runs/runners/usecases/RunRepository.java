package dk.runs.runners.usecases;

import dk.runs.runners.entities.Run;
import dk.runs.runners.entities.WayPoint;

import java.util.List;

public interface RunRepository {

    void createRun(Run run, String routeId, String participantId);

    Run getRunWithAllCheckpoints(String runId);

    Run getRunWithLastCheckpoints(String runId);

    void deleteRun(String runId);

    void addCheckpointIfValid(String runId, double currentX, double currentY, int precision);

    List<WayPoint> getMissingWaypoints(String runId);

    class CreateRunException extends RuntimeException{
        public CreateRunException(String msg){
            super(msg);
        }
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

package dk.runs.runners.usecases;

import dk.runs.runners.entities.Run;

public interface RunRepository {

    void createRun(Run run, String routeId, String participantId);

    Run getRun(String runId);

    void deleteRun(String runId);

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
}

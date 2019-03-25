package dk.runs.runners.usecases;

import dk.runs.runners.entities.Run;

import java.math.BigInteger;
import java.util.List;

public interface RunRepository {
    void createRun(Run run, String creatorId) throws CreateRunException;

    Run getRun(String id) throws RunNotFoundException;

    void updateRun(Run updatedRun);

    List<Run> getRunsList();

    void deleteRun(String id)throws DeleteRunException ;

    class UpdateRunException extends RuntimeException{
        public UpdateRunException(String msg){
            super(msg);
        }
    }

    class RunNotFoundException extends RuntimeException{
        public RunNotFoundException(String msg){
            super(msg);
        }
    }

    class CreateRunException extends RuntimeException{
        public CreateRunException(String msg){
            super(msg);
        }
    }
    class DeleteRunException extends RuntimeException{
        public DeleteRunException(String msg){
            super(msg);
        }
    }
    class GetRunsException extends RuntimeException{
        public GetRunsException(String msg) {super(msg);}
    }
    class RunIdDuplicationException extends RuntimeException{
        public RunIdDuplicationException(String msg) {super(msg);}
    }
}

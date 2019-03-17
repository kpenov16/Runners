package dk.runs.runners.usecases;

import dk.runs.runners.entities.Run;

import java.util.List;

public interface RunRepository {
    void createRun(Run run) throws CreateRunException;

    Run getRun(int id) throws RunNotFoundException;

    void updateRun(Run updatedRun);

    List<Run> getRunsList();

    void deleteRun(int id)throws DeleteRunException ;

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

}

package dk.runs.runners.usecases;

import dk.runs.runners.entities.Run;

public interface RunRepository {
    void createRun(Run run) throws CreateRunException;

    Run getRun(int id) throws RunNotFoundException;

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
}

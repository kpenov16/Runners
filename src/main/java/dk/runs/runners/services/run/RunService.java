package dk.runs.runners.services.run;

import dk.runs.runners.entities.Run;

import java.util.List;

public interface RunService {
    Run getRun(String id);
    List<Run> getRunsList();
    void createRun(Run run, String creatorId);
    void deleteRun(String id);

    class RunServiceException extends RuntimeException{
        public RunServiceException(String msg){
            super(msg);
        }
    }

}

package dk.runs.runners.services.run;

import dk.runs.runners.entities.Run;
import dk.runs.runners.usecases.RunRepository;

import java.util.List;

public class RunServiceImpl implements RunService {

    private RunRepository runRepository;

    public RunServiceImpl(RunRepository runRepository){
        this.runRepository = runRepository;
    }

    @Override
    public Run getRun(String id) {
        return runRepository.getRun(id);
    }

    @Override
    public List<Run> getRunsList() {
        try {
            return runRepository.getRunsList();
        }catch (RunRepository.GetRunsException e){
            throw new RunServiceException("Could not receive run events from database");
        }
    }

    @Override
    public void createRun(Run run, String creatorId) {
        try {
            runRepository.createRun(run, creatorId);
        }catch (RunRepository.RunIdDuplicationException e){
            if(e.getMessage().contains("PRIMARY")){
                throw new RunServiceException("Run already created!");
            } else {
                throw new RunServiceException("Error creating a run. Try again later.");
            }
        }

    }

    @Override
    public void deleteRun(String id) {
        runRepository.deleteRun(id);
    }
}

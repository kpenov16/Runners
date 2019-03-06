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
    public Run getRun(int id) {
        return runRepository.getRun(id);
    }

    @Override
    public List<Run> getRunsList() {
        return runRepository.getRunsList();
    }
}

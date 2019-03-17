package dk.runs.runners.services.run;

import dk.runs.runners.entities.Run;

import java.util.List;

public interface RunService {
    Run getRun(int id);
    List<Run> getRunsList();
    void createRun(Run run);
    void deleteRun(int id);
}

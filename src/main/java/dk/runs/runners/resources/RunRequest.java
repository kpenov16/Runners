package dk.runs.runners.resources;

import dk.runs.runners.entities.Run;

public class RunRequest {
    private Run run;
    private String creatorId;

    public RunRequest(){}
    public Run getRun() {
        return run;
    }
    public void setRun(Run run) {
        this.run = run;
    }
    public String getCreatorId() {
        return creatorId;
    }
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}

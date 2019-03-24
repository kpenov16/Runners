package dk.runs.runners.resources;

import dk.runs.runners.entities.Run;

public class RunResponse {
    private String error;
    private Run run;
    private String creatorId;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

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

    public RunResponse(){}
}

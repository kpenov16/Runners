package dk.runs.runners.rest;


import dk.runs.runners.services.RunController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins="http://localhost:4200")
@RestController
public class RunRestImpl {
    private RunController runController;

    public RunRestImpl(RunController runController){
        this.runController = runController;
    }

}

package dk.runs.runners.rest;

import dk.runs.runners.entities.Run;
import dk.runs.runners.repositories.RunRepositoryImpl;
import dk.runs.runners.services.run.RunService;
import dk.runs.runners.services.run.RunServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RunController {

    @GetMapping("/runs/{id}")  //FIXME crashes if id is not exists
    public Run getRun(@PathVariable int id){
        RunService runService = new RunServiceImpl(new RunRepositoryImpl());
        Run run = runService.getRun(id);
        return run;
    }


    @GetMapping("/runs")
    public List<Run> getRuns(){

//        List<Run> runs = new LinkedList<>();
//        Run run1 = new Run(5);
//        run1.setLocation("Riga");
//        Run run2 = new Run(6);
//        run2.setLocation("Sofia");
//        runs.add(run1);
//        runs.add(run2);
        RunService runService = new RunServiceImpl(new RunRepositoryImpl());
        return runService.getRunsList();
    }
}

// String.format(template, name)

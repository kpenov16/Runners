package dk.runs.runners.rest;

import dk.runs.runners.entities.Run;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class RunController {
    private static final String template = "Bonjour, %s!";
    private final AtomicInteger counter = new AtomicInteger();

    @RequestMapping("/run")
    public Run greeting(@RequestParam(value="name", defaultValue = "Runner") String name){
        return new Run(counter.incrementAndGet());
    }
}

// String.format(template, name)

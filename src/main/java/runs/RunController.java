package runs;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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

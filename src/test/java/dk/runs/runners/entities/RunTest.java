package dk.runs.runners.entities;

import dk.runs.runners.entities.Run;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RunTest {


    @Test
    public void givenNewRun_returnRunCreated() {
        Run run = new Run(1);
        run.setLocation("Copenhagen");
        assertEquals("Wrong id", 1, run.getId());
        assertEquals("Wrong location","Copenhagen",run.getLocation());
    }


}

package dk.runs.runners.entities;

import dk.runs.runners.entities.Run;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RunTest {


    @Test
    public void givenNewRun_returnRunCreated() {
        String id = UUID.randomUUID().toString();
        Run run = new Run(id);
        run.setLocation("Copenhagen");
        assertEquals("Wrong id", id, run.getId());
        assertEquals("Wrong location","Copenhagen",run.getLocation());
    }


}

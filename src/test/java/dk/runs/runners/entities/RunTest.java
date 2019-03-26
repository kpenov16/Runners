package dk.runs.runners.entities;


import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RunTest {

    @Test
    public void givenNewRun_returnRunCreated() {
        String id = UUID.randomUUID().toString();
        Run run = new Run(id);
        run.setLocation("Copenhagen");
        assertEquals(id, run.getId());
        assertEquals("Copenhagen",run.getLocation());
    }
}

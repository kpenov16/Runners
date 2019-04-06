package dk.runs.runners.entities;


import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteTest {

    @Test
    public void givenNewRun_returnRunCreated() {
        String id = UUID.randomUUID().toString();
        Route route = new Route(id);
        route.setLocation("Copenhagen");
        assertEquals(id, route.getId());
        assertEquals("Copenhagen", route.getLocation());
    }
}

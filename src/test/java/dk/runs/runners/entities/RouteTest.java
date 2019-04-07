package dk.runs.runners.entities;


import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteTest {
    private final long ms = System.currentTimeMillis();
    private final long ONE_HOUR = 60*60*1_000;
    private final int DISTANCE = 5_000;
    private Route route;

    @Test
    public void givenNewRoute_returnRouteCreated() {
        String id = UUID.randomUUID().toString();
        route = new Route(id);
        route.setTitle("Route three");
        route.setLocation("Stockholm");
        route.setDescription("It is going to be very fun!!!");
        Date date = new Date(ms);
        route.setDate(date);
        route.setStatus("active");
        route.setDuration(ONE_HOUR);
        route.setDistance(DISTANCE);

        List<WayPoint> wayPoints = new LinkedList<>();
        WayPoint startWayPoint = new WayPoint(1.12, 1.13, 0);
        WayPoint endWayPoint = new WayPoint(5.12, 5.13, 1);
        wayPoints.add(startWayPoint);
        wayPoints.add(endWayPoint);

        route.setWayPoints(wayPoints);

        assertEquals(id, route.getId());
        assertEquals("Route three", route.getTitle());
        assertEquals("Stockholm", route.getLocation());
        assertEquals("It is going to be very fun!!!", route.getDescription());
        assertEquals(date.getTime(), route.getDate().getTime());
        assertEquals("active", route.getStatus());
        assertEquals(ONE_HOUR, route.getDuration());
        assertEquals(DISTANCE, route.getDistance());

        for (int i=0; i< wayPoints.size(); i++){
            assertEquals(wayPoints.get(i).getIndex(), route.getWayPoints().get(i).getIndex());
            assertEquals(wayPoints.get(i).getX(), route.getWayPoints().get(i).getX(), 0.1);
            assertEquals(wayPoints.get(i).getY(), route.getWayPoints().get(i).getY(), 0.1);
        }
    }
}

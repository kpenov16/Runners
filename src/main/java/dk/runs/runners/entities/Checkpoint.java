package dk.runs.runners.entities;

public class Checkpoint {
    private WayPoint wayPoint;
    private long visitedTimestamp;

    public Checkpoint(){}

    public Checkpoint(WayPoint wayPoint) {
        setWayPoint(wayPoint);
    }

    public WayPoint getWayPoint() {
        return wayPoint;
    }

    public void setWayPoint(WayPoint wayPoint) {
        this.wayPoint = wayPoint;
    }

    public long getVisitedTimestamp() {
        return visitedTimestamp;
    }

    public void setVisitedTimestamp(long visitedTimestamp) {
        this.visitedTimestamp = visitedTimestamp;
    }

    @Override
    public String toString(){
        return "waypoint: " + wayPoint + ", " +
                "visitedTimestamp: " + visitedTimestamp;
    }
}

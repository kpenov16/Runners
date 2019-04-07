package dk.runs.runners.entities;

public class WayPoint {
    private double x;
    private double y;
    private int index;

    public WayPoint(double x, double y, int index) {
        setX(x);
        setY(y);
        setIndex(index);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

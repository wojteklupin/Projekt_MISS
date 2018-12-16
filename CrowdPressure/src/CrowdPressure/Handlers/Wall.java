package CrowdPressure.Handlers;

import CrowdPressure.Point;

public class Wall {

    private Point start;
    private Point end;
    private double length;

    public Wall(Point start, Point end){
        this.start = start;
        this.end = end;
        this.length = start.distance(end);
    }

    public double getLength() {
        return length;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}

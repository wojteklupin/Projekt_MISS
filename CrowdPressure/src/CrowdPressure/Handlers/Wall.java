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
    @Override
    public String toString(){
        return "Wall: \nStart: (" + Double.toString(this.start.getX()) + ", " + Double.toString(this.start.getY()) + "), End: (" + Double.toString(this.end.getX()) + ", "
                + Double.toString(this.end.getY()) + ")\n";
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

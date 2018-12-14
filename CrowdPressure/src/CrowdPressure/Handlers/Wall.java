package CrowdPressure.Handlers;

import CrowdPressure.Point;

public class Wall {

    private Point start;
    private Point end;
    private double length;


    public Wall(Point p1, Point p2){
        this.start = p1;
        this.end = p2;
        this.length = p1.distance(p2);
    }



}

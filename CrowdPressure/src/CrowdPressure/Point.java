package CrowdPressure;

import java.util.Vector;

public class Point {

    private double xCoord;
    private double yCoord;

    public Point(double x, double y){
        this.xCoord = x;
        this.yCoord = y;
    }

    public double distance(Point p){
        Vector pCoords = p.getCoords();
        double horizontal = Math.pow(((Integer)pCoords.get(0) - this.xCoord), 2);
        double vertical = Math.pow(((Integer)pCoords.get(1) - this.yCoord), 2);
        return Math.sqrt(horizontal + vertical);
    }

    public Vector<Double> getCoords(){
        Vector v = new Vector(2);
        v.addElement(this.xCoord);
        v.addElement(this.yCoord);
        return v;
    }
}

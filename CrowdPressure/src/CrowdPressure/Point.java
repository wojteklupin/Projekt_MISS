package CrowdPressure;



public class Point {

    private double x;
    private double y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double distance(Point p){
        return Math.sqrt( Math.pow(p.getY() - this.y, 2) + Math.pow(p.getX() - this.x, 2));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

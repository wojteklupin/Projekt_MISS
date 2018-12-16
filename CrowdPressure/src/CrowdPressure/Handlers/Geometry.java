package CrowdPressure.Handlers;

import CrowdPressure.Point;

public class Geometry {

    //checks if point p belongs to line between start and end
    public static boolean contains(Point start, Point end, Point p){
        return start.distance(end) == start.distance(p) + p.distance(end); //can be vulnerable due to limitations in precision
    }

    //calculates distance between line determined by points a and b and point p
    public double segmentDistanceFromPoint(Point a, Point b, Point p){

        double x0 = p.getX();
        double y0 = p.getY();

        double x1 = a.getX();
        double y1 = a.getY();
        double x2 = b.getX();
        double y2 = b.getY();

        double numerator = Math.abs(x0 * (y2 - y1) - y0 * (x2 - x1) + x2 * y1 - x1 * y2);
        double denominator = Math.sqrt(Math.pow(y2 - y1,2) + Math.pow(x2 - x1, 2));

        double distanceFromLine = numerator / denominator; //distance between p and line containing a and b
        double distanceFromStart = p.distance(a); //distance between p and a
        double distanceFromEnd = p.distance(b); //distance between p and b


        //determining if calculated distance applies also to chosen segment of line between points a and b
        double maxDistance = Math.max(distanceFromStart, distanceFromEnd);
        double c = Math.sqrt(Math.pow(maxDistance,2) - Math.pow(distanceFromLine, 2));

        return c <= a.distance(b) ? distanceFromLine : Math.min(distanceFromEnd, distanceFromStart);


    }

    //checks if point p belongs to part of the circle with center point, central outline point direction and angle of alpha
    //basically it will help determine if one human is in second's field of view
    public static boolean pointInside(Point center, Point direction, Double alpha, Point p){
        double a = center.distance(direction);
        double b = center.distance(p);
        double c = center.distance(p);
        if(b > c){
            return false;
        }
        else{
            double cos = (Math.pow(a,2) + Math.pow(b,2) - Math.pow(c,2)) / 2 * a * b; //transformed formula from law of cosines
            return Math.acos(cos) < alpha;
        }
    }

    public static Point middleOfSegment(Point a, Point b){
        double x1 = a.getX();
        double y1 = a.getY();

        double x2 = b.getX();
        double y2 = b.getY();

        return new Point((x1 + x2) / 2, (y1 + y2) / 2);
    }

    public static Point pointOfCrossing(Point a1, Point a2, Point b1, Point b2){
        return new Point(0,0);
    }

    public static boolean lineInside(Point center, Point direction, Double alpha, Point start, Point end){
        return false;
    }

}

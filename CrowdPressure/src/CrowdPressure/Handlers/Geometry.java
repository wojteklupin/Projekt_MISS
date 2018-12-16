package CrowdPressure.Handlers;

import CrowdPressure.Point;

public class Geometry {

    //checks if point p belongs to line between start and end
    public static boolean contains(Point start, Point end, Point p){
        return start.distance(end) == start.distance(p) + p.distance(end); //can be vulnerable due to limitations in precision
    }

    //checks if point p belongs to part of the circle with center point, central outline point direction and angle of alpha
    //basically it will help determine if one human is in second's field of view
    public static boolean inside(Point center, Point direction, Double alpha, Point p){
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

}

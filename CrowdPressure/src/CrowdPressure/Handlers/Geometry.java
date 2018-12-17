package CrowdPressure.Handlers;

import CrowdPressure.Point;

public class Geometry {

    //checks if point p belongs to line between start and end
    public static boolean contains(Point start, Point end, Point p) {
        return start.distance(end) == start.distance(p) + p.distance(end); //can be vulnerable due to limitations in precision
    }

    //calculates distance between line determined by points a and b and point p
    public static double segmentDistanceFromPoint(Point a, Point b, Point p) {

        double x0 = p.getX();
        double y0 = p.getY();

        double x1 = a.getX();
        double y1 = a.getY();
        double x2 = b.getX();
        double y2 = b.getY();

        double numerator = Math.abs(x0 * (y2 - y1) - y0 * (x2 - x1) + x2 * y1 - x1 * y2);
        double denominator = Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));

        double distanceFromLine = numerator / denominator; //distance between p and line containing a and b
        double distanceFromStart = p.distance(a); //distance between p and a
        double distanceFromEnd = p.distance(b); //distance between p and b


        //determining if calculated distance applies also to chosen segment of line between points a and b
        double maxDistance = Math.max(distanceFromStart, distanceFromEnd);
        double c = Math.sqrt(Math.pow(maxDistance, 2) - Math.pow(distanceFromLine, 2));

        return c <= a.distance(b) ? distanceFromLine : Math.min(distanceFromEnd, distanceFromStart);


    }

    //checks if point p belongs to part of the circle with center point, central outline point direction and angle of alpha
    //basically it will help determine if one human is in second's field of view
    public static boolean pointInside(Point center, Point direction, Double alpha, Point p) {
        double a = center.distance(direction);
        double b = center.distance(p);
        double c = center.distance(p);
        if (b > c) {
            return false;
        } else {
            double cos = (Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / 2 * a * b; //transformed formula from law of cosines
            return Math.acos(cos) < alpha;
        }
    }

    public static Point middleOfSegment(Point a, Point b) {
        double x1 = a.getX();
        double y1 = a.getY();

        double x2 = b.getX();
        double y2 = b.getY();

        return new Point((x1 + x2) / 2, (y1 + y2) / 2);
    }

    public static Point pointOfCrossing(Point a1, Point a2, Point b1, Point b2) {

        double x1 = a1.getX();
        double y1 = a1.getY();

        double x2 = a2.getX();
        double y2 = a2.getY();

        double x3 = b1.getX();
        double y3 = b1.getY();

        double x4 = b2.getX();
        double y4 = b2.getY();

        double xp = ((x1 * y2 - x2 * y1) * (x3 - x4) - (x1 - x2) * (x3 * y4 - x4 * y3)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));

        double yp = ((x1 * y2 - x2 * y1) * (y3 - y4) - (y1 - y2) * (x3 * y4 - x4 * y3)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));

        return new Point(xp, yp);

    }


//rotates point p by angle in  direction ( direction should be 1 (clockwise) or -1 (opposite)
    public static Point rotatePoint(Point p, double angle, int direction){
        double x = p.getX() * Math.cos(direction) + direction * p.getY() * Math.sin(direction);
        double y = p.getY() * Math.cos(direction) + p.getX() * Math.sin(direction);
        return new Point(x, y);
    }

    //checks if any part of line segment is placed inside circle, center is center of the circle, direction is straight point from the center and alpha is an angle in both directions
    //start and end are vertices of line segment
    public static boolean lineInside(Point center, Point direction, Double alpha, Point start, Point end) {
        return Geometry.pointInside(center, direction, alpha, pointOfCrossing(center, rotatePoint(direction, alpha, 1), start, end)) ||
                Geometry.pointInside(center, direction, alpha, pointOfCrossing(center, rotatePoint(direction, alpha, -1), start, end)) ||
                Geometry.pointInside(center, direction, alpha, pointOfCrossing(center, direction, start, end)) || Geometry.pointInside(center, direction, alpha, start) ||
                Geometry.pointInside(center, direction, alpha, end );
    }
}

package CrowdPressure.Geometry;

import java.util.Optional;

import CrowdPressure.Map.Point;

public class Geometry {

    public static Optional<Point> pointOfCrossing(Point p, Double angle, Point p1, Point p2){

        double tan = Math.tan(angle * Math.PI);
        double xi = p.getX();
        double yi = p.getY();
        double xp1 = p1.getX();
        double yp1 = p1.getY();
        double xp2 = p2.getX();
        double yp2 = p2.getY();

        double numerator = -xi * xp1 * tan + xi * xp2 * tan + yi * xp1 - yi * xp2 - xp1 * yp2 + yp1 * xp2;
        double denominator = -xp1 * tan + yp1 + xp2 * tan - yp2;
        double x = numerator / denominator;
        double y = tan * x + yi - tan * xi;


        if (lineContainsPoint(new Point(x, y), new Point(xp1, yp1), new Point(xp2, yp2))) {

            if ((angle < 1 && y > yi) || (angle > 1 && y < yi)
                    || (angle == 1 && y == yi && x > xi) || (angle == 0 && y == yi && x < xi))
                return Optional.of(new Point(x, y));
        }
        return Optional.empty();
    }

    public static Double angleBetween(Point pos, Point destinationPoint){
        double result = (pos.getY() - destinationPoint.getY()) / (pos.getX() - destinationPoint.getX());
        result = Math.atan(result) / Math.PI;
        if (result < 0) {
            result = 1 + result;
        }
        if (pos.getY() >= destinationPoint.getY()) {
            if ((pos.getX() >= destinationPoint.getX()) || (result != 0)) {
                result = result + 1;
            }
        }
        return result;
    }

    public static Optional<Vector> vectorStraightPoint(Point p, Point p1, Point p2){
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double a = y2 - y1;
        double b = x1 - x2;
        double c = x2 * y1 - y2 * x1;
        double x0 = p.getX();
        double y0 = p.getY();
        double denominator = (Math.pow(a, 2) + Math.pow(b, 2));
        double x = (b * (b * x0 - a * y0) - a * c) / denominator;
        double y = (a * (-b * x0 + a * y0) - b * c) / denominator;

        if (lineContainsPoint(new Point(x, y), new Point(x1, y1), new Point(x2, y2))) {
            Vector result = vectorFromTwoPoints(new Point(x, y), new Point(x0, y0));
            return Optional.of(result);
        }
        return Optional.empty();
    };


    public static Double angleDifference(Double a1, Double a2){
        double p1 = Math.abs(a1 - a2);
        double p2 = 2.0 - p1;
        return p1 < p2 ? p1 : p2;
    };


    public static Vector vectorFromTwoPoints(Point start, Point end) {
        return new Vector(new Point(end.getX() - start.getX(), end.getY() - start.getY()));
    }


    public static boolean lineContainsPoint(Point checking, Point pA, Point pB) {
        double x = checking.getX();
        double y = checking.getY();
        double x1 = pA.getX();
        double y1 = pA.getY();
        double x2 = pB.getX();
        double y2 = pB.getY();

        return (((x1 - 0.001 <= x && x <= x2 + 0.001) || (x2 - 0.001 <= x && x <= x1 + 0.001)) && ((y1 - 0.001 <= y && y <= y2 + 0.001) || (y2 - 0.001 <= y && y <= y1 + 0.001)));
    }

    public static Vector subtractVectors(Vector v1, Vector v2) {
        return new Vector(new Point(v1.getX() - v2.getX(), v1.getY() - v2.getY()));
    }


    public static Vector addVectors(Vector v1, Vector v2) {
        return new Vector(new Point(v1.getX() + v2.getX(), v1.getY() + v2.getY()));
    }



}

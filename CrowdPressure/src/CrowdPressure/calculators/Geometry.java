package CrowdPressure.calculators;

import java.util.Optional;
import java.util.function.BiFunction;

import CrowdPressure.Point;
import CrowdPressure.calculators.figures.LinePointAngle;
import CrowdPressure.calculators.figures.LineTwoPoints;
import CrowdPressure.calculators.figures.Vector;
import CrowdPressure.Configuration;

public class Geometry {

    public static Optional<Point> crossPointTwoLines(LinePointAngle lpa, LineTwoPoints ltp){

        // TODO alpha =0.5 or 1.5
        // TODO inf points
        double tan = Math.tan(lpa.getAngle() * Math.PI);
        double xi = lpa.getPoint().getX();
        double yi = lpa.getPoint().getY();
        double xp1 = ltp.getStartPosition().getX();
        double yp1 = ltp.getStartPosition().getY();
        double xp2 = ltp.getEndPosition().getX();
        double yp2 = ltp.getEndPosition().getY();

        double numerator = -xi * xp1 * tan + xi * xp2 * tan + yi * xp1 - yi * xp2 - xp1 * yp2 + yp1 * xp2;
        double denominator = -xp1 * tan + yp1 + xp2 * tan - yp2;
        double x = numerator / denominator;
        double y = tan * x + yi - tan * xi;


        if (checkIfPointIsInBetween(new Point(x, y), new Point(xp1, yp1), new Point(xp2, yp2))) {

            if ((lpa.getAngle() < 1 && y > yi) || (lpa.getAngle() > 1 && y < yi)
                    || (lpa.getAngle() == 1 && y == yi && x > xi) || (lpa.getAngle() == 0 && y == yi && x < xi))
                return Optional.of(new Point(x, y));
        }
        return Optional.empty();
    }

    public static Double calculateAngle(Point pos, Point destinationPoint){
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
    };

    public static Optional<Vector> vectorStraightPoint(Point p, LineTwoPoints l){
        double x1 = l.getStartPosition().getX();
        double y1 = l.getStartPosition().getY();
        double x2 = l.getEndPosition().getX();
        double y2 = l.getEndPosition().getY();
        double a = y2 - y1;
        double b = x1 - x2;
        double c = x2 * y1 - y2 * x1;
        double x0 = p.getX();
        double y0 = p.getY();
        double denominator = (Math.pow(a, 2) + Math.pow(b, 2));
        double x = (b * (b * x0 - a * y0) - a * c) / denominator;
        double y = (a * (-b * x0 + a * y0) - b * c) / denominator;

        if (lineContainsPoint(new Point(x, y), new Point(x1, y1), new Point(x2, y2))) {
            // double result = Math.abs(a * x0 + b * y0 + c) / Math.sqrt(denominator);
            Vector result = vectorFromTwoPoints(new Point(x, y), new Point(x0, y0));
            // cod.i(result);
            return Optional.of(result);
        }
        return Optional.empty();
    };

    public static boolean isBigger(Double d1, Double d2){
        return d1 < d2;
    };

    public static Double angleDiff(Double a1, Double a2){
        double p1 = Math.abs(a1 - a2);
        double p2 = 2.0 - p1;
        return p1 < p2 ? p1 : p2;
    };


    public static Vector vectorFromTwoPoints(Point start, Point end) {
        return new Vector(new Point(end.getX() - start.getX(), end.getY() - start.getY()));
    }


    public static boolean checkIfPointIsInBetween(Point checking, Point pA, Point pB) {
        double x = checking.getX();
        double y = checking.getY();
        double x1 = pA.getX();
        double y1 = pA.getY();
        double x2 = pB.getX();
        double y2 = pB.getY();

        return (((x1 - Configuration.PRECISION_OF_CALCULATIONS <= x
                && x <= x2 + Configuration.PRECISION_OF_CALCULATIONS)
                || (x2 - Configuration.PRECISION_OF_CALCULATIONS <= x
                && x <= x1 + Configuration.PRECISION_OF_CALCULATIONS))
                && ((y1 - Configuration.PRECISION_OF_CALCULATIONS <= y
                && y <= y2 + Configuration.PRECISION_OF_CALCULATIONS)
                || (y2 - Configuration.PRECISION_OF_CALCULATIONS <= y
                && y <= y1 + Configuration.PRECISION_OF_CALCULATIONS)));
    }

    public static Vector subtractVectors(Vector v1, Vector v2) {
        // cod.i("SUBTRACT ", Arrays.asList(v1, v2));
        return new Vector(new Point(v1.getX() - v2.getX(), v1.getY() - v2.getY()));
    }


    public static Vector addVectors(Vector v1, Vector v2) {
        return new Vector(new Point(v1.getX() + v2.getX(), v1.getY() + v2.getY()));
    }

    public static boolean lineContainsPoint(Point p, Point start, Point end) {
        return start.distance(end) == start.distance(p) + p.distance(end); //can be vulnerable due to limitations in precision
    }

}

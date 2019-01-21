package CrowdPressure.calculators;

import CrowdPressure.calculators.figures.LinePointAngle;
import CrowdPressure.calculators.figures.LineTwoPoints;
import CrowdPressure.calculators.figures.Vector;
import CrowdPressure.model.Board;
import CrowdPressure.model.MinimumDistance;
import CrowdPressure.model.map.Wall;
import CrowdPressure.model.pedestrian.Human;
import CrowdPressure.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Collisions {

    private double alpha;
    private Human human;
    private Board board;

    public Collisions(double alpha, Human p){
        this.alpha = alpha;
        this.human = p;
        this.board = p.getBoard();
    }

    public Double calculateWallCollisionDistance(){
        double minDistance = this.human.getRangeOfView();

        for(Wall w : this.board.getWalls()){
            Optional<Point> pointOfCrossing = Geometry.crossPointTwoLines(new LinePointAngle(this.human.getPosition(), alpha), new LineTwoPoints(w.getStartPosition(), w.getEndPosition()));
            if(pointOfCrossing.isPresent()){
                if(pointOfCrossing.get().distance(this.human.getPosition()) < minDistance){
                    minDistance = pointOfCrossing.get().distance(this.human.getPosition());
                }
            }
        }

        return minDistance;
    }

    public double calculatePedestrianCollisionDistance(){

        List<Point> points = new ArrayList<>();
        double a = Math.tan(this.alpha * Math.PI);
        double b = this.human.getPosition().getY() - a * this.human.getPosition().getX();
        double minDistance = this.human.getRangeOfView();

        for(Human p : this.board.getHumans()){
            if(p.getId() != this.human.getId()){
                Point oldPosition = p.getPosition();
                Vector move = p.getDesiredSpeed();

                Point newPosition = new Point(oldPosition.getX() + move.getX(), oldPosition.getY() + move.getY());
                double radius = p.getRadius();
                double x = newPosition.getX();
                double y = newPosition.getY();

                double value = -1 * Math.pow(x , 2) * Math.pow(a, 2) + 2 * x * y * a - 2 * x * a * b + Math.pow(a, 2) * Math.pow(radius, 2) - Math.pow(y,2) + 2 * y * b - Math.pow(b, 2)
                        + Math.pow(radius,2);
                if(value > 0){
                    double root = Math.sqrt(value);
                    double x1 = (root + x + a * y - a * b) / (Math.pow(a, 2) + 1);
                    Point p1 = new Point(x1, a * (x1 - this.human.getPosition().getX()) + this.human.getPosition().getY());
                    if(Geometry.vectorFromTwoPoints(this.human.getPosition(), p1).getAngle() == this.alpha){
                        if(this.human.getPosition().distance(p1) < minDistance){
                            minDistance = this.human.getPosition().distance(p1);
                        }
                    }
                    double x2 = (-root + x + a * y - a * b) / (Math.pow(a, 2) + 1);
                    Point p2 = new Point(x2, a * (x2 - this.human.getPosition().getX()) + this.human.getPosition().getY());
                    if(Geometry.vectorFromTwoPoints(this.human.getPosition(), p2).getAngle() == this.alpha){
                        if(this.human.getPosition().distance(p2) < minDistance){
                            minDistance = this.human.getPosition().distance(p2);
                        }
                    }
                } else if(value == 0){
                    double xn = (x + a * y - a * b) / (Math.pow(a, 2) + 1);
                    Point pn = new Point(xn, a * (xn - this.human.getPosition().getX()) + this.human.getPosition().getY());
                    if(Geometry.vectorFromTwoPoints(this.human.getPosition(), pn).getAngle() == this.alpha){
                        if(this.human.getPosition().distance(pn) < minDistance){
                            minDistance = this.human.getPosition().distance(pn);
                        }
                    }
                }
            }
        }


        return minDistance;
    }

    public MinimumDistance calculateCollisionDistance(){ //dwie powyższe zrobić prywatne, używać tylko tej
        double walldistance = this.calculateWallCollisionDistance();
        double pedestriandistance = this.calculatePedestrianCollisionDistance();
        double comparableValue = walldistance < pedestriandistance ? walldistance : pedestriandistance;
        double finalDistance = this.human.getRangeOfView() > comparableValue ? comparableValue : this.human.getRangeOfView();

        if(walldistance == finalDistance && walldistance != this.human.getRangeOfView()){
            return new MinimumDistance(finalDistance, Optional.of(finalDistance));
        }
        else{
            return new MinimumDistance(finalDistance, Optional.empty());
        }
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}


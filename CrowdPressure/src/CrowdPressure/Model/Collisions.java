package CrowdPressure.Model;

import CrowdPressure.Geometry.Geometry;
import CrowdPressure.Geometry.Vector;
import CrowdPressure.Map.Board;
import CrowdPressure.Geometry.MinDistance;
import CrowdPressure.Map.Wall;
import CrowdPressure.Human;
import CrowdPressure.Map.Point;

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

    public Optional<Double> calculateWallCollisionDistance(){
        double minDistance = this.human.getRangeOfView();

        for(Wall w : this.board.getWalls()){
            Optional<Point> pointOfCrossing = Geometry.pointOfCrossing(this.human.getPosition(), alpha, w.getStartPosition(), w.getEndPosition());
            if(pointOfCrossing.isPresent()){
                if(pointOfCrossing.get().distance(this.human.getPosition()) < minDistance){
                    minDistance = pointOfCrossing.get().distance(this.human.getPosition());
                    return Optional.of(minDistance);
                }
            }
        }

        return Optional.empty();
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

    public MinDistance calculateCollisionDistance(){

        Optional<Double> walldistance = this.calculateWallCollisionDistance();
        double pedestriandistance = this.calculatePedestrianCollisionDistance();
        double rangeOfView = this.human.getRangeOfView();

        double minDistance = rangeOfView;
        minDistance = minDistance > pedestriandistance ? pedestriandistance : minDistance;

        if(walldistance.isPresent()){
            minDistance = minDistance > walldistance.get() ? walldistance.get() : minDistance;
            return new MinDistance(minDistance, walldistance);
        }
        else{
            return new MinDistance(minDistance, Optional.empty());
        }

    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}


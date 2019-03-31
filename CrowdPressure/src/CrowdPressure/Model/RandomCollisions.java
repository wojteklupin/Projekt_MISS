package CrowdPressure.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import CrowdPressure.Geometry.Direction;
import CrowdPressure.Geometry.Geometry;
import CrowdPressure.Geometry.MinDistance;
import CrowdPressure.Human;

public class RandomCollisions {
    private static final Random rand = new Random();
    private Human human;

    public RandomCollisions(Human human){
        this.human = human;
    }


    public Direction getDirections(){

        List<Direction> directions = new ArrayList<>();

        double start = this.human.getVisionCenter() - this.human.getAngleOfView();
        double end = this.human.getVisionCenter() + this.human.getAngleOfView();

        double step = 0.1;

        double alpha = start;
        Collisions collisionsCalculator = new Collisions(alpha, this.human);

        for(double i = start; i <= end; i += step){

            alpha = i;

            alpha = alpha <= 2 && alpha >= 0 ? alpha : alpha - (alpha / Math.abs(alpha)) * 2;

            collisionsCalculator.setAlpha(alpha);

            MinDistance collisionDistance = collisionsCalculator.calculateCollisionDistance();
            double destinationDistance = this.human.getDestinationDistanceFunction(alpha, collisionDistance.getMinimumDistance());

            directions.add(new Direction(alpha, collisionDistance.getMinimumDistance(), collisionDistance.getWallDistance(), destinationDistance));
        }


        Double destinationAngle = Geometry.angleBetween(
                human.getNextPosition(),
                human.getDestination()
        );

        List<Direction> closeDirections = new ArrayList<>();
        for(Direction d: directions){
            if(Geometry.angleDifference(d.getAlpha(), destinationAngle) < 0.2){
                closeDirections.add(d);
            }
        }

        List<Direction> closeFreeDirections = getFreeDirections(closeDirections);

        List<Direction> FreeDirections = getFreeDirections(directions);



        if (!closeFreeDirections.isEmpty()) {

            return getOptimalDirection(closeFreeDirections);

        } else if (FreeDirections.isEmpty()) {

            if (getRandom(5)) {

                return changeVision();

            } else {

                return getOptimalDirection(directions);

            }
        } else if (FreeDirections.size() < directions.size()) {

            Direction minDist = getOptimalDirection(directions);

            Optional<Double> notMovingObstacle = minDist.getWallObsacleDistance();

            if (notMovingObstacle.isPresent()) {
                if (notMovingObstacle.get() < 5) {

                    return getOptimalDirection(FreeDirections);

                }
            }

            if (getRandom(5)) {

                return minDist;

            } else {

                return getOptimalDirection(directions);

            }
        } else {

            return getOptimalDirection(directions);
        }
    }

    private List<Direction> getFreeDirections(List<Direction> directions) {

        List<Direction> filtered = new ArrayList<>();

        for (Direction d : directions) {
            if (!d.getWallObsacleDistance().isPresent()) {
                filtered.add(d);
            }
        }

        return filtered;
    }

    private boolean getRandom(int probablity) {
        int random = rand.nextInt(100);
        return random < probablity;
    }

    private Direction changeVision() {
        return getRandomDirection();
    }

    private Direction getRandomDirection() {
        double alpha = (double) rand.nextInt(10000) / 5000;
        return new Direction(alpha, Double.MAX_VALUE, Optional.empty(),
                Double.MIN_VALUE);
    }


    public Direction getOptimalDirection(List<Direction> directions) {

        Direction optimal = directions.get(0);

        for (Direction d: directions){
            if(d.getDestinationDistance() < optimal.getDestinationDistance()){
                optimal = d;
            }
        }

        return optimal;
    }

}

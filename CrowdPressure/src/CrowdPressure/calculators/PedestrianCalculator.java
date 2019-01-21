package CrowdPressure.calculators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import CrowdPressure.Configuration;
import CrowdPressure.Point;
import CrowdPressure.calculators.figures.Vector;
import CrowdPressure.model.DirectionInfo;
import CrowdPressure.model.Board;
import CrowdPressure.model.MinimumDistance;
import CrowdPressure.model.pedestrian.Human;

public class PedestrianCalculator {
    private static final Random rand = new Random();
    private static final int RANDOM_POOL = 10000;
    private Human human;
    private CrowdPressure crowdPressureCal;
    private Force forceCal;

    public PedestrianCalculator(Human human, Board board){
        this.human = human;
        this.crowdPressureCal = new CrowdPressure();
        this.forceCal = new Force(board);
    }


    public DirectionInfo getDirectionInfo(){

        List<DirectionInfo> directionInfos = new ArrayList<>();

        double start = this.human.getVisionCenter() - this.human.getAngleOfView();
        double end = this.human.getVisionCenter() + this.human.getAngleOfView();

        double step = 0.12;

        double alpha = start;
        Collisions collisionsCalculator = new Collisions(alpha, this.human);

        for(double i = start; i <= end; i += step){

            alpha = i;

            alpha = alpha <= 2 && alpha >= 0 ? alpha : alpha - (alpha / Math.abs(alpha)) * 2;

            collisionsCalculator.setAlpha(alpha);

            MinimumDistance collisionDistance = collisionsCalculator.calculateCollisionDistance();
            double destinationDistance = this.human.getDestinationDistanceFunction(alpha, collisionDistance.getMinimumDistance());

            directionInfos.add(new DirectionInfo(alpha, collisionDistance, destinationDistance));
        }


        Double destinationAngle = Geometry.calculateAngle(
                human.getNextPosition(),
                human.getDestination()
        );


// wybiera takie kierunki, gdzie różnica między wybranym kątem a kątem do celu jest mniejsza niż jakiś przyjęty parametr i przekazuje do funkcji
        List<DirectionInfo> freeDestinationDirections = getNoNotMovingObstaclesDirections(directionInfos.stream().filter(di -> { //lista kierunków odpowiednio bliskich docelowemu nie posiadająca
            //nieruchomych przeszkód (ścian)
                    return Geometry.angleDiff(di.getAlpha(), destinationAngle) < Configuration.DESTINATION_ANGLE_RANGE;
                }
        ).collect(Collectors.toList()));

        List<DirectionInfo> hasNotNotMovingObstacles = getNoNotMovingObstaclesDirections(directionInfos); //lista wszystkich kierunków nie posiadających nieruchomych przeszkód


//        if (!freeDestinationDirections.isEmpty()) {
//            cod.i(pedestrianInformation.getStaticInformation().getId() + " X "
//                    + pedestrianInformation.getVariableInformation().getVisionCenter());
//            return getMinimumDestinationDistance(freeDestinationDirections);
//        } else {
//            cod.i(pedestrianInformation.getStaticInformation().getId() + " Y "
//                    + pedestrianInformation.getVariableInformation().getVisionCenter());
//            DirectionInfo minDist = getMinimumDestinationDistance(directionInfos);
//
//            Optional<Double> notMovingObstacleDirectionInfo = minDist.getCollisionDistance().getNotMovingObstacle();
//            if (notMovingObstacleDirectionInfo.isPresent()) {
//                cod.i(minDist.getCollisionDistance().getNotMovingObstacle().get());
//                if (notMovingObstacleDirectionInfo.get() < 8.5) {
//                    cod.i(pedestrianInformation.getStaticInformation().getId() + " Z "
//                            + pedestrianInformation.getVariableInformation().getVisionCenter());
//                    return getMinimumDestinationDistance(hasNotNotMovingObstacles);
//                }
//            }
////            return minDist;
//        }

        if (!freeDestinationDirections.isEmpty()) { //jeśli ograniczona lista nie jest pusta to zwracamy jej minimum

//            cod.i(pedestrianInformation.getStaticInformation().getId() + " X "
//                    + pedestrianInformation.getVariableInformation().getVisionCenter());
            return getMinimumDestinationDistance(freeDestinationDirections);
        } else if (hasNotNotMovingObstacles.isEmpty()) { //jeśli lista wszystkich kierunków bez nieruchomych przeszkód jest pusta

//            cod.i(pedestrianInformation.getStaticInformation().getId() + " A "
//                    + pedestrianInformation.getVariableInformation().getVisionCenter());
            if (getTrueInProbability(Configuration.PERCENT_PROBABILITY_OF_CHANGING_VISION_CENTER)) {
                return changeVisionCenter(directionInfos);
            } else {
                return getMinimumDestinationDistance(directionInfos);
            }
        } else if (hasNotNotMovingObstacles.size() < directionInfos.size()) { //jeśli jest mniej kierunków bez nieruchomych przeszkód niż wszystkich
            DirectionInfo minDist = getMinimumDestinationDistance(directionInfos);

            Optional<Double> notMovingObstacle = minDist.getCollisionDistance().getNotMovingObstacle();
            if (notMovingObstacle.isPresent()) { //jeśli wybrany kierunek ma nieruchomą przeszkodę
                if (notMovingObstacle.get() < Configuration.WALL_DISTANCE_TO_CHANGE_DIRECTION) { //jeśli odległość od nieruchomej przeszkody mniejsza niż odległość na zmianę kierunku
//                    cod.i(pedestrianInformation.getStaticInformation().getId() + " Z "
//                            + pedestrianInformation.getVariableInformation().getVisionCenter());
                    return getMinimumDestinationDistance(hasNotNotMovingObstacles);
                }
            }

            if (getTrueInProbability(Configuration.PERCENT_PROBABILITY_OF_COMPUTE_NOT_FOR_WALLS)) { //jeśli jakiś parametr?
                return minDist;
            } else {
                return getMinimumDestinationDistance(directionInfos);
            }
        } else {
//            cod.e(pedestrianInformation.getStaticInformation().getId() + " C " + pedestrianInformation.getVariableInformation().getVisionCenter());
            return getMinimumDestinationDistance(directionInfos);
        }
    }

    private List<DirectionInfo> getNoNotMovingObstaclesDirections(List<DirectionInfo> directionInfos) {
        return directionInfos.stream()
                .filter(di -> !di.getCollisionDistance().getNotMovingObstacle().isPresent()) // zwraca takie kierunki, które nie mają nie ruszających się przeszkód
                .collect(Collectors.toList());
    }

    private boolean getTrueInProbability(int probablity) {
        int random = rand.nextInt(100);
        boolean computeNotForWalls = random < probablity;
        return computeNotForWalls;
    }

    private DirectionInfo changeVisionCenter(List<DirectionInfo> directionInfos) {
        if (Configuration.CHANGE_VISION_CENTER_RANDOM) {
            return getRandomDirectionInfo();
        }
        return getMaximumCollisionDistance(directionInfos);
    }

    private DirectionInfo getRandomDirectionInfo() {
        double alpha = (double) rand.nextInt(RANDOM_POOL) / ((double) (RANDOM_POOL / 2));
        // cod.i("alpha: " + alpha);
        return new DirectionInfo(alpha, new MinimumDistance(Double.MAX_VALUE, Optional.empty()),
                Double.MIN_VALUE);
    }

    public DirectionInfo getMaximumCollisionDistance(List<DirectionInfo> directionInfos) {
        DirectionInfo max = Collections.max(directionInfos, new Comparator<DirectionInfo>() {
            @Override
            public int compare(DirectionInfo di1, DirectionInfo di2) {
                return di1.getCollisionDistance().getNotMovingObstacle().get()
                        .compareTo(di2.getCollisionDistance().getNotMovingObstacle().get());
            }
        });
        return max;
    }


    // to tylko wybiera minimalną wartość z listy kierunków, uwzględnić to w pętli po kierunkach!!
    public DirectionInfo getMinimumDestinationDistance(List<DirectionInfo> directionInfos) {
        DirectionInfo min = Collections.min(directionInfos, new Comparator<DirectionInfo>() {
            @Override
            public int compare(DirectionInfo di1, DirectionInfo di2) {
                return di1.getDestinationDistance().compareTo(di2.getDestinationDistance());
            }
        });
        return min;
    }

    //to plus get alpha przerobić na prostą pętlę odwołującą się do Collisions


    //to można przepisać do funkcji w których jest używane!!
    public Point getNextPosition() {
        // double desiredDirection =
        // pedestrianInformation.getVariableInformation().getDesiredDirection();
        // double desiredSpeed =
        // pedestrianInformation.getVariableInformation().getDesiredSpeed().getValue();

        Vector velocity = human.getDesiredSpeed();
        Vector acceleration = human.getDesiredAcceleration();

        /*System.out.println(velocity.getValue() + " + " + acceleration.getValue());
        System.out.println("v: " + velocity.getX() + ", " + velocity.getY());
        System.out.println("a: " + acceleration.getX() + ", " + acceleration.getY());*/

        Vector finalForce = velocity;

        /*if (Configuration.FORCES) { // rozkminić na koniec co z tym zrobić

            finalForce = Geometry.addVectors(velocity, acceleration);
        }*/

        // cod.i(pedestrianInformation.getStaticInformation().getId()+" VELO ACC FF:
        // ",Arrays.asList(velocity,acceleration, finalForce));

        Vector shift = finalForce;
        System.out.println(finalForce.getValue());

        double x = human.getPosition().getX();
        double y = human.getPosition().getY();

        return new Point(x + shift.getX(), y + shift.getY());

    }


    public Vector getDesireVelocity(double collisionDistance, double alpha, int i) {
        double comfortableSpeed = human.getComfortableSpeed();;
        // cod.i("v: "+i,Arrays.asList(goalVelocity(),
        // collisionVelocity(collisionDistance), result));
        Vector v = new Vector(alpha, Arrays.asList(goalVelocity(), collisionVelocity(collisionDistance), comfortableSpeed)
                .stream().mapToDouble(d -> d).min().getAsDouble());
        return v;
    }

    private double collisionVelocity(double collisionDistance) {
        return collisionDistance / Human.relaxationTime;
    }

    private double goalVelocity() {
        double distance = human.getPosition().distance(human.getDestination());
       return distance / Human.relaxationTime;
    }



    //FORCES:


    public List<Vector> getForces() {

        List<Vector> neighborsForces = forceCal.getForceNeighbours(human);
        List<Vector> wallForces = forceCal.getForceWalls(human);

        Vector nForce = getForcesSum(neighborsForces);
        nForce.setValue(nForce.getValue() / human.getMass());

        Vector wForce = getForcesSum(wallForces);
        wForce.setValue(wForce.getValue() / human.getMass());
        return Arrays.asList(nForce, wForce);
    }

    public double getForcesValue(List<Vector> forces) {
        double forceValue = 0;
        for (Vector force : forces) {
            forceValue = forceValue + force.getValue();
        }
        return forceValue;
    }

    public Vector getDesireAcceleration(Vector vdes, List<Vector> forces)  {
        Vector velocity = human.getDesiredSpeed();
        /*System.out.println("vdes " + vdes.getValue());
        System.out.println("v " + velocity.getValue());*/
        Vector acceleration = Geometry.subtractVectors(vdes, velocity);
        /*System.out.println("a " + acceleration.getValue());*/
        acceleration
                .setValue(acceleration.getValue() / human.relaxationTime);
        //System.out.println("a1 " + acceleration.getValue());
        //System.out.println();
        for (Vector force : forces) {
            acceleration = Geometry.addVectors(acceleration, force);

        }
        // acceleration = Geometry.addVectors(acceleration, wForce);

        // if(pedestrianInformation.getStaticInformation().getId() == 1) {
        // cod.i("ACC:", acceleration);
        // }

        //System.out.println("a2 " + acceleration.getValue());
        if (acceleration.getValue() > velocity.getValue()) {
            acceleration.setValue(velocity.getValue());
        }
        if (Configuration.MAX_ACCELERATION_VALUE < acceleration.getValue()) {
            acceleration.setValue(Configuration.MAX_ACCELERATION_VALUE);
        }

        System.out.println("af " + acceleration.getValue());

        return acceleration;
    }

    private Vector getForcesSum(List<Vector> forces)  {
        Vector force = new Vector(Double.NaN, 0.0);
        // cod.i(forces);
        for (Vector f : forces) {
            // cod.i("FORCES",crowdPressureCalforces);
            force = Geometry.addVectors(force, f);
        }
        return force;
    }

    public double getCrowdPressure(double forcesValue) {
        // cod.i(pedestrianInformation.getStaticInformation().getId());
        return crowdPressureCal.getCrowdPressure(forcesValue);
    }

}

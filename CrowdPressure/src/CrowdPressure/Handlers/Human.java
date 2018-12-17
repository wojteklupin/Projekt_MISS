package CrowdPressure.Handlers;

import CrowdPressure.Point;

import java.util.LinkedList;


public class Human {

    private final int id;
    private final Double mass;
    private final Double radius;
    private Double angleOfView;
    private Double rangeOfView;
    private Double direction;
    private Point position;
    private Point centralPoint;
    private Double desiredVelocity;
    private Double velocity;
    private Board board;
    private static final double relaxationTime = 0.5; //sedonds
    private LinkedList<Human> humansNearby = new LinkedList<>();
    private LinkedList<Wall> wallsNearby = new LinkedList<>();


    public Human(Board board, Double mass, Double velocity, Point position, Point destination){
        this.board = board;
        LinkedList<Human> humans = board.getHumans();
        this.id = (humans.isEmpty()) ? 1 : humans.getLast().getId() + 1;
        this.mass = mass;
        this.radius = mass / 320;
        this.velocity = velocity;
        this.position = position;
        this.centralPoint = destination;
        this.findObstacles();
    }

    public void findObstacles(){

        for(Human human : this.board.getHumans()) {
            if (human.getId() != this.id && Geometry.pointInside(this.position, this.centralPoint, this.angleOfView, human.getCurrentPosition())) {
                this.humansNearby.add(human);
            }
        }

        for(Wall wall : this.board.getWalls()) {
            if(Geometry.lineInside(this.position, this.centralPoint, this.angleOfView, wall.getStart(), wall.getEnd())){
                this.wallsNearby.add(wall);
        }

        }
    }

    public double minimalObstacleDistance(double angle, int direction){
        Point destination = Geometry.rotatePoint(this.centralPoint, angle, direction);
        double minDistance = this.position.distance(destination);

        for(Wall wall : this.wallsNearby){
            Point pointOfCrossing = Geometry.pointOfCrossing(this.position, destination, wall.getStart(), wall.getEnd());
            if(Geometry.contains(this.position, destination, pointOfCrossing) && Geometry.contains(wall.getStart(), wall.getEnd(), pointOfCrossing)){
                minDistance = Geometry.segmentDistanceFromPoint(wall.getStart(), wall.getEnd(), this.position) < minDistance ?
                        Geometry.segmentDistanceFromPoint(wall.getStart(), wall.getEnd(), this.position) : minDistance;
            }
        }

        for(Human human : this.humansNearby){
            Point humanPosition = human.getCurrentPosition();
            Double humanDirection = human.getDirection() >= 0 ? human.getDirection() : -1 * human.getDirection();
            Point humanDestination = Geometry.rotatePoint(humanPosition, humanDirection, human.getDirection() >= 0 ? 1 : -1);
            Point pointOfCrossing = Geometry.pointOfCrossing(this.position, destination, human.getCurrentPosition(), humanDestination);
            if(Geometry.contains(this.position, destination, pointOfCrossing) && Geometry.contains(humanPosition, humanDestination, pointOfCrossing)){

                double distanceFromCollision = this.position.distance(pointOfCrossing);
                double time = distanceFromCollision / this.velocity;

                double humanDistanceFromCollision = humanPosition.distance(pointOfCrossing);
                double humanTime = humanDistanceFromCollision / human.getVelocity();

                if(time == humanTime){
                    minDistance = distanceFromCollision < minDistance ? distanceFromCollision : minDistance;
                }
            }

        }

        return minDistance;
    }

    public double destinationDistance(double angle){
        int direction = angle >= 0 ? 1 : -1;
        double dMax = this.position.distance(this.centralPoint);
        return Math.pow(dMax, 2) + Math.pow(minimalObstacleDistance(Math.abs(angle), direction), 2) - 2 * dMax * minimalObstacleDistance(Math.abs(angle), direction) * Math.cos(this.direction - angle);
    }

    public void nextStep(){

        LinkedList<Double> results = new LinkedList<>();
        double minvalue = destinationDistance(-1 * this.angleOfView);
        double desiredDirection;

        for(double i = -1 * this.angleOfView; i < this.angleOfView; i += Math.PI / 180){
            if(this.destinationDistance(i) < minvalue) {
                minvalue = this.destinationDistance(i);
                desiredDirection = i;
            }
        }




    }




    public int getId() {
        return this.id;
    }

    public Point getCurrentPosition() {
        return this.position;
    }

    public Point getCentralPoint(){
        return this.centralPoint;
    }

    public Double getDirection(){
        return this.direction;
    }

    public Double getVelocity(){
        return this.velocity;
    }
}

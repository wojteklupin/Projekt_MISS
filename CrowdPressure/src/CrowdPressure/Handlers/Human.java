package CrowdPressure.Handlers;

import CrowdPressure.Point;

import java.util.LinkedList;
import java.util.List;


public class Human {

    private final int id;
    private final Double mass;
    private final Double radius;
    private Double angleOfView;
    private Double rangeOfView;
    private Point currentPosition;
    private Point finalDestination;
    private Point currentDestination;
    private Double defaultVelocity;
    private Double currentVelocity;
    private Double optimalDirection;
    private Board board;


    public Human(Board board, Double mass, Double velocity, Point position, Point destination){
        this.board = board;
        LinkedList<Human> humans = board.getHumans();
        this.id = (humans.isEmpty()) ? 1 : humans.getLast().getId() + 1;
        this.mass = mass;
        this.radius = mass / 300;
        this.defaultVelocity = velocity;
        this.currentPosition = position;
        this.finalDestination = destination;
    }

    public void determineNextStep(){

        LinkedList<Human> humansNearby = new LinkedList<>(); //people in our field of view

        for(Human human : this.board.getHumans()){

            if(Geometry.inside(this.currentPosition, this.currentDestination, this.angleOfView, human.getCurrentPosition())){
                humansNearby.add(human);
            }
        }

        LinkedList<Wall> wallsNearby = new LinkedList<>(); //walls in our field of view

        for(Wall wall : this.board.getWalls()){
            //need to find way if line between points a and b has part inside field of view (determined part of circle as in Geometry.inside method, code this in Geometry
        }

        //after finding walls in our area need to figure out if some is on our way

        for(Human human : humansNearby){

            if(Geometry.contains(this.currentPosition, this.currentDestination, human.getCurrentPosition())){
                //someones is currently on our way
            }

            //we need to figure out if someone can collide with us given ours and their current position direction and velocity

        }
    }

    public int getId() {
        return this.id;
    }

    public Point getCurrentPosition() {
        return this.currentPosition;
    }
}

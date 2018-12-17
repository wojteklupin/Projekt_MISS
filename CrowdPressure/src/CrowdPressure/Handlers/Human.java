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
    private Double direction;
    private Point position;
    private Point destination;
    private Double desiredVelocity;
    private Double velocity;
    private Board board;


    public Human(Board board, Double mass, Double velocity, Point position, Point destination){
        this.board = board;
        LinkedList<Human> humans = board.getHumans();
        this.id = (humans.isEmpty()) ? 1 : humans.getLast().getId() + 1;
        this.mass = mass;
        this.radius = mass / 320;
        this.velocity = velocity;
        this.position = position;
        this.destination = destination;
    }

    public void determineNextStep(){


    }

    public int getId() {
        return this.id;
    }

    public Point getCurrentPosition() {
        return this.position;
    }
}

package CrowdPressure.Handlers;

import CrowdPressure.Point;

public class Human {

    private double mass;
    private double velocity;
    private Point position;
    final private double desiredVelocity;
    final private Point destination;
    final private double radius = this.mass / 320;

    public Human(double m, double v, Point d){

        this.desiredVelocity = v;
        this.mass = m;
        this.destination = d;
    }

}

package CrowdPressure;

import java.util.List;

import CrowdPressure.Map.Point;
import CrowdPressure.Model.Forces;
import CrowdPressure.Geometry.Geometry;
import CrowdPressure.Model.RandomCollisions;
import CrowdPressure.Geometry.Vector;
import CrowdPressure.Geometry.Direction;
import CrowdPressure.Map.Board;


public class Human {

	private Board board;
	private RandomCollisions randomCollisions;
	private int id;
	private double mass;
	private double radius;
	private double comfortableSpeed;
	private double angleOfView;
	private double rangeOfView;
	public static double relaxationTime = 0.5;
	private double visionCenter;
	private double destinationAngle;
	private Point destination;
	private Point position;
	private Point nextPosition;
	private double desiredDirection;
	private Vector desiredSpeed;
	private Vector desiredAcceleration;
	private boolean finished;
	private double  crowdPressure;


	public Human(Board board, int id, int mass, double comfortableSpeed, double angleOfView, double rangeOfView, double visionCenter, Point destination, Point position){
		this.board = board;
		this.id = id;
		this.mass = mass;
		this.radius = mass / 320.0;
		this.comfortableSpeed = comfortableSpeed;
		this.angleOfView = angleOfView;
		this.rangeOfView = rangeOfView;
		this.visionCenter = visionCenter;
		this.destination = destination;
		this.position = position;
		this.nextPosition = position;
		this.destinationAngle = Geometry.angleBetween(nextPosition, destination);
		this.desiredSpeed = new Vector(desiredDirection, 0);
		this.desiredAcceleration = new Vector(Double.NaN, 0.0);
		this.crowdPressure = 1.0;
		this.finished = position.distance(destination) < 1.8;
		this.desiredDirection = visionCenter;
		this.randomCollisions = new RandomCollisions(this);
	}

	public void nextStep(){
		if(!this.isFinished()) {
			Direction desiredDirectionInfo = randomCollisions.getDirections();

			Forces forcesCalc = new Forces(this);

			double velocity = Math.min(this.comfortableSpeed, desiredDirectionInfo.getCollisionDistance() / Human.relaxationTime);
			Vector desiredVelocity = new Vector(desiredDirectionInfo.getAlpha(), velocity);

			List<Vector> forces = forcesCalc.calculateForces();
			double forcesValue = forcesCalc.sumForcesValues(forces);
			Vector desiredAcceleration = this.calculateAcceleration(desiredVelocity, forces);
			this.desiredDirection = desiredDirectionInfo.getAlpha();
			this.desiredSpeed = desiredVelocity;
			this.desiredAcceleration = desiredAcceleration;
			this.nextPosition = calculateNextPosition();
			this.destinationAngle = Geometry.angleBetween(this.nextPosition, this.destination);
			this.crowdPressure = this.getCrowdPressure(forcesValue);

			this.position = this.nextPosition;
			this.visionCenter = this.desiredDirection;
			this.finished = this.position.distance(this.destination) < 1.0;
		}
		else{
			System.out.println("I AM FINISHED, ID: " + this.id);
		}
	}

	public double getDestinationDistanceFunction(double alpha, Double collisionDistance) {

		double dangle = this.destinationAngle;
		double dmax = this.rangeOfView;
		double f = collisionDistance;
		return Math.pow(dmax,2) + Math.pow(f,2) - 2 * dmax * f * Math.cos(dangle * Math.PI - alpha * Math.PI);

	}

	public Vector calculateAcceleration(Vector velocity,List<Vector> forces){

		Vector acceleration = Geometry.subtractVectors(velocity, this.desiredSpeed);
		acceleration.setValue(Geometry.subtractVectors(velocity,this.desiredSpeed).getValue() / Human.relaxationTime);

		for (Vector force : forces) {
			acceleration = Geometry.addVectors(acceleration, force);

		}

		if (acceleration.getValue() > velocity.getValue()) {
			acceleration.setValue(velocity.getValue());
		}

		if (0.5 < acceleration.getValue()) {
			acceleration.setValue(0.5);
		}

		return acceleration;
	}


	Point calculateNextPosition(){
		Vector velocity = this.desiredSpeed;
		Vector acceleration = this.desiredAcceleration;

		Vector finalForce = velocity;

		Vector shift = finalForce;

		double x = this.position.getX();
		double y = this.position.getY();

		return new Point(x + shift.getX(), y + shift.getY());
	}

	public double getCrowdPressure(double forcesValue) {

		double minimalAcceleration = 0.0;
		double maximalCPValue = 1.0;

		double crowdPressure = 0.0;

		if (forcesValue > minimalAcceleration) {
			crowdPressure = forcesValue * 3;
		} else {
			crowdPressure = 0.0;
		}
		if (crowdPressure > maximalCPValue) {
			crowdPressure = maximalCPValue;
		}

		return crowdPressure;
	}


	@Override
	public String toString(){
		String p = "Human: \nId: " + this.id + " | Mass: " + this.mass + " | Radius: " + this.radius + "\n";
		p += "Position: (" + this.position.getX() + ", " + this.position.getY() + ") | Destination: (" + + this.destination.getX() + ", " + this.destination.getY() + ")\n";

		return p;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public double getRangeOfView() {
		return rangeOfView;
	}

	public int getId() {
		return id;
	}

	public Point getPosition() {
		return position;
	}

	public double getRadius() {
		return radius;
	}

	public Point getDestination() {
		return destination;
	}

	public Point getNextPosition() {
		return nextPosition;
	}

	public double getAngleOfView() {
		return angleOfView;
	}

	public double getComfortableSpeed() {
		return comfortableSpeed;
	}

	public double getCrowdPressure() {
		return crowdPressure;
	}

	public double getDesiredDirection() {
		return desiredDirection;
	}

	public double getDestinationAngle() {
		return destinationAngle;
	}

	public double getMass() {
		return mass;
	}

	public double getVisionCenter() {
		return visionCenter;
	}

	public Vector getDesiredAcceleration() {
		return desiredAcceleration;
	}

	public Vector getDesiredSpeed() {
		return desiredSpeed;
	}

	public boolean isFinished() {
		return finished;
	}
}

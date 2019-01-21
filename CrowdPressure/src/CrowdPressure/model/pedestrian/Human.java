package CrowdPressure.model.pedestrian;

import java.util.List;

import CrowdPressure.Configuration;
import CrowdPressure.Point;
import CrowdPressure.calculators.Forces;
import CrowdPressure.calculators.Geometry;
import CrowdPressure.calculators.PedestrianCalculator;
import CrowdPressure.calculators.figures.Vector;
import CrowdPressure.model.DirectionInfo;
import CrowdPressure.model.Board;


public class Human {

	private Board board;
	private PedestrianCalculator pedestrianCalculator;
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
		this.destinationAngle = Geometry.calculateAngle(nextPosition, destination);
		this.desiredSpeed = new Vector(desiredDirection, 0);
		this.desiredAcceleration = new Vector(Double.NaN, 0.0);
		this.crowdPressure = 1.0;
		this.finished = Geometry.isBigger(position.distance(destination), Configuration.MAX_DISTANCE_TO_GOAL);
		this.desiredDirection = visionCenter;
		this.pedestrianCalculator = new PedestrianCalculator(this, board);
	}

	public void nextStep(){
		if(!this.isFinished()) {
			DirectionInfo desiredDirectionInfo = pedestrianCalculator.getDirectionInfo();
			System.out.println(desiredDirectionInfo.getAlpha());

			Forces forcesCalc = new Forces(this);

			double velocity = Math.min(this.comfortableSpeed, desiredDirectionInfo.getCollisionDistance().getMinimumDistance() / Human.relaxationTime); //była jeszcze prędkość z odległóścią od przeszkody
			Vector desiredVelocity = new Vector(desiredDirectionInfo.getAlpha(), velocity);

			List<Vector> forces = forcesCalc.calculateForces(); //pedestrianCalculator.getForces();
			double forcesValue = forcesCalc.sumForcesValues(forces); //pedestrianCalculator.getForcesValue(forces);
			Vector desiredAcceleration = this.calculateAcceleration(desiredVelocity, forces);
			this.desiredDirection = desiredDirectionInfo.getAlpha();
			this.desiredSpeed = desiredVelocity;
			this.desiredAcceleration = desiredAcceleration;
			this.nextPosition = pedestrianCalculator.getNextPosition();
			this.destinationAngle = Geometry.calculateAngle(this.nextPosition, this.destination);
			this.crowdPressure = pedestrianCalculator.getCrowdPressure(forcesValue);

			this.position = this.nextPosition;
			this.visionCenter = this.desiredDirection;
			this.finished = Geometry.isBigger(this.position.distance(this.destination), 1.0);
			//System.out.println(this.finished);
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
		acceleration.setValue((velocity.getValue() - this.desiredSpeed.getValue()) / Human.relaxationTime);

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

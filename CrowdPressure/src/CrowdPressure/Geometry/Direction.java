package CrowdPressure.Geometry;


import java.util.Optional;

public class Direction {
	private double alpha;
	private Double collisionDistance;
	private Double destinationDistance;
	private Optional<Double> wallObsacleDistance;

	public Direction(double alpha, Double collisionDistanceValue, Optional<Double> dist, Double destinationDistance) {
		super();
		this.alpha = alpha;
		this.collisionDistance = collisionDistanceValue;
		this.wallObsacleDistance = dist;
		this.destinationDistance = destinationDistance;
	}


	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public Double getDestinationDistance() {
		return destinationDistance;
	}

	public void setDestinationDistance(Double destinationDistance) {
		this.destinationDistance = destinationDistance;
	}

	public double getCollisionDistance() {
		return collisionDistance;
	}

	public void setCollisionDistance(double collisionDistance) {
		this.collisionDistance = collisionDistance;
	}

	public Optional<Double> getWallObsacleDistance() {
		return wallObsacleDistance;
	}

	public void setWallObsacleDistance(Optional<Double> d){
		this.wallObsacleDistance = d;
	}
}

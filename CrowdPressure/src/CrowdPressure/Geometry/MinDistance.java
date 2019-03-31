package CrowdPressure.Geometry;

import java.util.Optional;

public class MinDistance {

	private Double minimumDistance;
	private Optional<Double> wallDistance; //?????
	

	public MinDistance(Double minimumDistance, Optional<Double> notMovingObstacle) {
		super();
		this.minimumDistance = minimumDistance;
		this.wallDistance = notMovingObstacle;
	}

	public Double getMinimumDistance() {
		return minimumDistance;
	}

	public void setMinimumDistance(Double minimumDistance) {
		this.minimumDistance = minimumDistance;
	}

	public Optional<Double> getWallDistance() {
		return wallDistance;
	}

	public void setWallDistance(Optional<Double> notMovingObstacle) {
		this.wallDistance = notMovingObstacle;
	}

}

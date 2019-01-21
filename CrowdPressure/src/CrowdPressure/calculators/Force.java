package CrowdPressure.calculators;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import CrowdPressure.calculators.figures.LineTwoPoints;
import CrowdPressure.calculators.figures.Vector;
import CrowdPressure.model.Board;
import CrowdPressure.model.map.Wall;
import CrowdPressure.model.pedestrian.Human;
import CrowdPressure.Configuration;

public class Force {
	private Board board;

	public Force(Board board) {
		this.board = board;
	}

	public List<Vector> getForceNeighbours(Human human) {
		List<Vector> forces = new ArrayList<>();
		for (Human p : board.getHumans()) {
			if (p.getId() != human.getId()) {
				Optional<Vector> oForce = calculateForce(human, p);
				if (oForce.isPresent()) {
					forces.add(oForce.get());
				}
			}
		}
		return forces;
	}

	private Optional<Vector> calculateForce(Human human, Human neighbor) {
		
		Double radiusSum = human.getRadius()
				+ neighbor.getRadius();


		
//		cod.i("POS", Arrays.asList(pedestrianInfo.getVariableInformation().getPosition(),
//				neighborInfo.getVariableInformation().getPosition()));
		Double distance = human.getPosition().distance(neighbor.getPosition());

		Double force = (radiusSum - distance);	
//		cod.i(pedestrianInfo.getStaticInformation().getId()+" RS, D, F", Arrays.asList(radiusSum, distance, force));
		force = force * Configuration.K_PARAMETER;
		if (force > 0) {
			Vector direction = Geometry.subtractVectors(
					new Vector(human.getPosition()),
					new Vector(neighbor.getPosition()));
			Vector forceVector = direction;
			forceVector.setValue(force);
			return Optional.of(forceVector);
		}
		return Optional.empty();
	}

	public List<Vector> getForceWalls(Human human) {
		List<Vector> forces = new ArrayList<>();
		for (Wall w : board.getWalls()) {
				Optional<Vector> oForce = calculateForce(human, w);
				if (oForce.isPresent()) {
					forces.add(oForce.get());
				}

		}
		return forces;
	}

	private Optional<Vector> calculateForce(Human human, Wall wall) {
		Optional<Vector> optionalVector = Geometry.vectorStraightPoint(human.getPosition(), new LineTwoPoints(wall.getStartPosition(), wall.getEndPosition()));
		
		
		
		
		
		if (optionalVector.isPresent()) {
			Vector forceVector = optionalVector.get();
			Double force = human.getRadius() - forceVector.getValue();
			if (force > 0) {

				force = force * Configuration.K_PARAMETER;

				forceVector.setValue(force);

				// double neutral = 1;
				// Vector direction = new Vector(
				// (wall.getStartPosition().getX() * neutral) / wall.getStartPosition().getY(),
				// neutral);
				// Vector forceVector = Geometry.changeVector(direction);
				// forceVector.setValue(force);
				return Optional.of(forceVector);
			}
		}
		return Optional.empty();
	}

}

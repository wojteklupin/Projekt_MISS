package CrowdPressure.calculators.figures;

import CrowdPressure.Point;

public class LineTwoPoints {
	private Point startPosition;
	private Point endPosition;

	public LineTwoPoints(Point startPosition, Point endPosition) {
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}

	public Point getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(Point startPosition) {
		this.startPosition = startPosition;
	}

	public Point getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(Point endPosition) {
		this.endPosition = endPosition;
	}
}

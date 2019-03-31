package CrowdPressure.calculators.figures;

import CrowdPressure.Point;

public class LinePointAngle {
	private Point point;
	private double angle;

	public LinePointAngle(Point point, double angle) {
		super();
		this.point = point;
		this.angle = angle;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

}

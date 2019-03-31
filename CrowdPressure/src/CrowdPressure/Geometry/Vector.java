package CrowdPressure.Geometry;

import CrowdPressure.Map.Point;

public class Vector {
	private double x;
	private double y;
	private double angle;
	private double value;

	public Vector(Double angle, double value) {
		this.angle = angle;
		this.value = value;


		double alpha = angle * Math.PI;

		if(angle == 2.0){
			angle = 0.0;
		}

        if (angle < 0.5) {
			this.x = value * Math.cos(alpha);
			this.y =  value * Math.sin(alpha);
		}
		else if (angle == 0.5) {
			this.x = 0;
			this.y = value;
		}
		else if (angle <= 1.0) {
        	this.x = -value * Math.cos(Math.PI - alpha);
        	this.y = value * Math.sin(Math.PI - alpha);
        }
        else if (angle < 1.5) {
			this.x = -value * Math.cos(alpha - Math.PI);
			this.y = -value * Math.sin(alpha - Math.PI);
		}
		else if (angle == 1.5) {
			this.x = 0;
			this.y = -value;
		}
		else if (angle < 2.0) {
			this.x = value * Math.cos(2 * Math.PI - alpha);
			this.y = -value * Math.sin(2 * Math.PI - alpha);
		}
		else if (angle.isNaN()) {
			this.x = 0;
			this.y = 0;
		}


	}

	public Vector(Point coords){

		this.x = coords.getX();
		this.y = coords.getY();

		this.value = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
		this.angle = Geometry.angleBetween(new Point(0,0), new Point(this.x, this.y));
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getValue(){
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}

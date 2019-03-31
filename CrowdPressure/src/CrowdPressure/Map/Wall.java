package CrowdPressure.Map;

public class Wall {
	private Point startPosition;
	private Point endPosition;

	public Wall(Point startPosition, Point endPosition) {
		super();
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

	@Override
	public String toString() {
		return "Wall(start(" + this.getStartPosition().getX() + ", " + this.getStartPosition().getY() +
				"), end(" + this.getEndPosition().getX() + ", " + this.getEndPosition().getY() + "\n";
	}
}

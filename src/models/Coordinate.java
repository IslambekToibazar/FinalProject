package models;

public class Coordinate {
	private double x;
	private double y;

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate getMoveDelta(double dx, double dy) {
		return new Coordinate(this.x + dx, this.y + dy);
	}

	public Coordinate getMoveVelocity(double angle, double speed) {
		return new Coordinate(this.x + (Math.cos(angle) * speed), this.y + Math.sin(angle) * speed);
	}

}

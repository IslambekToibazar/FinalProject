package models;

public class Packet extends Rectangle {
	private double speed;
	private boolean consumed;

	protected Packet(Coordinate topLeft, double width, double height, RectangleType type, double speed, boolean consumed) {
		super(topLeft, width, height, type);
		this.speed = speed;
		this.consumed=consumed;
	}

	protected Packet(Coordinate topLeft, double width, double height, int id, RectangleType type, double speed, boolean consumed) {
		super(topLeft, width, height, id, type);
		this.speed = speed;
		this.consumed=consumed;
	}

	public Packet setSpeed(double speed) {
		return new Packet(this.getTopLeftCoordinate(), this.getWidth(), this.getHeight(), this.getType(), speed,consumed);
	}

	public Packet getMove() {
		return new Packet(this.getTopLeftCoordinate().getMoveVelocity(Math.toRadians(90), speed), this.getWidth(), this.getHeight(),
				this.getId(), this.getType(), speed,consumed);
	}
	public Packet setConsumed(boolean consumed){
		return new Packet(this.getTopLeftCoordinate(), this.getWidth(), this.getHeight(),
				this.getId(), this.getType(), speed,consumed);
	}

	public boolean isConsumed() {
		return consumed;
	}

	public double getSpeed() {
		return speed;
	}

}

package models;

public class PhotonBullet extends Rectangle {
	private int speed = 2;

	protected PhotonBullet(Coordinate topLeft, double width, double height, RectangleType type) {
		super(topLeft, width, height, type);
	}

	protected PhotonBullet(Coordinate topLeft, double width, double height, int id, RectangleType type) {
		super(topLeft, width, height, id, type);
	}

	public PhotonBullet getMove() {
		return new PhotonBullet(this.getTopLeftCoordinate().getMoveVelocity(Math.toRadians(270), speed),
				this.getWidth(), this.getHeight(), this.getId(), this.getType());
	}
	public PhotonBullet kill() {
		Coordinate killCoordinate = new Coordinate(0,0);
		return new PhotonBullet(killCoordinate,
				this.getWidth(), this.getHeight(), this.getId(), this.getType());
	}

}

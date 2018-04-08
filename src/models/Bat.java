package models;

public class Bat extends Rectangle{
	protected Bat(Coordinate topLeft, double width, double height, RectangleType type) {
		super(topLeft, width, height, type);
	}

	protected Bat(Coordinate topLeft, double width, double height, int id, RectangleType type) {
		super(topLeft, width, height, id, type);
	}
	protected Bat(Coordinate topLeft, Coordinate bottomRight, int id, RectangleType type) {
		super(topLeft,bottomRight,id,type);
	}
	public Bat createMove(double dx, double dy) {
		return new Bat(this.getTopLeftCoordinate().getMoveDelta(dx, dy), this.getBottomRightCoordinate().getMoveDelta(dx, dy), this.getId(), this.getType());
	}

}

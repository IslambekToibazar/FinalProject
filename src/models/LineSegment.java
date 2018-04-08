package models;

public class LineSegment {

	private Coordinate a;
	private Coordinate b;

	public LineSegment(Coordinate a, Coordinate b) {
		this.a = a;
		this.b = b;
	}

	public Coordinate getA() {
		return a;
	}

	public Coordinate getB() {
		return b;
	}

}

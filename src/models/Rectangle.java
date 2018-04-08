package models;

public class Rectangle {
	protected static int count;
	private RectangleType type;
	private int id;
	private Coordinate topLeft;
	private Coordinate bottomRight;
	private Coordinate topRight;
	private Coordinate bottomLeft;
	private Coordinate center;

	private LineSegment rightLine;
	private LineSegment leftLine;
	private LineSegment topLine;
	private LineSegment bottomLine;

	private double width;
	private double height;

	protected Rectangle(Coordinate topLeft, Coordinate bottomRight, int id, RectangleType type) {
		this.id = id;
		this.type = type;

		this.topLeft = topLeft;
		this.bottomRight = bottomRight;

		this.width=Math.abs(topLeft.getX() - bottomRight.getX());
		this.height=Math.abs(topLeft.getY() - bottomRight.getY());

		this.topRight=new Coordinate(topLeft.getX() + width, topLeft.getY());
		this.bottomLeft=new Coordinate(topLeft.getX(), topLeft.getY() + height);
		this.center=new Coordinate(topLeft.getX() + (width / 2),
				topLeft.getY() + (height / 2));

		this.rightLine=new LineSegment(topRight, bottomRight);
		this.leftLine=new LineSegment(topLeft, bottomLeft);
		this.topLine=new LineSegment(topLeft, topRight);
		this.bottomLine = new LineSegment(bottomLeft, bottomRight);
	}

	// Alternative to the main constructor, preserves id
	protected Rectangle(Coordinate topLeft, double width, double height, int id, RectangleType type) {
		this(topLeft, topLeft.getMoveDelta(width, height), id, type);
	}

	public Rectangle(Coordinate topLeft, Coordinate bottomRight, RectangleType type) {
		this(topLeft, bottomRight, count++, type);
	}

	
	public Rectangle(Coordinate topLeft, double width, double height, RectangleType type) {
		this(topLeft, topLeft.getMoveDelta(width, height), type);
	}

	public Rectangle(Runnable doesSomething) {

	}

    public Rectangle createMove(double dx, double dy, RectangleType type) {
		return new Rectangle(topLeft.getMoveDelta(dx, dy), bottomRight.getMoveDelta(dx, dy), this.id, type);
	}

	public Coordinate getTopLeftCoordinate() {
		return topLeft;
	}

	public Coordinate getBottomRightCoordinate() {
		return bottomRight;
	}

	public Coordinate getTopRightCoordinate() {
		return topRight;
	}

	public Coordinate getBottomLeftCoordinate() {
		return bottomLeft;
	}

	public Coordinate getCenterCoordinate() {
		return center;
	}

	public LineSegment getTopLineSegment() {
		return topLine;
	}

	public LineSegment getRightLineSegment() {
		return rightLine;
	}

	public LineSegment getBottomLineSegment() {
		return bottomLine;
	}

	public LineSegment getLeftLineSegment() {
		return leftLine;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getPercentCoordinate(double percentage) {
		return this.getTopLeftCoordinate().getX() + ((percentage / 100) * this.getWidth());
	}

	public int getId() {
		return id;
	}

	public RectangleType getType() {
		return type;
	}

}

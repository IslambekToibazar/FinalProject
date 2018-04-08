package models;

import sample.TheController;

public class Brick extends Rectangle {
	private int hitCount = 0;
	private int hitResistance = 1;
	private boolean isAlive = true;

	// Main constructor
	protected Brick(Coordinate topLeft, Coordinate bottomRight, int id, RectangleType type, int hitCount,
			int hitResistance) {
		super(topLeft, bottomRight, id, type);
		this.hitCount = hitCount;
		this.hitResistance = hitResistance;
	}

	// Same as the one above but you can change isAlive
	protected Brick(Coordinate topLeft, Coordinate bottomRight, int id, RectangleType type, int hitCount,
			int hitResistance, boolean isAlive) {
		super(topLeft, bottomRight, id, type);
		this.hitCount = hitCount;
		this.hitResistance = hitResistance;
		this.isAlive = isAlive;
	}

	// Alternative to the main constructor, preserves id
	protected Brick(Coordinate topLeft, double width, double height, int id, RectangleType type, int hitCount) {
		super(topLeft, topLeft.getMoveDelta(width, height), id, type);
		this.hitCount = hitCount;
	}

	// For creating brand new rectangles
	public Brick(Coordinate topLeft, Coordinate bottomRight, RectangleType type, int hitCount, int hitResistance) {
		super(topLeft, bottomRight, count++, type);
		this.hitCount = hitCount;
		this.hitResistance = hitResistance;
	}

	// For creating brand new rectangles as well
	public Brick(Coordinate topLeft, double width, double height, RectangleType type, int hitCount, int hitResistance) {
		super(topLeft, topLeft.getMoveDelta(width, height), type);
		this.hitCount = hitCount;
		this.hitResistance = hitResistance;
	}

	public Brick hitBrick() {
		RectangleType nextType=this.getType();
		if(this.getType()==RectangleType.ThreeBrick1){
			nextType=RectangleType.TwoBrick1;
			TheController.score += 10;
		}
		else if(this.getType()==RectangleType.TwoBrick1){
			nextType=RectangleType.OneBrick1;
			TheController.score += 15;
		}
		return new Brick(this.getTopLeftCoordinate(), this.getBottomRightCoordinate(), this.getId(), nextType,
				this.hitCount + 1, this.hitResistance);
	}

	public int getHitCount() {
		return hitCount;
	}

	public int getHitResistance() {
		return hitResistance;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public Brick kill() {
		return new Brick(this.getTopLeftCoordinate(), this.getBottomRightCoordinate(), this.getId(), this.getType(),
				this.getHitResistance(), this.getHitResistance(), false);
	}

}

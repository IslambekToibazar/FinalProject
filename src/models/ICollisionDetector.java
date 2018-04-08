package models;

public interface ICollisionDetector {
	boolean intersects(LineSegment one, LineSegment two);

	boolean basicIntersects(Rectangle r1, Rectangle r2);
}
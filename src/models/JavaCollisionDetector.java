package models;

import java.awt.geom.Line2D;

public class JavaCollisionDetector implements ICollisionDetector {
	@Override
	public boolean intersects(LineSegment one, LineSegment two) {
		Line2D line1 = new Line2D.Double(one.getA().getX(), one.getA().getY(), one.getB().getX(), one.getB().getY());
		Line2D line2 = new Line2D.Double(two.getA().getX(), two.getA().getY(), two.getB().getX(), two.getB().getY());
		boolean result = line2.intersectsLine(line1);
		return result;
	}

	public boolean basicIntersects(Rectangle r1, Rectangle r2) {
		if (r1.getTopLeftCoordinate().getX() < r2.getTopLeftCoordinate().getX() + r2.getWidth()
				&& r1.getTopLeftCoordinate().getX() + r1.getWidth() > r2.getTopLeftCoordinate().getX()
				&& r1.getTopLeftCoordinate().getY() < r2.getTopLeftCoordinate().getY() + r2.getHeight()
				&& r1.getHeight() + r1.getTopLeftCoordinate().getY() > r2.getTopLeftCoordinate().getY()) {
			return true;
		} else
			return false;
	}
}

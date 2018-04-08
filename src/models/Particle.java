package models;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Particle {
	private double x;
	private double y;

	private Point2D velocity;

	private double radius;
	double life = 1.0;
	private double decay;

	private Paint color;
	public Particle(double x, double y, Point2D velocity, double radius, double expireTime, Paint color) {
		this.x = x;
		this.y = y;
		this.velocity = velocity;
		this.radius = radius;
		this.color = color;

		this.decay = 0.016/expireTime;
	}

	public boolean isAlive(){
		return life > 0;
	}

	public void update(){
		x+=velocity.getX();
		y+=velocity.getY();
		life-=decay;
	}
	public Shape render(){
		Circle graphicalCircle = new Circle();
		graphicalCircle.setTranslateX(x);
		graphicalCircle.setTranslateY(y);
		graphicalCircle.setRadius(radius);
		graphicalCircle.setOpacity(life);
		graphicalCircle.setFill(color);
		graphicalCircle.setBlendMode(BlendMode.MULTIPLY);

		javafx.scene.shape.Rectangle graphicalRectangle = new javafx.scene.shape.Rectangle();
		graphicalRectangle.setTranslateX(x);
		graphicalRectangle.setTranslateY(y);
		graphicalRectangle.setWidth(radius);
		graphicalRectangle.setHeight(radius);
		graphicalRectangle.setOpacity(life);
		graphicalRectangle.setFill(color);
		graphicalRectangle.setBlendMode(BlendMode.MULTIPLY);

		return graphicalCircle;
	}


}

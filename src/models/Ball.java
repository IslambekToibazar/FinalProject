package models;

public class Ball extends Rectangle {
	private double speed;
	private double angleOfMovement;
	private boolean canUseBat;
	private int xScore;
	private int yScore;
	private Brick brickItHit;
	private boolean isUnstoppable = false;

	// For creating a brand new ball
	public Ball(Coordinate topLeft, double width, double height, double speed, double angleOfMovement,
			RectangleType type) {
		super(topLeft, width, height, type);
		this.speed = speed;
		this.angleOfMovement = angleOfMovement;
	}

	public Ball(Coordinate topLeft, double width, double height, RectangleType type, boolean canUseBat, Brick brickItHit, int xScore, int yScore, int id, boolean unstoppableOn, double angleOfMovement, double speed) {
		super(topLeft, width, height,id, type);
		this.canUseBat = canUseBat;
		this.brickItHit=brickItHit;
		this.xScore=xScore;
		this.yScore=yScore;
		this.isUnstoppable=unstoppableOn;
		this.angleOfMovement=angleOfMovement;
		this.speed=speed;
	}

	public Ball setUnstoppable(boolean isUnstoppable){
		return new Ball(this.getTopLeftCoordinate(), this.getWidth(), this.getHeight(), this.getType(), this.canUseBat, this.brickItHit, this.xScore,this.yScore,this.getId(),isUnstoppable,this.getAngleOfMovement(),this.getSpeed());
	}
	public Ball setSize(double size){
		return new Ball(this.getTopLeftCoordinate(), size, size, this.getType(), this.canUseBat, this.brickItHit, this.xScore,this.yScore,this.getId(),isUnstoppable,this.getAngleOfMovement(),this.getSpeed());
	}

	public Ball setScore(int xScore, int yScore) {
		return new Ball(this.getTopLeftCoordinate(), this.getWidth(), this.getHeight(), this.getType(), this.canUseBat,
				  brickItHit, xScore,yScore,this.getId(),this.isUnstoppable,this.getAngleOfMovement(),this.getSpeed());
	}

	public Ball setBrickItHit(Brick brickItHit) {
		return new Ball(this.getTopLeftCoordinate(), this.getWidth(), this.getHeight(), this.getType(), this.canUseBat,
				  brickItHit, this.xScore,this.yScore,this.getId(),this.isUnstoppable,this.getAngleOfMovement(),this.getSpeed());
	}
	public Ball setCanUseBat(boolean canUseBat) {
		return new Ball(this.getTopLeftCoordinate(), this.getWidth(), this.getHeight(), this.getType(), canUseBat,
				  brickItHit, xScore,yScore,this.getId(),this.isUnstoppable,this.getAngleOfMovement(),this.getSpeed());
	}
	public Ball getMove() {
		return new Ball(this.getTopLeftCoordinate().getMoveVelocity(angleOfMovement, speed), this.getWidth(), this.getHeight(), this.getType(), this.canUseBat,
			  this.brickItHit, this.xScore,this.yScore,this.getId(),this.isUnstoppable,
				this.getAngleOfMovement(),this.getSpeed());
	}

	public Ball flipXDirection() {
		double cos = Math.cos(angleOfMovement);
		double sin = Math.sin(angleOfMovement);
		double newAngle = Math.atan2(sin, -cos);
		return new Ball(this.getTopLeftCoordinate(), this.getWidth(), this.getHeight(), this.getType(), this.canUseBat,
				  this.brickItHit, this.xScore,this.yScore,this.getId(),this.isUnstoppable,newAngle,this.getSpeed());
	}

	public Ball flipYDirection() {
		double cos = Math.cos(angleOfMovement);
		double sin = Math.sin(angleOfMovement);
		double newAngle = Math.atan2(-sin, cos);
		return new Ball(this.getTopLeftCoordinate(), this.getWidth(), this.getHeight(), this.getType(), this.canUseBat,
				 this.brickItHit, this.xScore,this.yScore,this.getId(),this.isUnstoppable,newAngle,this.getSpeed());
	}

	public Ball changeAngleDegrees(double changeAmount) {
		return new Ball(this.getTopLeftCoordinate(), this.getWidth(), this.getHeight(), this.getType(), this.canUseBat,
				  this.brickItHit, this.xScore,this.yScore,this.getId(),this.isUnstoppable,(this.angleOfMovement + Math.toRadians(changeAmount)),this.getSpeed());
	}

	public Ball setPosition(double setX, double setY) {
		return new Ball(new Coordinate(setX,setY), this.getWidth(), this.getHeight(), this.getType(), this.canUseBat,
				  this.brickItHit, this.xScore,this.yScore,this.getId(),this.isUnstoppable,this.angleOfMovement,this.getSpeed());
	}

	public Ball setAngleInDegrees(double changeAmount) {
		return new Ball(this.getTopLeftCoordinate(), this.getWidth(), this.getHeight(), this.getType(), this.canUseBat,
				 this.brickItHit, this.xScore,this.yScore,this.getId(),this.isUnstoppable,(Math.toRadians(changeAmount)),this.getSpeed());
	}
	public Ball setSpeed(double speed){
		return new Ball(this.getTopLeftCoordinate(), this.getWidth(), this.getHeight(), this.getType(), this.canUseBat,
				 this.brickItHit, this.xScore,this.yScore,this.getId(),this.isUnstoppable,angleOfMovement,speed);
	}

	public double calculatePercentageOffsetWith(Rectangle r) {
		// Formula is (centerCoordinate of ball / (1/2) r's width)*50 =
		// percentage offset from -50% to +50%
		double offset = this.getCenterCoordinate().getX() - r.getCenterCoordinate().getX();
		return (offset / (0.5 * r.getWidth())) * 50;
	}

	public double getAngleOfMovement() {
		return angleOfMovement;
	}

	public double getAngleInDegrees() {
		return Math.toDegrees(angleOfMovement);
	}

	public double getSpeed() {
		return speed;
	}

	public int getXScore() {
		return xScore;
	}

	public int getYScore() {
		return yScore;
	}

	public boolean getCanUseBat() {
		return canUseBat;
	}

	public Brick getBrickItHit() {
		return brickItHit;
	}

	public boolean isUnstoppable() {
		return isUnstoppable;
	}

}

package models;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import sample.Levels;
import sample.TheController;

public class GameBoardModel {
	private Random random;
	private int levelNum;
	private int fireCount = 0;
	private int gunCount = 0;
	private final static int EXPLOSION_RADIUS = 85;
	private final static int FIRE_INTERVAL_TIME = 300;
	private final static int GUN_TIME = 1200;
	private final static double LEFT_ANGLE_LIMIT = -35;
	private final static double RIGHT_ANGLE_LIMIT = -150;
	private int BAT_SPEED;
	private final static int PACKET_ODDS = 7;
	private double BALL_SPEED;
	private final static double BALL_SIZE = 15;
	private final static double PACKET_SIZE = 18;
	private final static double PACKET_SPEED = 1;
	public final static double UNSTOPPABLE_BALL_SIZE = BALL_SIZE;
	private final static double BALL_SPEED_INCREASE = 0.17;
	private double BAT_WIDTH = 120;
	private double BAT_HEIGHT = 50;

	private int brickHeight; // Similar to brickRowHeight but gap is included
	private int columnWidth;
	private int brickWidth;
	private Rectangle windowRectangle;
	private Bat bat;
	private PhotonBlasters photonBlasters;
	private LinkedList<Ball> balls = new LinkedList<Ball>();
	private LinkedList<Brick> bricks = new LinkedList<Brick>();
	private LinkedList<Packet> packets = new LinkedList<Packet>();
	private LinkedList<PhotonBullet> photonBullets = new LinkedList<PhotonBullet>();

	private ICollisionDetector detector;

	private IntegerProperty attemptedBatPosX = new SimpleIntegerProperty(0);

	public GameBoardModel(int levelNum, int brickRowHeight, int brickGapH, int brickGapV, int gapAboveBricks,
			int brickColumns, int brickRows, int batSpeed, double ballSpeed, ICollisionDetector detector) {
		this.random = new Random();
		this.levelNum = levelNum;
		this.detector = detector;

		this.attemptedBatPosX.addListener((o, oldVal, newVal) -> dragBat(attemptedBatPosX.getValue()));

		this.windowRectangle = new Rectangle(new Coordinate(0, 0), TheController.getBoardWidth(),
				TheController.getBoardHeight(), RectangleType.Window);

		if (brickColumns != 0)
			this.columnWidth = (TheController.getBoardWidth() - 10) / brickColumns;
		this.brickWidth = columnWidth - brickGapH;
		this.brickHeight = brickRowHeight - brickGapV;
		this.BAT_SPEED = batSpeed;
		this.BALL_SPEED = ballSpeed; // An optional variable to set the all the
										// balls to the same speed
		// Brick creation algorithm
		for (int column = 0; column < brickColumns; column++) {
			for (int row = 0; row < brickRows; row++) {
				// The 4+ is so the bricks do not appear right on the edges
				Brick r = new Brick(
						new Coordinate(4 + (column * columnWidth) + (brickGapH / 2),
								(row * brickRowHeight) + gapAboveBricks),
						brickWidth, brickHeight, RectangleType.ThreeBrick1, 0, 3);
				bricks.add(r);
			}
		}
		// Brick creation algorithm end

		// DO NOT MAKE ANGLE LESS THAN 0 OR GREATER THAN 2PI
		Coordinate batUL = new Coordinate((TheController.getBoardWidth() - BAT_WIDTH) / 2,
				(TheController.getBoardHeight() - BAT_HEIGHT - 80));
		bat = new Bat(batUL, BAT_WIDTH, BAT_HEIGHT, RectangleType.Bat);
		balls.add(new Ball(new Coordinate(300, TheController.getBoardHeight() / 2 + 200), BALL_SIZE, BALL_SIZE,
				BALL_SPEED, 4, RectangleType.Ball));
		makeBallsUnstoppable();

		// balls.add(new Ball(new Coordinate(300, TheController.getBoardHeight()
		// / 2 + 100), BALL_SIZE, BALL_SIZE,
		// BALL_SPEED, Math.toRadians(154), RectangleType.Ball));
		// balls.add(new Ball(new Coordinate(300, TheController.getBoardHeight()
		// / 2 + 100), BALL_SIZE, BALL_SIZE,
		// BALL_SPEED, Math.toRadians(150), RectangleType.Ball));
		photonBlasters = new PhotonBlasters(bat.getWidth(), bat.getTopLeftCoordinate()).setTurnedOn(true);
	}

	// Updating Ball and Destroying bricks
	private static Ball calculateBallHitBricks(Ball ball, LinkedList<Brick> bricks, ICollisionDetector detector,
			boolean unstoppableOn) {
		Ball nextBall = ball.getMove();
		Optional<Double> r = bricks.stream().map(i -> i.getBottomRightCoordinate().getY()).max(Double::compare);

		@SuppressWarnings("unchecked")
		LinkedList<Brick> copyBricks = (LinkedList<Brick>) bricks.clone();
		if (bricks.size() > 0) {
			if (nextBall.getTopLeftCoordinate().getY() <= r.get()) {
				for (Brick brick : copyBricks) {
					if (brick.isAlive() && detector.basicIntersects(nextBall, brick)) {
						ball = ball.setBrickItHit(brick).setSpeed(ball.getSpeed() + BALL_SPEED_INCREASE);
						int xScore = xScore(brick, ball, detector);
						int yScore = yScore(brick, ball, detector);
						if (!unstoppableOn) {
							if (xScore > yScore) {
								return ball.getMove().flipXDirection();
							} else if (yScore > xScore) {
								return ball.getMove().flipYDirection();

							} else if (xScore == yScore) {
								System.out.println("XY");
								return ball.getMove().flipYDirection();
							}
						}
					}
				}
			}

		}
		return ball;
	}

	private static Ball calculateBallHitWall(Ball ball, Rectangle windowRectangle, ICollisionDetector detector) {
		Ball nextBall = ball.getMove();
		LineSegment ballPathCenter = new LineSegment(ball.getCenterCoordinate(), nextBall.getCenterCoordinate());
		LineSegment ballPathTopLeft = new LineSegment(ball.getTopLeftCoordinate(), nextBall.getTopLeftCoordinate());
		LineSegment ballPathBottomRight = new LineSegment(ball.getBottomRightCoordinate(),
				nextBall.getBottomRightCoordinate());
		LineSegment ballPathTopRight = new LineSegment(ball.getTopRightCoordinate(), nextBall.getTopRightCoordinate());
		LineSegment ballPathBottomLeft = new LineSegment(ball.getBottomLeftCoordinate(),
				nextBall.getBottomLeftCoordinate());
		if (detector.intersects(ballPathTopLeft, windowRectangle.getLeftLineSegment())
				|| detector.intersects(ballPathBottomLeft, windowRectangle.getLeftLineSegment())
				|| detector.intersects(ballPathCenter, windowRectangle.getLeftLineSegment())
				|| detector.intersects(ballPathTopRight, windowRectangle.getRightLineSegment())
				|| detector.intersects(ballPathBottomRight, windowRectangle.getRightLineSegment())
				|| detector.intersects(ballPathCenter, windowRectangle.getRightLineSegment())) {
			return ball.flipXDirection();
		} else if (detector.intersects(ballPathTopLeft, windowRectangle.getTopLineSegment())
				|| detector.intersects(ballPathTopRight, windowRectangle.getTopLineSegment())
				|| detector.intersects(ballPathCenter, windowRectangle.getTopLineSegment())) {
			return ball.flipYDirection();
		} else {
			return ball;
		}
	}

	private static Ball calculateBallHitBat(Ball ball, Rectangle bat, ICollisionDetector detector,
			double LEFT_ANGLE_LIMIT, double RIGHT_ANGLE_LIMIT) {
		Ball nextBall = ball.getMove();
		LineSegment ballPathBottomRight = new LineSegment(ball.getBottomRightCoordinate(),
				nextBall.getBottomRightCoordinate());
		LineSegment ballPathTopLeft = new LineSegment(ball.getTopLeftCoordinate(), nextBall.getTopLeftCoordinate());
		LineSegment ballPathTopRight = new LineSegment(ball.getTopRightCoordinate(), nextBall.getTopRightCoordinate());
		LineSegment ballPathBottomLeft = new LineSegment(ball.getBottomLeftCoordinate(),
				nextBall.getBottomLeftCoordinate());

		boolean canUseBat = ball.getCanUseBat();

		if (ball.getBottomLeftCoordinate().getY() < bat.getTopLeftCoordinate().getY()) {
			canUseBat = true;
			if (ball.getCanUseBat() == false) {
				return ball.setCanUseBat(true);
			}
		}
		if (ball.getCanUseBat() == false) {
			return ball;
		}

		if (canUseBat) {
			if (detector.basicIntersects(ball, bat)
					&& !(detector.intersects(ballPathBottomLeft, bat.getTopLineSegment())
							|| detector.intersects(ballPathBottomRight, bat.getTopLineSegment())
							|| detector.intersects(ballPathTopLeft, bat.getBottomLineSegment())
							|| detector.intersects(ballPathTopRight, bat.getBottomLineSegment()))) {
				Ball changedBall = ball.flipXDirection().flipYDirection()
						.changeAngleDegrees(ball.calculatePercentageOffsetWith(bat)).getMove().setCanUseBat(false);
				if (changedBall.getAngleInDegrees() > LEFT_ANGLE_LIMIT) {
					changedBall = changedBall.setAngleInDegrees(LEFT_ANGLE_LIMIT);
				} else if (changedBall.getAngleInDegrees() < RIGHT_ANGLE_LIMIT) {
					changedBall = changedBall.setAngleInDegrees(RIGHT_ANGLE_LIMIT);
				}

				return changedBall.setCanUseBat(false);
			} else if ((detector.intersects(ballPathBottomLeft, bat.getTopLineSegment())
					|| detector.intersects(ballPathBottomRight, bat.getTopLineSegment()))) {
				Ball changedBall = ball.flipYDirection().changeAngleDegrees(ball.calculatePercentageOffsetWith(bat))
						.setCanUseBat(false);
				if (changedBall.getAngleInDegrees() > LEFT_ANGLE_LIMIT) {
					changedBall = changedBall.setAngleInDegrees(LEFT_ANGLE_LIMIT);
				} else if (changedBall.getAngleInDegrees() < RIGHT_ANGLE_LIMIT) {
					changedBall = changedBall.setAngleInDegrees(RIGHT_ANGLE_LIMIT);
				}

				if (changedBall.getBottomLeftCoordinate().getY() >= bat.getTopLeftCoordinate().getY()) {
					changedBall = changedBall.setPosition(changedBall.getTopLeftCoordinate().getX(),
							(bat.getTopLeftCoordinate().getY() - changedBall.getHeight()));
				}
				return changedBall;
			}
		}
		return ball;
	}

	private void moveBalls() {
		@SuppressWarnings("unchecked")
		LinkedList<Ball> ballsCopy = (LinkedList<Ball>) balls.clone();

		for (Ball ball : ballsCopy) {
			// WINDOW COLLISION DETECTION
			if (calculateBallHitWall(ball, windowRectangle, detector) != ball) {
				balls.add(calculateBallHitWall(ball, windowRectangle, detector));
				balls.remove(ball);
			}
			// BAT COLLISION DETECTION
			else if (calculateBallHitBat(ball, bat, detector, LEFT_ANGLE_LIMIT, RIGHT_ANGLE_LIMIT) != ball) {
				balls.add(calculateBallHitBat(ball, bat, detector, LEFT_ANGLE_LIMIT, RIGHT_ANGLE_LIMIT));
				balls.remove(ball);
			}

			// BRICK COLLISION DETECTION
			else if (calculateBallHitBricks(ball, bricks, detector, ball.isUnstoppable()) != ball) {
				balls.add(calculateBallHitBricks(ball, bricks, detector, ball.isUnstoppable()));
				balls.remove(ball);
			} else {
				balls.add(ball.getMove().setBrickItHit(null));
				balls.remove(ball);
			}
		}
	}

	private void movePackets() {
		@SuppressWarnings("unchecked")
		LinkedList<Packet> copyPackets = (LinkedList<Packet>) packets.clone();
		for (Packet packet : copyPackets) {
			packets.remove(packet);
			packets.add(packet.getMove());
		}
	}

	private void movePhotonBullets() {
		@SuppressWarnings("unchecked")
		LinkedList<PhotonBullet> bulletsCopy = (LinkedList<PhotonBullet>) photonBullets.clone();
		for (PhotonBullet bullet : bulletsCopy) {
			photonBullets.add(bullet.getMove());
			photonBullets.remove(bullet);
		}
	}

	private void cleanBricks() {
		@SuppressWarnings("unchecked")
		LinkedList<Brick> copyBricks = (LinkedList<Brick>) bricks.clone();
		for (Brick brick : copyBricks) {
			if (!brick.isAlive()) {
				bricks.remove(brick);
			}
		}
	}

	private void cleanBalls() {
		@SuppressWarnings("unchecked")
		LinkedList<Ball> copyBalls = (LinkedList<Ball>) balls.clone();
		for (Ball ball : copyBalls) {
			if (ball.getTopLeftCoordinate().getY() > TheController.getBoardHeight()) {
				balls.remove(ball);
			}
		}
	}

	private void cleanPackets() {
		@SuppressWarnings("unchecked")
		LinkedList<Packet> copyPackets = (LinkedList<Packet>) packets.clone();
		for (Packet packet : copyPackets) {
			if (packet.isConsumed() || packet.getTopLeftCoordinate().getY() > TheController.getBoardHeight()) {
				packets.remove(packet);
			}
		}
	}

	private void cleanPhotonBullets() {
		@SuppressWarnings("unchecked")
		LinkedList<PhotonBullet> copyBullets = (LinkedList<PhotonBullet>) photonBullets.clone();
		for (PhotonBullet bullet : copyBullets) {
			if (bullet.getBottomLeftCoordinate().getY() <= 0) {
				photonBullets.remove(bullet);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void updateBricks() {
		LinkedList<Ball> copyBalls = (LinkedList<Ball>) balls.clone();
		LinkedList<PhotonBullet> copyBullets = (LinkedList<PhotonBullet>) photonBullets.clone();
		LinkedList<Brick> copyBricks = (LinkedList<Brick>) bricks.clone();
		for (Ball ball : copyBalls) {
			if (ball.getBrickItHit() != null) {
				hitBrick(ball.getBrickItHit(), PACKET_ODDS, ball.isUnstoppable());
			}

		}
		for (PhotonBullet bullet : copyBullets) {
			for (Brick brick : copyBricks) {
				if (detector.basicIntersects(bullet, brick)) {
					hitBrick(brick, PACKET_ODDS, false);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void updatePhotonBullets() {
		LinkedList<PhotonBullet> copyBullets = (LinkedList<PhotonBullet>) photonBullets.clone();
		LinkedList<Brick> copyBricks = (LinkedList<Brick>) bricks.clone();
		for (PhotonBullet bullet : copyBullets) {
			for (Brick brick : copyBricks) {
				if (detector.basicIntersects(bullet, brick)) {
					photonBullets.add(bullet.kill());
					photonBullets.remove(bullet);
				}
			}
		}
	}

	private void updatePackets() {
		@SuppressWarnings("unchecked")
		LinkedList<Packet> copyPackets = (LinkedList<Packet>) packets.clone();
		for (Packet packet : copyPackets) {
			if (detector.basicIntersects(packet, bat)) {
				packets.add(packet.setConsumed(true));
				packets.remove(packet);
				if (packet.getType() == RectangleType.BallPacket) {
					addBall(BALL_SIZE);
				} else if (packet.getType() == RectangleType.PhotonPacket) {
					startPhotonBlasters();

				} else if (packet.getType() == RectangleType.UnstoppablePacket) {
					makeBallsUnstoppable();
				}
			}
		}
	}

	public void updateAll() {
		cleanBalls();
		cleanPackets();
		cleanBricks();
		cleanPhotonBullets();

		makeBallsStoppable();
		moveBalls();
		movePackets();
		movePhotonBullets();
		if (gunCount >= GUN_TIME) {
			photonBlasters = photonBlasters.setTurnedOn(false);
			gunCount = 0;
		}
		if (photonBlasters.isOn()) {
			gunCount++;
			fireCount++;
			if (fireCount >= FIRE_INTERVAL_TIME) {
				photonBlasters.fireLeftBlaster(photonBullets);
				photonBlasters.fireRightBlaster(photonBullets);
				fireCount = 0;
			}
		} else if (!photonBlasters.isOn()) {
			gunCount = 0;
			photonBlasters = photonBlasters.getOffScreen();
		}

		updateBricks();
		updatePhotonBullets();
		updatePackets();

		if (gameStatus() == GameResult.PlayerWon) {
			System.out.println("YO");
			setHighScoreToLevelNum();
		}

	}

	private void hitBrick(Brick brick, int odds, boolean isUnstoppable) {
		if (isUnstoppable) {
			bricks.add(brick.kill());
			bricks.remove(brick);
		} else if (brick.getType() == RectangleType.BombBrick1) {
			destroyBricksWithinRadius(EXPLOSION_RADIUS, brick.getCenterCoordinate());
		} else if (brick.hitBrick().getHitCount() >= brick.getHitResistance()) {
			bricks.add(brick.kill());
			bricks.remove(brick);
			if (random.nextInt(odds) == (odds - 1)) {
				int randomNum = random.nextInt(3);
				if (randomNum == 0) {
					packets.add(new Packet(brick.getCenterCoordinate(), PACKET_SIZE, PACKET_SIZE,
							RectangleType.BallPacket, PACKET_SPEED, false));
				} else if (randomNum == 1) {
					packets.add(new Packet(brick.getCenterCoordinate(), PACKET_SIZE, PACKET_SIZE,
							RectangleType.UnstoppablePacket, 0.6, false));
				} else if (randomNum == 2) {
					packets.add(new Packet(brick.getCenterCoordinate(), PACKET_SIZE, PACKET_SIZE,
							RectangleType.PhotonPacket, 0.6, false));
				}
			}
		} else {
			bricks.add(brick.hitBrick());
			bricks.remove(brick);
		}
	}

	private void startPhotonBlasters() {
		if (photonBlasters.isOn()) {
			gunCount = 0;
		} else {
			photonBlasters = new PhotonBlasters(bat.getWidth(), bat.getTopLeftCoordinate()).setTurnedOn(true);
		}
	}

	private void makeBallsUnstoppable() {
		@SuppressWarnings("unchecked")
		LinkedList<Ball> copyBalls = (LinkedList<Ball>) balls.clone();
		for (Ball ball : copyBalls) {
			double allRotations = ball.getAngleOfMovement() / (2 * Math.PI);
			double answer = allRotations - Math.floor(allRotations);
			double answerInRadians = answer * (Math.PI * 2);
			if (answerInRadians > Math.PI) {
				balls.remove(ball);
				balls.add(ball.setUnstoppable(true).setSize(UNSTOPPABLE_BALL_SIZE));
			}
		}
	}

	private void makeBallsStoppable() {
		@SuppressWarnings("unchecked")
		LinkedList<Ball> copyBalls = (LinkedList<Ball>) balls.clone();
		for (Ball ball : copyBalls) {
			if (!((ball.getAngleOfMovement() < 0 && ball.getAngleOfMovement() > -(1.5 * Math.PI))
					|| (ball.getAngleOfMovement() > Math.PI
							&& ball.getAngleOfMovement() < (Math.PI + (Math.PI / 2))))) {
				balls.remove(ball);
				balls.add(ball.setUnstoppable(false).setSize(BALL_SIZE));
			}
		}
	}

	public void addBall(double size) {
		balls.add(new Ball(new Coordinate(TheController.getBoardWidth() / 2, TheController.getBoardHeight() / 2), size,
				size, BALL_SPEED, Math.toRadians(25), RectangleType.Ball));
	}

	public void replaceBall(Ball ball, double size) {
		balls.add(new Ball(ball.getTopLeftCoordinate(), size, size, ball.getType(), ball.getCanUseBat(),
				ball.getBrickItHit(), ball.getXScore(), ball.getYScore(), ball.getId(), ball.isUnstoppable(),
				ball.getAngleOfMovement(), ball.getSpeed()));
	}

	// This method is for manually adding bricks so that they do not have to be
	// in a dumb row
	public void addBrick(double width, double height, double posX, double posY, RectangleType type) {
		int hitResistance;
		if (type.equals(RectangleType.ThreeBrick1))
			hitResistance = 3;
		else if (type.equals(RectangleType.TwoBrick1)) {
			hitResistance = 2;
		} else if (type.equals(RectangleType.OneBrick1) || type.equals(RectangleType.BombBrick1)) {
			hitResistance = 1;
		} else {
			hitResistance = 1;
		}

		Brick r = new Brick(new Coordinate(posX, posY), width, height, type, 0, hitResistance);
		bricks.add(r);
	}

	private void destroyBricksWithinRadius(int radius, Coordinate explosionStartingPoint) {
		Coordinate topLeft = new Coordinate(explosionStartingPoint.getX() - radius,
				explosionStartingPoint.getY() - radius);
		Coordinate bottomRight = new Coordinate(topLeft.getX() + (2 * radius), topLeft.getY() + (2 * radius));
		Rectangle rectangleOfDestruction = new Rectangle(topLeft, bottomRight, RectangleType.OneBrick1);

		@SuppressWarnings("unchecked")
		LinkedList<Brick> copyBricks = (LinkedList<Brick>) bricks.clone();
		for (Brick brick : copyBricks) {
			if (detector.basicIntersects(rectangleOfDestruction, brick)) {
				bricks.add(brick.kill());
				bricks.remove(brick);
			}

		}
	}

	public GameResult gameStatus() {
		if (bricks.size() == 0) {
			return GameResult.PlayerWon;
		}
		if (balls.size() == 0) {
			return GameResult.PlayerLost;
		} else
			return GameResult.GameContinuing;
	}

	public void movePaddleLeft() {
		if ((bat.getTopLeftCoordinate().getX() - BAT_SPEED) >= 0) {
			bat = bat.createMove(-BAT_SPEED, 0);
		}
		if (photonBlasters.isOn()) {
			photonBlasters = photonBlasters.getMove(bat.getWidth(), bat.getTopLeftCoordinate());
		}
	}

	private void dragBat(int batPosX) {
		bat = new Bat(new Coordinate(batPosX, bat.getTopLeftCoordinate().getY()), bat.getWidth(), bat.getHeight(),
				bat.getId(), bat.getType());
		if ((bat.getTopLeftCoordinate().getX()) < 0) {
			bat = new Bat(new Coordinate(0, bat.getTopLeftCoordinate().getY()), bat.getWidth(), bat.getHeight(),
					bat.getId(), bat.getType());
		} else if ((bat.getTopRightCoordinate().getX()) > TheController.getBoardWidth() - 3) {
			bat = new Bat(
					new Coordinate(TheController.getBoardWidth() - bat.getWidth() - 3,
							bat.getTopLeftCoordinate().getY()),
					bat.getWidth(), bat.getHeight(), bat.getId(), bat.getType());
		}
		if (photonBlasters.isOn()) {
			photonBlasters = photonBlasters.getMove(bat.getWidth(),
					new Coordinate(bat.getTopLeftCoordinate().getX(), bat.getTopLeftCoordinate().getY()));
		}
	}

	public void setHighScoreToLevelNum() {
		if (Levels.hm.getHighscore() < levelNum) {
			Levels.hm.setHighscore(levelNum);
		}
	}

	public void setHighScoreManager(HighscoreManager hm) {}

	public void movePaddleRight() {
		if (bat.getBottomRightCoordinate().getX() + BAT_SPEED <= TheController.getBoardWidth()) {
			bat = bat.createMove(BAT_SPEED, 0);
		}
		if (photonBlasters.isOn()) {
			photonBlasters = photonBlasters.getMove(bat.getWidth(), bat.getTopLeftCoordinate());
		}
	}

	private static int yScore(Brick brick, Ball ball, ICollisionDetector detector) {
		int count = 0;
		Ball nextBall = ball.getMove();
		LineSegment ballPathCenter = new LineSegment(ball.getCenterCoordinate(), nextBall.getCenterCoordinate());
		LineSegment ballPathTopLeft = new LineSegment(ball.getTopLeftCoordinate(), nextBall.getTopLeftCoordinate());
		LineSegment ballPathBottomRight = new LineSegment(ball.getBottomRightCoordinate(),
				nextBall.getBottomRightCoordinate());
		LineSegment ballPathTopRight = new LineSegment(ball.getTopRightCoordinate(), nextBall.getTopRightCoordinate());
		LineSegment ballPathBottomLeft = new LineSegment(ball.getBottomLeftCoordinate(),
				nextBall.getBottomLeftCoordinate());

		if (detector.intersects(ballPathTopLeft, brick.getTopLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathBottomRight, brick.getTopLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathTopRight, brick.getTopLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathBottomLeft, brick.getTopLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathCenter, brick.getTopLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathTopLeft, brick.getBottomLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathBottomRight, brick.getBottomLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathTopRight, brick.getBottomLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathBottomLeft, brick.getBottomLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathCenter, brick.getBottomLineSegment())) {
			count++;
		}
		return count;
	}

	private static int xScore(Brick brick, Ball ball, ICollisionDetector detector) {
		int count = 0;
		Ball nextBall = ball.getMove();
		LineSegment ballPathCenter = new LineSegment(ball.getCenterCoordinate(), nextBall.getCenterCoordinate());
		LineSegment ballPathTopLeft = new LineSegment(ball.getTopLeftCoordinate(), nextBall.getTopLeftCoordinate());
		LineSegment ballPathBottomRight = new LineSegment(ball.getBottomRightCoordinate(),
				nextBall.getBottomRightCoordinate());
		LineSegment ballPathTopRight = new LineSegment(ball.getTopRightCoordinate(), nextBall.getTopRightCoordinate());
		LineSegment ballPathBottomLeft = new LineSegment(ball.getBottomLeftCoordinate(),
				nextBall.getBottomLeftCoordinate());
		if (detector.intersects(ballPathTopLeft, brick.getRightLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathBottomRight, brick.getRightLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathTopRight, brick.getRightLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathBottomLeft, brick.getRightLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathCenter, brick.getRightLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathTopLeft, brick.getLeftLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathBottomRight, brick.getLeftLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathTopRight, brick.getLeftLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathBottomLeft, brick.getLeftLineSegment())) {
			count++;
		}
		if (detector.intersects(ballPathCenter, brick.getLeftLineSegment())) {
			count++;
		}
		return count;
	}

	public Rectangle getBat() {
		return bat;
	}

	public void setBat(Bat bat) {
		this.bat = bat;
	}

	public void bindAttemptedBatPosX(ReadOnlyIntegerProperty p) {
		attemptedBatPosX.bind(p);
	}

	public PhotonBlasters getPhotonBlasters() {
		return photonBlasters;
	}

	public LinkedList<Ball> getBalls() {
		return balls;
	}

	public LinkedList<Brick> getBricks() {
		return bricks;
	}

	public LinkedList<Packet> getPackets() {
		return packets;
	}

	public LinkedList<PhotonBullet> getPhotonBullets() {
		return photonBullets;
	}

	public int getLevelNum() {
		return levelNum;
	}

	public double getSpeedOfBalls() {
		return BALL_SPEED;
	}

	public int getExplosionRadius() {
		return EXPLOSION_RADIUS;
	}

}

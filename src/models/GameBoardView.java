package models;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.util.LinkedList;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import sample.Controller;
import sample.NewGameController;
import sample.TheController;

public class GameBoardView extends Application {
	private double orgSceneX;
	private IntegerProperty orgTranslateX = new SimpleIntegerProperty(0);
	private IntegerProperty batPosX = new SimpleIntegerProperty(0);

	private Stage window;
	private Scene scene;

	private Runnable onTypePaddleMoveLeft;
	private Runnable onTypePaddleMoveRight;

	private Text score = new Text();

	private Image threeBrick1Image;
	private Image twoBrick1Image;
	private Image oneBrick1Image;
	private ImagePattern threeBrick1Pattern;
	private ImagePattern twoBrick1Pattern;
	private ImagePattern oneBrick1Pattern;

	private Image bomb1Image;
	private ImagePattern bomb1Pattern;

	private Image batImage;
	private ImagePattern batPattern;

	private Image ballImage;
	private ImagePattern ballPattern;

	private Image ballPacketImage;
	private ImagePattern ballPacketPattern;

	private Image unstoppablePacketImage;
	private ImagePattern unstoppablePacketPattern;

	private Image leftBlasterImage;
	private ImagePattern leftBlasterPattern;

	private Image laserImage;
	private ImagePattern laserPattern;
	private ImagePattern laserPacketPattern = new ImagePattern(new Image("images/photonPacket.png"));

	private Image bgImage;

	private Pane layout;
	private Pane particleLayout = new Pane();

	private LinkedList<ImageView> imageViews = new LinkedList<ImageView>();

	private static final int EXPLOSION_COLUMNS = 4;
	private static final int EXPLOSION_COUNT = 16;
	private static final int EXPLOSION_OFFSET_X = 0;
	private static final int EXPLOSION_OFFSET_Y = 0;
	private static final int EXPLOSION_WIDTH = 125;
	private static final int EXPLOSION_HEIGHT = 125;
	private static int EXPLOSIION_RADIUS;

	private Emitter emitter = new FireEmitter();

	private LinkedList<Particle> particles = new LinkedList<>();

	private LinkedList<models.Rectangle> unstoppableRects = new LinkedList<models.Rectangle>();

	public GameBoardView(Runnable onTypePaddleMoveLeft, Runnable onTypePaddleMoveRight,
			int explosionRadius) {
		this.onTypePaddleMoveLeft = onTypePaddleMoveLeft;
		this.onTypePaddleMoveRight = onTypePaddleMoveRight;
		EXPLOSIION_RADIUS = explosionRadius;
		threeBrick1Image = new Image("/images/threeBrick1.png");
		twoBrick1Image = new Image("/images/twoBrick1.png");
		oneBrick1Image = new Image("/images/oneBrick1.png");

		bomb1Image = new Image("/images/bombBrick1.png");

		batImage = new Image("/images/truckUpdate.png");

		ballImage = new Image("/sample/Pi/football.png");

		ballPacketImage = new Image("/images/ballPacket.png");
		unstoppablePacketImage = new Image("/images/unstoppablePacket.png");

		leftBlasterImage = new Image("/images/leftLaserBlaster.png");
		leftBlasterPattern = new ImagePattern(leftBlasterImage);

		laserImage = new Image("/images/laserBeam.png");
		laserPattern = new ImagePattern(laserImage);

		bgImage = new Image("/sample/Pi/SunnyModeUpdate.jpg");

		threeBrick1Pattern = new ImagePattern(threeBrick1Image);
		twoBrick1Pattern = new ImagePattern(twoBrick1Image);
		oneBrick1Pattern = new ImagePattern(oneBrick1Image);

		bomb1Pattern = new ImagePattern(bomb1Image);

		score.setText(TheController.score + "");
		score.setX(450);
		score.setY(40);
		score.setScaleX(1.5);
		score.setScaleY(1.5);

		switch (Controller.mode) {
			case 0:
				bgImage = new Image("/sample/Pi/SunnyModeUpdate.jpg");
				batImage = new Image("/images/truckUpdate.png");
				break;
			case 1:
				bgImage = new Image("/sample/Pi/SnowyModeUpdate.jpg");
				batImage = new Image("/images/sleigh.png");
				break;
			case 2:
				bgImage = new Image("/sample/Pi/RainyModeUpdate.jpg");
				batImage = new Image("/sample/Pi/zont.png");
				break;
		}

		System.out.println(NewGameController.ballType);

		switch (NewGameController.ballType) {
			case 0:
				ballImage = new Image("/sample/Pi/football.png");
				break;
			case 1:
				ballImage = new Image("/sample/Pi/snow.png");
				break;
			case 2:
				ballImage = new Image("/sample/Pi/smile.png");
				break;
		}

		batPattern = new ImagePattern(batImage);

		ballPattern = new ImagePattern(ballImage);

		ballPacketPattern = new ImagePattern(ballPacketImage);
		unstoppablePacketPattern = new ImagePattern(unstoppablePacketImage);

		layout = new Pane();
		particleLayout = new Pane();
		layout.getChildren().addAll(particleLayout, score);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		scene = new Scene(layout, TheController.getBoardWidth(), TheController.getBoardHeight());
		BackgroundImage myBI = new BackgroundImage(bgImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		// then you set to your layout
		layout.setBackground(new Background(myBI));

		// Detecting Key movement
		scene.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent k) {
				if (k.getCode() == KeyCode.LEFT) {
					onTypePaddleMoveLeft.run();
				}

				else if (k.getCode() == KeyCode.RIGHT) {
					onTypePaddleMoveRight.run();
				}
			}
		});
		// Detecting Keymovement end
		// layout.getChildren().add(imageView);
		window.setScene(scene);
		window.show();

	}

	public void drawRectangle(models.Rectangle r) {
		// find a graphical rectangle in children where r.Id = gr.Id
		// if it isn't there, create one and add it to children
		// then...
		//
		// gr.setwidth, etc. according to r
		int ID = r.getId();
		Optional<Node> graphicalRectNode = layout.getChildren().stream().filter(n -> n instanceof Rectangle)
				.filter(n -> n.getId().equals(Integer.toString(ID))).findFirst();
		Rectangle graphicalRect = null;
		if (graphicalRectNode.isPresent()) {
			graphicalRect = (Rectangle) (graphicalRectNode.get());
		} else {
			graphicalRect = new Rectangle();
			layout.getChildren().add(graphicalRect);
		}

		if (r.getType() == RectangleType.ThreeBrick1) {
			graphicalRect.setFill(threeBrick1Pattern);
		} else if (r.getType() == RectangleType.TwoBrick1) {
			graphicalRect.setFill(twoBrick1Pattern);
		} else if (r.getType() == RectangleType.OneBrick1) {
			graphicalRect.setFill(oneBrick1Pattern);
		} else if (r.getType() == RectangleType.BombBrick1) {
			graphicalRect.setFill(bomb1Pattern);
		} else if (r.getType() == RectangleType.BallPacket) {
			graphicalRect.setFill(ballPacketPattern);
		} else if (r.getType() == RectangleType.UnstoppablePacket) {
			graphicalRect.setFill(unstoppablePacketPattern);
		} else if (r.getType() == RectangleType.PhotonPacket) {
			graphicalRect.setFill(laserPacketPattern);
		} else if (r.getType() == RectangleType.PhotonBlaster) {
			graphicalRect.setFill(leftBlasterPattern);
		} else if (r.getType() == RectangleType.PhotonBullet) {
			graphicalRect.setFill(laserPattern);
		} else if (r.getType() == RectangleType.Bat) {
			graphicalRect.setFill(batPattern);
			graphicalRect.setOnMousePressed(rectangleOnMousePressedEventHandler);
			graphicalRect.setOnMouseDragged(rectangleOnMouseDraggedEventHandler);
			graphicalRect.setCursor(Cursor.HAND);
		} else if (r.getType() == RectangleType.Ball) {
			graphicalRect.setFill(ballPattern);
			if (((Ball) r).isUnstoppable()) {
				unstoppableRects.add(r);
			}
		}
		graphicalRectNode = null;

		if (r instanceof Brick && ((Brick) r).isAlive() == false) {
			graphicalRect.setOpacity(0);
			if (r.getType() == RectangleType.BombBrick1) {
				setExplosionAnimationOnto(r);
			}
		} else if (r instanceof Packet && ((Packet) r).isConsumed()) {
			graphicalRect.setOpacity(0);
		}
		graphicalRect.setId(Integer.toString(r.getId()));
		graphicalRect.setWidth(r.getWidth());
		graphicalRect.setHeight(r.getHeight());
		graphicalRect.setTranslateX(r.getTopLeftCoordinate().getX());
		graphicalRect.setTranslateY(r.getTopLeftCoordinate().getY());
		if(r instanceof Brick){
			graphicalRect.setWidth(graphicalRect.getWidth());
			graphicalRect.setHeight(graphicalRect.getHeight());
		}
	}

	EventHandler<MouseEvent> rectangleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			orgSceneX = t.getSceneX();
			orgTranslateX.setValue(((Rectangle) (t.getSource())).getTranslateX());
		}
	};

	EventHandler<MouseEvent> rectangleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			double offsetX = t.getSceneX() - orgSceneX;
			batPosX.setValue(orgTranslateX.getValue() + offsetX);
			((Rectangle) (t.getSource())).setTranslateX(batPosX.getValue());
		}
	};

	public void onUpdate() {
		particleLayout.getChildren().clear();
		for (models.Rectangle r : unstoppableRects) {
			particles.addAll(emitter.emit(r.getCenterCoordinate().getX(), r.getCenterCoordinate().getY()));
		}
		unstoppableRects.clear();
		@SuppressWarnings("unchecked")
		LinkedList<Particle> copyParticles = (LinkedList<Particle>) particles.clone();
		for (Particle p : copyParticles) {
			if (!p.isAlive()) {
				particles.remove(p);
			}
			p.update();
			particleLayout.getChildren().add(p.render());
		}
		score.setText(TheController.score + "");
	}

	public void setExplosionAnimationOnto(models.Rectangle r) {
		ImageView imageView = new ImageView(new Image("/images/animationSheet.png"));
		imageView.setTranslateX(r.getCenterCoordinate().getX() - EXPLOSIION_RADIUS);
		imageView.setTranslateY(r.getCenterCoordinate().getY() - EXPLOSIION_RADIUS);
		imageView.setViewport(
				new Rectangle2D(EXPLOSION_OFFSET_X, EXPLOSION_OFFSET_Y, EXPLOSION_WIDTH, EXPLOSION_HEIGHT));
		SpriteAnimation animation = new SpriteAnimation(imageView, Duration.millis(800), EXPLOSION_COUNT,
				EXPLOSION_COLUMNS, EXPLOSION_OFFSET_X, EXPLOSION_OFFSET_Y, EXPLOSION_WIDTH, EXPLOSION_HEIGHT);
		animation.setCycleCount(1);
		animation.play();
		imageView.setFitWidth(EXPLOSIION_RADIUS * 2);
		imageView.setFitHeight(EXPLOSIION_RADIUS * 2);
		imageViews.add(imageView);
		animation.setOnFinished(e ->  {
			@SuppressWarnings("unchecked")
			LinkedList<ImageView> imageViewsCopy = (LinkedList<ImageView>) imageViews.clone();
			for (ImageView iv : imageViewsCopy) {
				if (animation.getImageView().equals(iv)) {
					imageViews.remove(iv);
					layout.getChildren().remove(iv);
					break;
				}
			}
		});
		layout.getChildren().add(imageView);
	}

	public Scene getScene() {
		return scene;
	}

	public ReadOnlyIntegerProperty getBatPosX() {
		return batPosX;
	}
}

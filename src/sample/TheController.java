package sample;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import models.Ball;
import models.GameBoardModel;
import models.GameBoardView;
import models.GameResult;
import models.Packet;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;

import static helpers.DatabaseHelper.writeResult;

public class TheController {

	private static int BOARD_WIDTH;
	private static int BOARD_HEIGHT;

	public static String player;
	public static int score = 0;

	Timeline timeline;

	TheController(int width, int height) {
		BOARD_WIDTH = width;
		BOARD_HEIGHT = height;
	}

	public void goToPlayScreen(int levelNum) {
		Levels levels = new Levels();
		int desiredFPS = 120;
		GameBoardModel gbm = levels.findLevel(levelNum);
		GameBoardView gameView = new GameBoardView(new MovePaddleLeft(gbm), new MovePaddleRight(gbm), gbm.getExplosionRadius());

		gbm.bindAttemptedBatPosX(gameView.getBatPosX());

		try {
			gameView.start(Main.stage);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		timeline = new Timeline(new KeyFrame(Duration.millis(1000 / desiredFPS), ae -> {
			updateScreen(gameView, gbm);
			if(gbm.gameStatus() != GameResult.GameContinuing){
				writeResult(player, score);
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("Levels.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                score = 0;
                Main.stage.setScene(new Scene(root, 500, 600));
				timeline.stop();
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	public void updateScreen(GameBoardView theView, GameBoardModel theModel) {
			theView.drawRectangle(theModel.getBat());
			for (Ball ball : theModel.getBalls()) {
				theView.drawRectangle(ball);
			}
			for (Packet packet : theModel.getPackets()) {
				theView.drawRectangle(packet);
			}
			for (models.Brick brick : theModel.getBricks()) {
				theView.drawRectangle(brick);
			}

			for (models.Rectangle rectangle : theModel.getPhotonBullets()) {
				theView.drawRectangle(rectangle);
			}
			if (theModel.getPhotonBlasters()!=null) {
				theView.drawRectangle(theModel.getPhotonBlasters().getLeftBlaster());
				theView.drawRectangle(theModel.getPhotonBlasters().getRightBlaster());
			}
			theModel.updateAll();
			theView.onUpdate();

	}

	class MovePaddleLeft implements Runnable {
		GameBoardModel theModel;

		MovePaddleLeft(GameBoardModel theModel) {
			this.theModel = theModel;
		}

		@Override
		public void run() {
			theModel.movePaddleLeft();
		}
	}

	class MovePaddleRight implements Runnable {

		GameBoardModel theModel;

		MovePaddleRight(GameBoardModel theModel) {
			this.theModel = theModel;
		}

		@Override
		public void run() {
			theModel.movePaddleRight();
		}
	}
	public static int getBoardWidth() {
		return BOARD_WIDTH;
	}

	public static int getBoardHeight() {
		return BOARD_HEIGHT;
	}
}

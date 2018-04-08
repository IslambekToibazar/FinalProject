package sample;

import java.util.LinkedList;

import models.GameBoardModel;
import models.HighscoreManager;
import models.JavaCollisionDetector;
import models.RectangleType;

public class Levels {
	public static HighscoreManager hm = new HighscoreManager("Highscore.txt");
	static{
		hm.setHighscore(0);
	}
	public Levels(){
		levels.add(level1);
		levels.add(level2);
		levels.add(level3);
		levels.add(level4);
		double brickWidth = 30;
		double brickHeight = 30;
		level1.addBrick(brickWidth, brickHeight, 50, 160, RectangleType.TwoBrick1);
		level1.addBrick(brickWidth, brickHeight, 120, 160, RectangleType.TwoBrick1);
		level1.addBrick(brickWidth, brickHeight, 190, 160, RectangleType.TwoBrick1);
		level1.addBrick(brickWidth, brickHeight, 260, 160, RectangleType.TwoBrick1);
		level1.addBrick(brickWidth, brickHeight, 330, 160, RectangleType.TwoBrick1);
		level1.addBrick(brickWidth, brickHeight, 400, 160, RectangleType.TwoBrick1);

		level1.addBrick(brickWidth, brickHeight, 260, 260, RectangleType.OneBrick1);
		level1.addBrick(brickWidth, brickHeight, 190, 260, RectangleType.OneBrick1);
		level1.addBrick(brickWidth, brickHeight, 120, 260, RectangleType.OneBrick1);
		level1.addBrick(brickWidth, brickHeight, 330, 260, RectangleType.OneBrick1);

		level1.addBrick(brickWidth, brickHeight, 190, 360, RectangleType.OneBrick1);
		level1.addBrick(brickWidth, brickHeight, 260, 360, RectangleType.OneBrick1);

		level1.addBrick(brickWidth, brickHeight, 225, 460, RectangleType.BombBrick1);
	}
	LinkedList<GameBoardModel> levels = new LinkedList<GameBoardModel>();

	public GameBoardModel findLevel(int levelNum){
		for(GameBoardModel gbm: this.levels){
			if(gbm.getLevelNum()==levelNum){
				return gbm;
			}
		}
		return null;
	}
	JavaCollisionDetector jcd = new JavaCollisionDetector();
	GameBoardModel level1 = new GameBoardModel(1,
			0, // Brick Height
			0, // Horizontal Brick Gap
			0, // Vertical Brick Gap
			0, // Gap above bricks
			0, // # of Columns
			0, // # of Rows
			30, // Bat Speed
			2, // Ball Speed
			jcd);

	GameBoardModel level2 = new GameBoardModel(2,
			50, // Brick Height
			30, // Horizontal Brick Gap
			30, // Vertical Brick Gap
			100, // Gap above bricks
			4, // # of Columns
			4, // # of Rows
			30, // Bat Speed
			2, // Ball Speed
			jcd);
	GameBoardModel level3 = new GameBoardModel(3,
			80, // Brick Height
			40, // Horizontal Brick Gap
			40, // Vertical Brick Gap
			70, // Gap above bricks
			6, // # of Columns
			4, // # of Rows
			30, // Bat Speed
			3, // Ball Speed
			jcd);

	GameBoardModel level4 = new GameBoardModel(4,
			60, // Brick Height
			40, // Horizontal Brick Gap
			40, // Vertical Brick Gap
			70, // Gap above bricks
			6, // # of Columns
			4, // # of Rows
			30, // Bat Speed
			3, // Ball Speed
			jcd);

	public int getHighScore(){
		return hm.getHighscore();
	}
}

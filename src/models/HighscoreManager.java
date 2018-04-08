package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class HighscoreManager {
	private Path fileName;
	private File highScore;

	public HighscoreManager(String fileName) {
		this.fileName = Paths.get(fileName);
		highScore = new File(fileName);
		try {
			highScore.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getHighscore() {
		int num = 0;
		Scanner fileReader = null;
		try {
			fileReader = new Scanner(new File(fileName.toString()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (fileReader.hasNextInt()) {
			num = fileReader.nextInt();
			fileReader.close();
		}

		return num;
	}

	public void setHighscore(int num) {
		try {
			PrintStream scoreWriter = new PrintStream(fileName.toString());
			scoreWriter.print(num);
			scoreWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}

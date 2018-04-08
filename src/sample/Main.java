package sample;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage stage;
    public static Media backgroundSound = new Media(Main.class.getResource("background.mp3").toString());
    public static MediaPlayer bgSound = new MediaPlayer(backgroundSound);

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        stage = primaryStage;
        primaryStage.setTitle("BLOCKDROP++");
        primaryStage.setScene(new Scene(root, 500, 600));
        bgSound.setCycleCount(Timeline.INDEFINITE);
        bgSound.play();
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}

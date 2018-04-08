package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Controller {

    public static int mode = 0;

    TheController controller = new TheController(500, 600);

    @FXML
    private JFXButton newGame;
    @FXML
    private JFXButton play;
    @FXML
    private JFXButton level1;

    @FXML
    private JFXButton level2;

    @FXML
    private JFXButton level3;

    @FXML
    private JFXButton level4;
    @FXML
    private JFXButton backLevel;

    @FXML
    private JFXToggleButton rain;

    @FXML
    private JFXToggleButton snow;

    @FXML
    private JFXToggleButton sun;

    @FXML
    void rainy(ActionEvent event) {
        mode = (rain.isSelected()) ? 2 : 0;
        rain.setSelected(rain.isSelected());
        sun.setSelected(!rain.isSelected());
    }

    @FXML
    void snowy(ActionEvent event) {
        mode = (snow.isSelected()) ? 1 : 0;
        snow.setSelected(snow.isSelected());
        sun.setSelected(!snow.isSelected());
    }

    @FXML
    void sunny(ActionEvent event) {
        mode = (sun.isSelected()) ? 0 : 1;
        sun.setSelected(sun.isSelected());
        snow.setSelected(!sun.isSelected());
    }

    @FXML
    void backOfLvel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("NewGame.fxml"));
        Main.stage.setScene(new Scene(root, 500, 600));
    }

    @FXML
    void levelfor(ActionEvent event) {
        controller.goToPlayScreen(4);
    }

    @FXML
    void levelone(ActionEvent event) {
        controller.goToPlayScreen(1);
    }

    @FXML
    void levelthree(ActionEvent event) {
        controller.goToPlayScreen(2);
    }

    @FXML
    void leveltwo(ActionEvent event) {
        controller.goToPlayScreen(3);
    }


    @FXML
    void enter(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Levels.fxml"));
        Main.stage.setScene(new Scene(root, 500, 600));
    }

    @FXML
    void newGam(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("newGame.fxml"));
        Main.stage.setScene(new Scene(root, 500, 600));
    }

    @FXML
    private JFXButton BACK;

    @FXML
    void BAC(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.stage.setScene(new Scene(root, 500, 600));

    }

    @FXML
    private JFXButton bak;

    @FXML
    void bak1(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.stage.setScene(new Scene(root, 500, 600));
    }

    @FXML
    private JFXButton st;


    @FXML
    void sts(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Main.stage.setScene(new Scene(root, 500, 600));
    }


    @FXML
    private JFXButton ms;

    @FXML
    private JFXButton srs;

    @FXML
    void msm(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("modes.fxml"));
        Main.stage.setScene(new Scene(root, 500, 600));
    }

    @FXML
    void sr(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("scores.fxml"));
        Main.stage.setScene(new Scene(root, 500, 600));
    }

    @FXML
    private JFXButton b;

    @FXML
    void b1(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.stage.setScene(new Scene(root, 500, 600));
    }

    @FXML
    private JFXButton bam;


    @FXML
    void bam1(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.stage.setScene(new Scene(root, 500, 600));
    }

    @FXML
    private JFXButton xit;

    @FXML
    void xit1(ActionEvent event)
            throws IOException {
        Main.stage.close();
    }
}




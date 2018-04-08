package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class NewGameController {

    public static int ballType;

    @FXML
    private JFXTextField player;

    @FXML
    private JFXButton play;

    @FXML
    private ToggleGroup j;

    @FXML
    private JFXButton BACK;

    @FXML
    void BAC(ActionEvent event) {

    }

    @FXML
    public void initialize() {
        ballType = 0;
        j.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (j.getSelectedToggle() != null) {
                RadioButton chk = (RadioButton) newValue.getToggleGroup().getSelectedToggle();
                ballType = Integer.parseInt(chk.getId());
            }
        });
    }

    @FXML
    void enter(ActionEvent event) throws IOException {
        if (player.getText().length() > 0) {
            TheController.player = player.getText();
        }
        Parent root = FXMLLoader.load(getClass().getResource("Levels.fxml"));
        Main.stage.setScene(new Scene(root, 500, 600));
    }

}

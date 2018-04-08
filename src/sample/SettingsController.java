package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SettingsController {

    @FXML
    private JFXSlider slider;

    @FXML
    private JFXButton bak;

    @FXML
    void bak1(ActionEvent event) {

    }

    @FXML
    public void initialize() {
        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            Main.bgSound.setVolume(new_val.doubleValue() / 100);
        });
    }
}
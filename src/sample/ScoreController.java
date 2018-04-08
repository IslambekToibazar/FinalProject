package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import helpers.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import models.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ScoreController {

    ObservableList<Data> list = FXCollections.observableArrayList();

    @FXML
    private JFXButton b;

    @FXML
    private JFXListView<Data> top_players;

    @FXML
    void b1(ActionEvent event) {

    }

    @FXML
    public void initialize() {
        DatabaseHelper helper = new DatabaseHelper();
        list = helper.getResults();
        top_players.setItems(list);
    }

    public void fillData() {

    }
}
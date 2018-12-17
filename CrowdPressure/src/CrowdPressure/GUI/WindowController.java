package CrowdPressure.GUI;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

//import java.awt.*;

public class WindowController implements Initializable {

    @FXML
    public MenuItem menuInfo;
    @FXML
    public ToggleButton tbPlayPause;
    @FXML
    public ToggleButton tbAddHuman;
    @FXML
    public ToggleButton tbAddWall;
    @FXML
    public Button btnNextFrame;
    @FXML
    public ComboBox<String> cbSimulations;
    @FXML
    public Canvas board;
    @FXML
    public Button btnClear;
    @FXML
    public Label logInfo;

    public void showInfo(ActionEvent actionEvent) {
    }

    public void simPlayPause(ActionEvent actionEvent) {
    }

    public void simNextFrame(ActionEvent actionEvent) {
    }

    public void simClearBoard(ActionEvent actionEvent) {
    }

    public void simChooseSim(ActionEvent actionEvent) {
    }

    public void simAddHuman(ActionEvent actionEvent) {
    }

    public void simAddWall(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

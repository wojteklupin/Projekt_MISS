package CrowdPressure.GUI;

import CrowdPressure.*;
import CrowdPressure.model.Board;
import CrowdPressure.model.map.Wall;
import CrowdPressure.model.pedestrian.Human;
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


public class WindowController implements Initializable {

    private int count=0;
    private int fps;
    private MapBuilder simBuilder = new MapBuilder();
    private Point destination;
    private Board map;
    private String simChosen;
    private Timeline simLoop;
    private GraphicsContext gc;
    private Engine engine;
    private ArrayList<Double> walls = new ArrayList<>(2);


    @FXML
    public MenuItem menuInfo;
    @FXML
    public MenuItem menuQuit;
    @FXML
    public Button btnPlayPause;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        simChosen = "Default";
        engine = Initializer.createEngine(this.simBuilder, simChosen);
        map = engine.getMap();

        walls.clear();
        destination = new Point(600,200);//Configuration.DEFAULT_DESTINATION_POSITION;
        fps = Configuration.INITIAL_FPS;


        logInfo.setText("Welcome to Crowd Pressure simulation!");

        cbSimulations.getItems().removeAll(cbSimulations.getItems());


        for(String s : simBuilder.getSimulationList()){
            cbSimulations.getItems().add(s);
        }
        cbSimulations.getSelectionModel().select(simChosen);

        btnPlayPause.setText("Play");

        cbSimulationsListener();

        drawSimulation();

    }


    private void cbSimulationsListener() {
        cbSimulations.valueProperty().addListener((ov, old_val, new_val) -> {
            for (String s : simBuilder.getSimulationList()) {
                if (s.equals(new_val)) {
                    changeSimulation(s);
                    //pedestriansFactory = new PedestriansFactory();
                    break;
                }
            }
        });
    }

    private void changeSimulation(String symType) {
        simLoop.pause();
        btnPlayPause.setText("Start");
        btnNextFrame.setDisable(false);

        this.simChosen = symType;
        engine = Initializer.createEngine(this.simBuilder, symType);
        map = engine.getMap();
        List<Human> humans = map.getHumans();
        if (humans.size() > 0) {
            destination = engine.getMap().getHumans().get(0).getDestination();
        }
        gc.clearRect(0, 0, board.getWidth(), board.getHeight());
        drawSimulation();
    }


    @FXML
    public void showInfo(ActionEvent actionEvent) {
        System.out.println("Please choose one of the preset simulations or create your own one using Add Human/Add Wall buttons and press play.");
        logInfo.setText("Please choose one of the preset simulations or create your own one using Add Human/Add Wall buttons and press play.");
    }

    @FXML
    public void simPlayPause() {
        switch (simLoop.getStatus()) {
            case RUNNING:
                simLoop.pause();
                btnPlayPause.setText("Play");
                System.out.println("Stopped.");
                btnNextFrame.setDisable(false);
                break;
            case PAUSED:
            case STOPPED:
                simLoop.play();
                btnPlayPause.setText("Pause");
                System.out.println("Running.");
                btnNextFrame.setDisable(true);
                break;
        }
    }

    @FXML
    public void simNextFrame(ActionEvent actionEvent) {
        Duration duration = Duration.millis(1000 / (float) fps);
        KeyFrame frame = getNextFrame(duration);
        Timeline loop = new Timeline();
        int cycleCount = 1;
        loop.setCycleCount(cycleCount);
        loop.getKeyFrames().add(frame);
        loop.play();
        System.out.println("Next Frame");
    }

    @FXML
    public void simClearBoard(ActionEvent actionEvent) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource("GUI/Window.fxml"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        btnClear.getScene().setRoot(root);
    }

    @FXML
    public void simChooseSim(ActionEvent actionEvent) {
        System.out.println(cbSimulations.getValue());
    }

    @FXML
    public void simAddHuman(ActionEvent actionEvent) {
        if (tbAddHuman.isSelected()){
            logInfo.setText("Click to choose human location");
            // Control other buttons
            tbAddWall.setDisable(true);
            btnPlayPause.setDisable(true);
            btnNextFrame.setDisable(true);
            cbSimulations.setDisable(true);
            btnClear.setDisable(true);

            //Adding humans control
            board.setOnMouseClicked(event -> {
                double posX = event.getX();
                double posY = event.getY();

                System.out.println(posX + " x " + posY);
                System.out.println("Click to choose human location");
                logInfo.setText("Click to choose human location");
                setHuman(posX, posY);

            });

        } else {
            // Control other buttons
            tbAddWall.setDisable(false);
            btnPlayPause.setDisable(false);
            btnNextFrame.setDisable(false);
            cbSimulations.setDisable(false);
            btnClear.setDisable(false);
            logInfo.setText("Crowd Pressure");
        }
    }

    @FXML
    public void simAddWall(ActionEvent actionEvent) {
        if (tbAddWall.isSelected()) {
            count=0;

            tbAddHuman.setDisable(true);
            btnPlayPause.setDisable(true);
            btnNextFrame.setDisable(true);
            cbSimulations.setDisable(true);
            btnClear.setDisable(true);
            System.out.println("Click to choose beginning of the wall");
            //System.out.println(tbAddWall.getToggleGroup());
            logInfo.setText("Click to choose beginning of the wall");
            board.setOnMouseClicked(event -> {
                //System.out.println("Count before: " + count);
                count++;
                //System.out.println("Count after: " + count);

                double posX = event.getX();
                double posY = event.getY();

                System.out.println(posX + " x " + posY);

                if (walls.isEmpty()) {
                    walls.add(0, posX);
                    walls.add(1, posY);
                }
                double posX1 = walls.get(0);
                double posY1 = walls.get(1);
                System.out.println(posX1 + " x " + posY1);
                if ((posX1 != posX) || (posY1 != posY)) {
                    if (count==2){
                        tbAddWall.setSelected(false);
                        tbAddHuman.setDisable(false);
                        btnPlayPause.setDisable(false);
                        btnNextFrame.setDisable(false);
                        cbSimulations.setDisable(false);
                        btnClear.setDisable(false);
                        logInfo.setText("Crowd Pressure");
                    }
                    setWall(new Point(posX, posY), new Point(posX1, posY1));//descale
                } else {
                    if (count==2){
                        tbAddWall.setSelected(false);
                        tbAddHuman.setDisable(false);
                        btnPlayPause.setDisable(false);
                        btnNextFrame.setDisable(false);
                        cbSimulations.setDisable(false);
                        btnClear.setDisable(false);
                        logInfo.setText("Crowd Pressure");
                    }
                    System.out.println("Need another click to create wall...");
                    logInfo.setText("Need another click to create wall...");
                }
            });
        } else {
            tbAddHuman.setDisable(false);
            btnPlayPause.setDisable(false);
            btnNextFrame.setDisable(false);
            cbSimulations.setDisable(false);
            btnClear.setDisable(false);
        }
    }

    @FXML
    public void quit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you leaving already?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) btnPlayPause.getScene().getWindow();
            stage.close();
        } else {
            actionEvent.consume();
        }
    }

    private KeyFrame getNextFrame(Duration duration) {
        return new KeyFrame(duration, e -> {
            try {
                gc.clearRect(0, 0, board.getWidth(), board.getHeight());
                System.out.print("");
                drawBoard(map);
                drawHuman(engine.getMap().getHumans());
                drawDestination();
                engine.nextState();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void drawSimulation() {
        initBoard();
        initLoop(this.fps);
        drawBoard(map);
        drawHuman(engine.getMap().getHumans());
        drawDestination();
    }

    private void initBoard() {
        board.setHeight(600);
        board.setWidth(800);
        gc = board.getGraphicsContext2D();
        board.setScaleX(1); // Left -> Right ASC
        board.setScaleY(-1); // Down -> Up ASC
    }


    private void initLoop(int fps) {
        Duration duration = Duration.millis(1000 / (float) fps);
        KeyFrame frame = getNextFrame(duration);
        simLoop = new Timeline();
        int cycleCount = Animation.INDEFINITE;
        simLoop.setCycleCount(cycleCount);
        simLoop.getKeyFrames().add(frame);
    }

    private void drawDestination() {
        double x = destination.getX();
        double y = destination.getY();
        gc.setFill(Color.BLACK);
        gc.fillArc(x, y, 5, 5, 0, 360, ArcType.ROUND);
    }

    private void drawHuman(List<Human> humans) {
        for (Human h : humans) {

            Color pedestrianColor = getPressureColor(
                    h.getCrowdPressure()
            );

            gc.setFill(pedestrianColor);
            double x = h.getPosition().getX();
            double y = h.getPosition().getY();
            double radius = h.getRadius();

            gc.fillArc(x-radius*5, y-radius*5, radius*10, radius*10, 0, 360, ArcType.OPEN);
        }
    }

    private Color getPressureColor(double x) {
        double red=0.0;
        double green=0.0;
        if (x > 0.5) {
            red = 1.0;
        } else {
            red = 2*x / 1.0;
        }

        if (x > 0.5) {
            green = 1-2*(x-0.5)/1.0;
        } else {
            green = 1.0;
        }

        return new Color(red, green, 0.0, 1.0);
    }

    private void setWall(Point pos1, Point pos2) {
        Wall newWall = new Wall(pos1, pos2);
        List<Wall> currentWalls = map.getWalls();
        currentWalls.add(newWall);
        map = new Board(800, 600, map.getHumans(), currentWalls);
        drawBoard(map);
        System.out.println("Wall placed");
        walls.clear();
    }

    private void setHuman(double posX, double posY) {
        gc.fillArc(posX, posY, 5, 5, 0, 360, ArcType.OPEN);
        Human h = new Human(this.map,this.map.getHumans().size() + 1, 360, 2, Math.PI / 8, 40, 0.5, new Point(600,200), new Point(posX, posY));
        this.map.addHuman(h);
    }

    private void drawBoard(Board map) {
        for (Wall w : map.getWalls()) {
            Point start = w.getStartPosition();
            Point end = w.getEndPosition();
            gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY()); //scale
        }
    }
}

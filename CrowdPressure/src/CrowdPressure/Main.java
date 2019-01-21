package CrowdPressure;

import CrowdPressure.model.Board;
import CrowdPressure.model.map.Wall;
import CrowdPressure.model.pedestrian.Human;

import java.util.ArrayList;
import java.util.List;


public class Main {
/*
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GUI/Window.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Crowd pressure simulation");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setOnCloseRequest(check -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Are you leaving already?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                primaryStage.close();
            } else {
                check.consume();
            }
        });
        primaryStage.show();

    }*/

    public static void main(String[] args) {

        //launch(args);


        System.out.println("Test 1: \n-------");
        List<Wall> walls = new ArrayList<>();
        walls.add(new Wall(new Point(5, 1.5), new Point(5, 2.5)));
        walls.add(new Wall(new Point(5, 2.5), new Point(6, 2.5)));
        walls.add(new Wall(new Point(2,2), new Point(3,3)));
        walls.add(new Wall(new Point(6,6), new Point(6,2.5)));
        List<Human> humans = new ArrayList<>();
        Board e = new Board(6, 6, humans, walls);
        e.addHuman(new Human(e, 1, 90, 2, Math.PI / 4, 5, 0.5, new Point(6,2), new Point(1,5)));
        Human p = e.getHumans().get(0);
        e.addHuman(new Human(e, 2, 100, 0, Math.PI / 4, 5, 0.2, new Point(6,2), new Point(2.736022, 4.00691)));
        int counter = 0;
        while(counter < 5){
            System.out.println(p);
            p.nextStep();
            counter += 1;
        }



    }
}

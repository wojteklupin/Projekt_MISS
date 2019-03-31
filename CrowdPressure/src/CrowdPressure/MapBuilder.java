package CrowdPressure;

import CrowdPressure.Point;
import CrowdPressure.model.Board;
import CrowdPressure.model.map.Wall;
import CrowdPressure.model.pedestrian.Human;

import java.util.ArrayList;
import java.util.List;

public class MapBuilder {

    private List<String> simulationList = new ArrayList<>();

    public MapBuilder(){
        simulationList.add("Deafult1");
        simulationList.add("Deafult2");
        simulationList.add("Deafult3");
    }

    public static Board buildDefaultSimulation(){

        Board board = new Board(800, 600, new ArrayList<Human>(), new ArrayList<Wall>());

        board.addWall(new Wall(new Point(500, 150), new Point(500, 250)));
        board.addWall(new Wall(new Point(500, 250), new Point(600, 250)));
        board.addWall(new Wall(new Point(200,200), new Point(300,300)));
        //board.addWall(new Wall(new Point(600,600), new Point(600,250)));

        board.addHuman(new Human(board, 1, 360, 2, Math.PI / 2, 4000, 0.5, new Point(600,200), new Point(100,500)));
        board.addHuman(new Human(board, 1, 360, 2, Math.PI / 2, 4000, 0.5, new Point(600,200), new Point(200,500)));
        board.addHuman(new Human(board, 1, 360, 2, Math.PI / 2, 4000, 0.5, new Point(600,200), new Point(300,500)));
        board.addHuman(new Human(board, 1, 360, 2, Math.PI / 2, 4000, 0.5, new Point(600,200), new Point(100,400)));
       // board.addHuman(new Human(board, 2, 100, 0, Math.PI / 4, 5, 0.2, new Point(6,2), new Point(2.736022, 4.00691)));

       return board;
    }

    public Board buildCustomSimulation(String name){
        //TO DO

        return new Board(800, 600);
    }


    public List<String> getSimulationList() {
        return simulationList;
    }
}

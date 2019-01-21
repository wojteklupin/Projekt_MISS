import CrowdPressure.Point;
import CrowdPressure.model.Board;
import CrowdPressure.model.map.Wall;
import CrowdPressure.model.pedestrian.Human;

import java.util.ArrayList;

public class MapBuilder {


    public static Board buildDefaultSimulation(){

        Board board = new Board(800, 600, new ArrayList<Human>(), new ArrayList<Wall>());

        board.addWall(new Wall(new Point(5, 1.5), new Point(5, 2.5)));
        board.addWall(new Wall(new Point(5, 2.5), new Point(6, 2.5)));
        board.addWall(new Wall(new Point(2,2), new Point(3,3)));
        board.addWall(new Wall(new Point(6,6), new Point(6,2.5)));

        board.addHuman(new Human(board, 1, 90, 2, Math.PI / 4, 5, 0.5, new Point(6,2), new Point(1,5)));
        board.addHuman(new Human(board, 2, 100, 0, Math.PI / 4, 5, 0.2, new Point(6,2), new Point(2.736022, 4.00691)));

       return board;
    }

    public Board buildCustomSimulation(String name){
        //TO DO

        return new Board(800, 600);
    }

}

package CrowdPressure.Map;

import CrowdPressure.Human;

import java.util.ArrayList;
import java.util.List;

public class MapBuilder {

    private List<String> simulationList = new ArrayList<>();

    public MapBuilder(){
        simulationList.add("Default");
        simulationList.add("Bottleneck");
        simulationList.add("Bottleneck with column");
        simulationList.add("1 Human 0 Walls");
        simulationList.add("1 Human 1 Wall");
    }

    public static Board buildDefaultSimulation(){

        Board board = new Board(800, 600, new ArrayList<Human>(), new ArrayList<Wall>());

        board.addWall(new Wall(new Point(500, 150), new Point(500, 250)));
        board.addWall(new Wall(new Point(500, 250), new Point(600, 250)));
        board.addWall(new Wall(new Point(200,200), new Point(300,300)));
        board.addWall(new Wall(new Point(600,600), new Point(600,250)));

        board.addHuman(new Human(board, 1, 360, 0.9, 0.5, 40, 0.5, new Point(600,200), new Point(100,500)));
        board.addHuman(new Human(board, 2, 360, 0.9, 0.5, 40, 0.5, new Point(600,200), new Point(200,500)));
        board.addHuman(new Human(board, 3, 360, 0.9, 0.5, 40, 0.5, new Point(600,200), new Point(300,500)));
        board.addHuman(new Human(board, 4, 360, 0.9, 0.5, 40, 0.5, new Point(600,200), new Point(100,400)));
        //board.addHuman(new Human(board, 2, 100, 0, Math.PI / 4, 5, 0.2, new Point(6,2), new Point(2.736022, 4.00691)));

       return board;
    }

    public Board buildCustomSimulation(String name){
            Board board = new Board(800, 600, new ArrayList<Human>(), new ArrayList<Wall>());
            if (name == "Default"){

                board.addWall(new Wall(new Point(500, 150), new Point(500, 250)));
                board.addWall(new Wall(new Point(500, 250), new Point(600, 250)));
                board.addWall(new Wall(new Point(200,200), new Point(300,300)));

                board.addHuman(new Human(board, 1, 360, 2, Math.PI/8, 40, 0.5, new Point(600,200), new Point(100,500)));
                board.addHuman(new Human(board, 2, 360, 2, Math.PI/8, 40, 0.5, new Point(600,200), new Point(200,500)));
                board.addHuman(new Human(board, 3, 360, 2, Math.PI/8, 40, 0.5, new Point(600,200), new Point(300,500)));
                board.addHuman(new Human(board, 4, 360, 2, Math.PI/8, 40, 0.5, new Point(600,200), new Point(100,400)));
            }
            if (name == "1 Human 0 Walls"){

                board.addHuman(new Human(board, 1, 360, 2, Math.PI/8, 40, 0.5, new Point(600,200), new Point(100,500)));
            }

            if (name == "1 Human 1 Wall"){

                board.addHuman(new Human(board, 1, 360, 2, Math.PI/8, 40, 0.5, new Point(600,200), new Point(100,500)));

                board.addWall(new Wall(new Point(400, 100), new Point(500, 450)));
            }
            if (name == "Bottleneck"){

                int humanCounter = 0;
                for(int x = 380; x < 440; x += 6){
                    for(int y=180; y < 212; y += 4){
                        board.addHuman(new Human(board, humanCounter + 1, 360, 1.4, Math.PI/8, 40, 0.5, new Point(600,200), new Point(x,y)));
                    }
                }

                board.addWall(new Wall(new Point(500, 50), new Point(500, 199)));
                board.addWall(new Wall(new Point(500, 200), new Point(500, 375)));
                board.addWall(new Wall(new Point(100, 375), new Point(500, 375)));
                board.addWall(new Wall(new Point(100, 50), new Point(500, 50)));
                board.addWall(new Wall(new Point(100, 50), new Point(100, 375)));

            }
            if (name == "Bottleneck with column") {

                int humanCounter = 0;
                for(int x = 380; x < 440; x += 6){
                    for(int y=180; y < 212; y += 4){
                        board.addHuman(new Human(board, humanCounter + 1, 360, 1.4, Math.PI/8, 40, 0.5, new Point(600,200), new Point(x,y)));
                    }
                }


                board.addWall(new Wall(new Point(500, 50), new Point(500, 199)));
                board.addWall(new Wall(new Point(500, 203), new Point(500, 375)));
                board.addWall(new Wall(new Point(100, 375), new Point(500, 375)));
                board.addWall(new Wall(new Point(100, 50), new Point(500, 50)));
                board.addWall(new Wall(new Point(100, 50), new Point(100, 375)));
                board.addWall(new Wall(new Point(500, 201), new Point(490, 201)));

            }

        return board;
    }


    public List<String> getSimulationList() {
        return simulationList;
    }
}

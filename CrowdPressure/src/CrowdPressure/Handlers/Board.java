package CrowdPressure.Handlers;

import java.util.LinkedList;

public class Board {

    private Integer width;
    private Integer height;
    private LinkedList<Wall> walls = new LinkedList<>();
    private LinkedList<Human> humans = new LinkedList<>();


    public Board(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void addWall(Wall wall){
        this.walls.add(wall);
    }

    public void addHuman(Human human){
        this.humans.add(human);
    }

    public LinkedList<Wall> getWalls(){
        return this.walls;
    }

    public LinkedList<Human> getHumans(){
        return this.humans;
    }

    public Integer getWidth(){
        return this.width;
    }

    public Integer getHeight(){
        return this.height;
    }

}

package CrowdPressure.model;

import java.util.List;

import CrowdPressure.model.map.Wall;
import CrowdPressure.model.pedestrian.Human;

public class Board {

	private List<Human> humans;
	private List<Wall> walls;
	private double width;
	private double height;

	public Board(double width, double height, List<Human> humans, List<Wall> walls) {
		super();
		this.width = width;
		this.height = height;
		this.humans = humans;
		this.walls = walls;
	}

	public Board(double width, double height){
		super();
		this.width = width;
		this.height = height;
	}

	public List<Human> getHumans() {
		return humans;
	}

	public void setHumans(List<Human> humans) {
		this.humans = humans;
	}

	public void addHuman(Human p){
		this.humans.add(p);
	}

	public void addWall(Wall w){ this.walls.add(w);}

	public List<Wall> getWalls() {
		return walls;
	}

	public void setWalls(List<Wall> walls) {
		this.walls = walls;
	}
}

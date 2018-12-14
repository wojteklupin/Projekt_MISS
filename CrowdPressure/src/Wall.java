public class Wall implements Obstacle {

    private Point start;
    private Point end;
    private double length;


    public Wall(Point _start, Point _end){
        this.start = _start;
        this.end = _end;
        this.length = _start.distance(_end);
    }


}

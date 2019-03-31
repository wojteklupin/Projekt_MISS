package CrowdPressure.Symulation;

import CrowdPressure.Human;
import CrowdPressure.Map.Board;

import java.util.Iterator;

public class Engine {

    private int step;

    private Board map;

    public Engine(Board map) {
        this.map = map;
        this.step = 0;
    }


    public void nextState()  {


        for (Iterator<Human> iterator = map.getHumans().iterator(); iterator.hasNext();) {
             Human h = iterator.next();
             System.out.println(h);
            if (h.isFinished()) {
                iterator.remove();
            }
            else {
                h.nextStep();
                this.step++;
            }

        }


    }

    public Board getMap() {
        return this.map;
    }

    public void setMap(Board map) {
        this.map = map;
    }
}

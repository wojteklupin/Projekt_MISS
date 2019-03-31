package CrowdPressure;

import CrowdPressure.model.Board;
import CrowdPressure.model.pedestrian.Human;

import java.util.Iterator;

public class Engine {

    private int step;

    private Board map;

    public Engine(Board map) {
        this.map = map;
        this.step = 0;
    }


    public void nextState()  {

/*
        for (Human h : map.getHumans()) {
            if (h.isFinished()) {
                //cod.i("PEDESTRIAN: " + p.getPedestrianInformation().getStaticInformation().getId() + " FINISHED ON STEP: "+step);
                continue;
            }
            //p.prepareNextStep();
        }
*/

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

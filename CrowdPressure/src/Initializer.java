import CrowdPressure.model.Board;
import CrowdPressure.model.pedestrian.Human;
import org.omg.CORBA.Environment;

import java.util.ArrayList;
import java.util.List;

public class Initializer {

    static public Engine createEngine(MapBuilder builder, String simulationName) {


        //Board simulationMap = builder.buildCustomSimulation(simulationName);

        Board simulationMap = MapBuilder.buildDefaultSimulation();


        return new Engine(simulationMap);
    }

}

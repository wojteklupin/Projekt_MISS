package CrowdPressure.Symulation;

import CrowdPressure.Map.Board;
import CrowdPressure.Map.MapBuilder;

public class Initializer {

    static public Engine createEngine(MapBuilder builder, String simulationName) {


        Board simulationMap = builder.buildCustomSimulation(simulationName);


        return new Engine(simulationMap);
    }

}

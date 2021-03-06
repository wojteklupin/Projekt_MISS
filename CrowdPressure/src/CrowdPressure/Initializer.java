package CrowdPressure;

import CrowdPressure.Map.Board;

public class Initializer {

    static public Engine createEngine(MapBuilder builder, String simulationName) {


        Board simulationMap = builder.buildCustomSimulation(simulationName);

        //Board simulationMap = MapBuilder.buildDefaultSimulation();


        return new Engine(simulationMap);
    }

}

package CrowdPressure.calculators;

import CrowdPressure.calculators.figures.LineTwoPoints;
import CrowdPressure.calculators.figures.Vector;
import CrowdPressure.model.map.Wall;
import CrowdPressure.model.pedestrian.Human;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Forces {
    private Human human;


    public Forces(Human h){
        this.human = h;
    }

    public List<Vector> calculateHumanForces(){

        List<Vector> humanForces = new ArrayList<>();

        for(Human h: this.human.getBoard().getHumans()){
            if(h.getId() != this.human.getId()){
                double humanForceValue = 5000 * this.g(h);

                if(humanForceValue > 0){
                    Vector force = Geometry.subtractVectors(new Vector(this.human.getPosition()), new Vector(h.getPosition()));
                    force.setValue(humanForceValue);
                    humanForces.add(force);
                }
            }
        }

        return humanForces;
    }


    public List<Vector> calculateWallForces(){

        List<Vector> wallForces = new ArrayList<>();

        for(Wall w : this.human.getBoard().getWalls()){
            double force = 5000 * this.g(w);
            Optional<Vector> forceVector = Geometry.vectorStraightPoint(this.human.getPosition(), new LineTwoPoints(w.getStartPosition(), w.getEndPosition()));
            if(forceVector.isPresent() && force > 0) {
                forceVector.get().setValue(force);
                wallForces.add(forceVector.get());
            }
        }

        return wallForces;
    }


    private double g(Human h){

        double r = h.getRadius() + this.human.getRadius();
        double d = h.getPosition().distance(this.human.getPosition());

        return d > r ? 0 : r - d;
    }


    private double g(Wall w){

        double r = this.human.getRadius();
        Optional<Vector> force = Geometry.vectorStraightPoint(this.human.getPosition(), new LineTwoPoints(w.getStartPosition(), w.getEndPosition()));

        if (force.isPresent()) {
            return r - force.get().getValue() > 0 ? human.getRadius() - force.get().getValue() : 0;
        }
        else{
            return 0;
        }

    }


    public List<Vector> calculateForces(){

        List<Vector> humanForces = this.calculateHumanForces();
        List<Vector> wallForces = this.calculateWallForces();

        Vector hforce = new Vector(Double.NaN, 0.0);

         for(Vector f : humanForces){
             hforce = Geometry.addVectors(hforce, f);
         }

         Vector wforce = new Vector(Double.NaN, 0.0);

         for(Vector f: wallForces){
             wforce = Geometry.addVectors(wforce, f);
         }

         List<Vector> forces = new ArrayList<>();
         forces.add(hforce);
         forces.add(wforce);

         return forces;
    }

    public double sumForcesValues(List<Vector> forces){

        double forceSum = 0;

        for(Vector f : forces){
            forceSum += f.getValue();
        }

        return forceSum;
    }
}


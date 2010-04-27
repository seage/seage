/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.sat.antColony;

import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.AntCreator;
import org.seage.metaheuristic.antcolony.Graph;

/**
 *
 * @author Zagy
 */
public class SatAntCreator extends AntCreator {

    public SatAntCreator(Graph graph, AntBrain brain, int numAnts, double qantumPheromone) {
        super(graph, brain, numAnts, qantumPheromone);
    }

    @Override
    public Ant[] createAnts() {
        SatAnt[] ants = new SatAnt[_numAnts];
        for (int i = 0; i < _numAnts; i++) {
            ants[i] = new SatAnt(_graph, _qantumPheromone, _brain);
        }
        return ants;
    }
}

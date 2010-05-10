/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.antcolony;

import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.AntCreator;
import org.seage.metaheuristic.antcolony.Graph;

/**
 *
 * @author Zagy
 */
public class TspAntCreator extends AntCreator {

    protected TspAntCreator(Graph graph, AntBrain brain, int numAnts, double qantumPheromone) {
        super(graph, brain, numAnts, qantumPheromone);
    }

    @Override
    protected Ant[] createAnts() {
        TspAnt[] ants = new TspAnt[_numAnts];
        for (int i = 0; i < _numAnts; i++) {
            ants[i] = new TspAnt(_graph, _qantumPheromone, _brain);
        }
        return ants;
    }
}

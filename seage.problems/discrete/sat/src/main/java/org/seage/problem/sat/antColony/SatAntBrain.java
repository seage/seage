/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antColony;

import java.util.Vector;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 *
 * @author Zagy
 */
public class SatAntBrain extends AntBrain {
    private Formula _formula;

    public SatAntBrain(Formula formula) {
        _formula = formula;
    }

    @Override
    public Edge getNextEdge(Vector<Node> visited, Node currentPosition) {
        double alpha = 2, beta = 1;
        double sum = 0;
        double[] probabilities = new double[currentPosition.getConnectionMap().size()];

        // for each Edges
        for (int i = 0; i < probabilities.length; i++) {
            Edge e = currentPosition.getConnectionMap().get(i);
            probabilities[i] = Math.pow(e.getLocalPheromone(), alpha) * Math.pow(1 / e.getEdgeLength(), beta);
            sum += probabilities[i];
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        return currentPosition.getConnectionMap().get(next(probabilities));
    }

    @Override
    protected int next(double[] probs) {
        double randomNumber = _rand.nextDouble();
        if (probs[0] > randomNumber) {
            return 0;
        } else {
            return 1;
        }
    }

    public double solutionCost(Vector<Edge> path){
        boolean[] solution = new boolean[_formula.getLiteralCount()];
        SatNode node;
        for (int i = 0; i < _formula.getLiteralCount(); i++) {
            node = (SatNode)path.get(i).getNode2();
            solution[i] = node.getValue();
        }
        return (FormulaEvaluator.evaluate(_formula, solution) + 1);
    }
}

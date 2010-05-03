/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antcolony;

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

    public SatAntBrain(double alpha, double beta, Formula formula) {
        super(alpha, beta);
        _formula = formula;
    }

    @Override
    public Edge getNextEdge(Vector<Node> visited, Node currentPosition) {
        double sum = 0;
        double[] probabilities = new double[currentPosition.getConnectionMap().size()];

        // for each Edges
        for (int i = 0; i < probabilities.length; i++) {
            Edge e = currentPosition.getConnectionMap().get(i);
            probabilities[i] = Math.pow(e.getLocalPheromone(), _alpha) * Math.pow(1 / e.getEdgeLength(), _beta);
            sum += probabilities[i];
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        return currentPosition.getConnectionMap().get(next(probabilities));
    }

    /**
     * Advantageous solutions determining
     * @param path - Actual ants path
     * @return - Advantageous solutions
     */
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

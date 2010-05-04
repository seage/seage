/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antcolony;

import java.util.List;
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

//    @Override
//    public Edge getNextEdge(Vector<Node> visited, Node currentPosition) {
//        double sum = 0;
//        double[] probabilities = new double[currentPosition.getConnectionMap().size()];
//
//        // for each Edges
//        for (int i = 0; i < probabilities.length; i++) {
//            Edge e = currentPosition.getConnectionMap().get(i);
//            probabilities[i] = Math.pow(e.getLocalPheromone(), _alpha) * Math.pow(1 / e.getEdgeLength(), _beta);
//            sum += probabilities[i];
//        }
//        for (int i = 0; i < probabilities.length; i++) {
//            probabilities[i] /= sum;
//        }
//        return currentPosition.getConnectionMap().get(next(probabilities));
//    }

    @Override
    public List<Edge> getAvailableEdges(Node currentPosition, Vector<Node> visited) {
        return currentPosition.getConnectionMap();
    }

    @Override
    protected Edge selectNextEdge(List<Edge> edges) {
        double[] probabilities = new double[edges.size()];
        double sum = 0;
        // for each Edges
        for (int i = 0; i < probabilities.length; i++) {
            Edge e = edges.get(i);
            probabilities[i] = Math.pow(e.getLocalPheromone(), _alpha) * Math.pow(1 / e.getEdgeLength(), _beta);
            sum += probabilities[i];
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        return edges.get(next(probabilities));
    }

    /**
     * Advantageous solutions determining
     * @param path - Actual ants path
     * @return - Advantageous solutions
     */
    @Override
    public double pathLength(Vector<Edge> path) {
        boolean[] solution = new boolean[_formula.getLiteralCount()];
        Node node;
        for (int i = 0; i < _formula.getLiteralCount(); i++) {
            node = (Node) path.get(i).getNode2();
            if (node.getId() < 0) {
                solution[i] = false;
            } else {
                solution[i] = true;
            }
        }
        return (FormulaEvaluator.evaluate(_formula, solution) + 1);
    }
}

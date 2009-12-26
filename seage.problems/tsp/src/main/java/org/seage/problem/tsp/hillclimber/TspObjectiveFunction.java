/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.hillclimber;

import org.seage.metaheuristic.hillclimber.IMove;
import org.seage.metaheuristic.hillclimber.IObjectiveFunction;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.tsp.City;

/**
 *
 * @author rick
 */
public class TspObjectiveFunction implements IObjectiveFunction {

    private City[] _cities;

    public TspObjectiveFunction(City[] cities) {
        _cities = cities;
    }

    public double evaluateMove(Solution s, IMove m) {
        Integer[] tour = null;
        if(s.switcher.equals("Random") || s.switcher.equals("random")){
            TspRandomSolution2 sol = (TspRandomSolution2) s;
            tour = sol.getTour().clone();
        }

        if(s.switcher.equals("Greedy") || s.switcher.equals("greedy")){
            TspGreedySolution2 sol = (TspGreedySolution2) s;
            tour = sol.getTour().clone();
        }

        TspMove move = (TspMove) m;

        if(m!=null)
        {   
            int ix1 = move.getIx1();
            int ix2 = move.getIx2();
            
            tour = modify(tour, ix1, ix2);
        }

        return length(tour);
    }

    private Integer[] modify(Integer[] tour, int ix1, int ix2) {
        int pom = tour[ix1];
        tour[ix1] = tour[ix2];
        tour[ix2] = pom;
        return tour;
    }

    private double length(Integer[] tour) {
        double lenght = 0, dx, dy;
        for (int i = 0; i < tour.length-1; i++) {
            dx = _cities[tour[i]].X - _cities[tour[i+1]].X;
            dy = _cities[tour[i]].Y - _cities[tour[i+1]].Y;
            lenght += Math.sqrt(dx * dx + dy * dy);
        }
        dx = _cities[tour[0]].X - _cities[tour[tour.length-1]].X;
        dy = _cities[tour[0]].Y - _cities[tour[tour.length-1]].Y;
        lenght += Math.sqrt(dx * dx + dy * dy);
        return lenght;
    }
}

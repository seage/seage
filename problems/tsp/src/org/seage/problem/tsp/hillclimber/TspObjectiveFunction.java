/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp.hillclimber;

import org.seage.metaheuristic.hillclimber.IMove;
import org.seage.metaheuristic.hillclimber.IObjectiveFunction;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.tsp.data.City;

/**
 *
 * @author rick
 */
public class TspObjectiveFunction implements IObjectiveFunction
{
    private City[] _cities;

    public TspObjectiveFunction(City[] cities)
    {
        _cities = cities;
    }


    public double evaluateMove(Solution s, IMove m)
    {
        TspSolution sol = (TspSolution)s;
        TspMove move = (TspMove)m;

        int[] tour = sol.getTour();

        int ix1 = move.getIx1();
        int ix2 = move.getIx2();

        int[] tour2 = modify(tour, ix1, ix2);

        return length(tour2);
    }

    private int[] modify(int[] tour, int ix1, int ix2)
    {
        int pom = tour[ix1];
        tour[ix1] = tour[ix2];
        tour[ix2] = pom;

        return tour;
    }

    //TODO: A - implement
    private double length(int[] tour)
    {
        double lenght = 0;
        for(int i=0;i<tour.length;i++)
        {
            double x = _cities[tour[i]].X;
            lenght = 1;
        }
        return lenght;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp.genetics;

import org.seage.metaheuristic.genetics.Evaluator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.tsp.data.City;

/**
 *
 * @author Rick
 */
public class TspEvaluator implements Evaluator
{
    private City[] _cities;
	public TspEvaluator(City[] cities)
	{
		_cities = (City[])cities.clone();
	}
    
    public double[] evaluate(Subject solution) throws Exception
    {
        return evaluate(solution.getGenome().getChromosome(0).getGeneArray());
    }

    private double[] evaluate(Object[] phenotypeSubject) throws Exception
    {
        double tourLength = 0;
        int numCities = _cities.length;

        for (int i = 0; i < numCities; i++)
        {				
            int k = i + 1;
            if (i == numCities - 1)
                k = 0;

            int ix1 = (Integer)phenotypeSubject[i];
            int ix2 = (Integer)phenotypeSubject[k];
            double x1 = _cities[ix1].X;
            double y1 = _cities[ix1].Y;
            double x2 = _cities[ix2].X;
            double y2 = _cities[ix2].Y;
            tourLength += Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        }
        return new double[] {tourLength };		
    }
    
    public int compare(double[] o1, double[] o2)
    {
        if(o1 == null)
            return -1;
        if(o2 == null)
            return 1;
        
        int length = o1.length<=o2.length ? o1.length : o2.length;
        
        for(int i=0;i<length;i++)
        {
            if(o1[i] < o2[i])
                return 1;
            if(o1[i] > o2[i])
                return -1;
        }            
        return 0;
    }
}

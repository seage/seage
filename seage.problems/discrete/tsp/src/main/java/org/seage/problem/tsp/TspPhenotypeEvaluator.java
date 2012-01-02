/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.data.ProblemInstanceInfo;

/**
 *
 * @author Rick
 */
public class TspPhenotypeEvaluator implements IPhenotypeEvaluator
{
    
    @Override
    public double[] evaluate(Object[] phenotypeSubject, ProblemInstanceInfo instance) throws Exception
    {
        double tourLength = 0;
        City[] cities = ((TspProblemInstance)instance).getCities();
        int numCities = cities.length;

        for (int i = 0; i < numCities; i++)
        {				
            int k = i + 1;
            if (i == numCities - 1)
                k = 0;

            int ix1 = (Integer)phenotypeSubject[i];
            int ix2 = (Integer)phenotypeSubject[k];
            double x1 = cities[ix1].X;
            double y1 = cities[ix1].Y;
            double x2 = cities[ix2].X;
            double y2 = cities[ix2].Y;
            tourLength += Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        }
        return new double[] {tourLength };		
    }
    
    @Override
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

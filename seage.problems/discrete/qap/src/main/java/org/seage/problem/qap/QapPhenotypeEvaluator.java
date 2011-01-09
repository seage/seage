/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.qap;

import org.seage.aal.IPhenotypeEvaluator;
import org.seage.aal.ProblemInstance;

/**
 *
 * @author Karel Durkota
 * student
 */
public class QapPhenotypeEvaluator implements IPhenotypeEvaluator
{
    
    @Override
    public double[] evaluate(Object[] phenotypeSubject, ProblemInstance instance) throws Exception
    {
        double assignPrice = 0;
        Double[][][] facilityLocation = ((QapProblemInstance)instance).getFacilityLocation();
        int numFacilities = facilityLocation[0][0].length;

        double price = 0;
        for(int i=0;i<facilityLocation[0][0].length;i++){
            for(int j=0;j<facilityLocation[0][0].length;i++){
                price+=facilityLocation[0][i][j]*facilityLocation[1][(Integer)phenotypeSubject[i]][(Integer)phenotypeSubject[j]];
            }
        }
        double addition=0;
        for(int i=0;i<facilityLocation[0][0].length;i++){
            addition+=facilityLocation[2][i][(Integer)phenotypeSubject[i]];
        }

        return new double[] {price+addition};
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

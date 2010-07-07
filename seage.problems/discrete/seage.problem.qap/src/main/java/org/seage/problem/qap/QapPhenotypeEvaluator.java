/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.qap;

import org.seage.aal.IPhenotypeEvaluator;

/**
 *
 * @author Karel Durkota
 */
public class QapPhenotypeEvaluator implements IPhenotypeEvaluator
{
    
    @Override
    public double[] evaluate(Object[] phenotypeSubject) throws Exception
    {
        double assignPrice = 0;
        Double[][] facilityLocation = QapProblemProvider.getFacilityLocation();
        int numFacilities = facilityLocation.length;

        for (int i = 0; i < numFacilities; i++)
        {				
            int k = i + 1;
            if (i == numFacilities - 1)
                k = 0;

            int location = (Integer)phenotypeSubject[i];
            assignPrice += facilityLocation[i][location];
        }
        return new double[] {assignPrice };
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

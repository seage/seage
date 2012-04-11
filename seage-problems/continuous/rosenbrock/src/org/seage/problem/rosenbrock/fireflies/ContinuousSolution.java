/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.rosenbrock.fireflies;

import org.seage.metaheuristic.fireflies.Solution;
import org.seage.metaheuristic.fireflies.SolutionAdapter;


/**
 *
 * @author Administrator
 */
public class ContinuousSolution extends SolutionAdapter{

    protected Double[] _assign;
    
    public ContinuousSolution(){} // Appease clone()

    public ContinuousSolution(int dim, double[] maxBound, double[] minBound){
        _assign = new Double[dim];
        for(int i=0;i<dim;i++){
            
        }
    }
    
    public ContinuousSolution(Double[] assign){
        _assign = assign;
    }

    public Object clone()
    {
		ContinuousSolution copy = (ContinuousSolution)super.clone();
		copy._assign = (Double[])this._assign.clone();
        return copy;
    }   // end clone

    public Double[] getAssign()
    {
            return _assign;
    }

    public void setAssign(Double[] assign)
    {
            _assign = assign;
    }
    
    public String toString()
    {
        StringBuffer s = new StringBuffer();

        s.append("[");
        for(int i=0;i<_assign.length-1;i++){
            s.append((_assign[i]+1));
            s.append(",");
        }
        s.append((_assign[_assign.length-1]+1)+"]");
        
        return s.toString();
    }   // end toString

    @Override
    public boolean equals(Solution in){
        ContinuousSolution q = (ContinuousSolution)in;
        for(int i=0;i<_assign.length;i++){
            if(_assign[i]!=q.getAssign()[i])
                return false;
        }
        return true;
    }
    
}

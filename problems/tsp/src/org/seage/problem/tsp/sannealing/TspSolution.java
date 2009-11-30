
package org.seage.problem.tsp.sannealing;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.tsp.data.City;

/**
 *
 * @author Jan Zmátlík
 */
public class TspSolution extends Solution
{
    private Integer[] _tour;
    private City[] _cities;

    public TspSolution(City[] cities)
    {
        _tour = new Integer[cities.length];
        _cities = cities;

        initTourOrder();
    }

    private void initTourOrder()
    {
        int tourLength = _tour.length;
        for(int i = 0; i < tourLength; i++) _tour[i] = i;
    }

    public Integer[] getTour()
    {
        return _tour;
    }

     public void setTour(Integer[] tour)
    {
        _tour = tour;
    }

    public City[] getCities()
    {
        return _cities;
    }
    
    public void setCities(City[] cities)
    {
        _cities = cities;
    }

    @Override
    public TspSolution clone()
    {
        TspSolution tspSolution = null;
        try
        {
            tspSolution = (TspSolution)super.clone();
            tspSolution.setTour( this._tour.clone() );
            tspSolution.setCities( this._cities.clone() );
        } catch (CloneNotSupportedException ex)
        {
            Logger.getLogger(TspSolution.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tspSolution;
    }

}

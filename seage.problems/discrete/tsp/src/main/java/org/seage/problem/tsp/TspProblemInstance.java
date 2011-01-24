/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp;

import org.seage.aal.ProblemInstance;

/**
 *
 * @author rick
 */
public class TspProblemInstance extends ProblemInstance{

    private City[] _cities;
    public TspProblemInstance(String instanceName, City[] cities) {
        super(instanceName);
        _cities = cities;
    }
    
    public City[] getCities(){return _cities;}
}

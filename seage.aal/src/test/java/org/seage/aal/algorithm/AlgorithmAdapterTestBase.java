/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.aal.algorithm;

import org.seage.aal.data.AlgorithmParams;

/**
 *
 * @author rick
 */
public class AlgorithmAdapterTestBase {
    protected IAlgorithmAdapter algorithm; 
    protected Object[][] solutions;
    protected AlgorithmParams algParams;
    
    protected void runAlgorithmTest() throws Exception
    {             
        algorithm.solutionsFromPhenotype(solutions);
        algorithm.setParameters(algParams);
        algorithm.startSearching();
        solutions = algorithm.solutionsToPhenotype();
        algorithm.solutionsFromPhenotype(solutions);
        algorithm.startSearching();
    }
}

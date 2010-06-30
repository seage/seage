/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.aal;

import org.seage.data.DataNode;

/**
 *
 * @author rick
 */
public interface IProblemProvider
{
    void initProblemInstance(DataNode problemParams, int instanceIx) throws Exception;
    Object[][] generateInitialSolutions(int numSolutions) throws Exception;
    IAlgorithmFactory createAlgorithmFactory(DataNode algorithmParams) throws Exception;
    public void visualize(Object[] solution) throws Exception;
}

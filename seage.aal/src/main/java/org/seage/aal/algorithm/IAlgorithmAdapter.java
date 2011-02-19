/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.aal.algorithm;

import org.seage.aal.data.AlgorithmParams;
import org.seage.aal.reporting.AlgorithmReport;

/**
 * IAlgorithmAdapter interface.
 *
 * @author Richard Malek
 */
public interface IAlgorithmAdapter extends Runnable
{
//    // Returns meta-data on the algorithm
//    //  |_ id
//    //  |_ name
//    //  |_ class
//    //  |_ parameters
//    DataNode getAlgorithmInfo() throws Exception;

    // Inits the algorithm.
    //void init(DataNode params);

    // Sets algorithm parameters
    void setParameters(AlgorithmParams params) throws Exception;

    // Runs the algorithm.
    void startSearching() throws Exception;

    // Stops the algorithm.
    void stopSearching() throws Exception;

    // Returns the runtime report collected during the algorithm run.
    AlgorithmReport getReport() throws Exception;

    // Converts solution from outer representation to the inner one.
    void solutionsFromPhenotype(Object[][] source) throws Exception;

    // Converts solution from inner representation to the outer one.
    // Solutions are ordered in descendent (best first) order according to the quality.
    Object[][] solutionsToPhenotype() throws Exception;
}

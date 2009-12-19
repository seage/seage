/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */

package org.seage.aal.sannealing;

import org.seage.aal.IAlgorithmAdapter;
import org.seage.data.DataNode;
import org.seage.metaheuristic.sannealing.ISimulatedAnnealingListener;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;

/**
 *
 * @author Jan Zmatlik
 */
public class SimulatedAnnealingAdapter implements IAlgorithmAdapter, ISimulatedAnnealingListener
{

    public DataNode getReport() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void solutionsFromPhenotype(Object[][] source) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object[][] solutionsToPhenotype() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void startSearching(DataNode params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stopSearching() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /****************************************************************/
    public void newBestSolutionFound(SimulatedAnnealingEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void newCurrentSolutionFound(SimulatedAnnealingEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void simulatedAnnealingStarted(SimulatedAnnealingEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void simulatedAnnealingStopped(SimulatedAnnealingEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}

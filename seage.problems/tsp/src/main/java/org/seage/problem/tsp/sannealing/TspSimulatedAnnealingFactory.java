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
package org.seage.problem.tsp.sannealing;

import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.data.DataNode;
import org.seage.problem.tsp.City;

/**
 *
 * @author Richard Malek
 */
public class TspSimulatedAnnealingFactory implements IAlgorithmFactory
{
    public TspSimulatedAnnealingFactory(DataNode params, City[] cities)
    {

    }

    public IAlgorithmAdapter createAlgorithm() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

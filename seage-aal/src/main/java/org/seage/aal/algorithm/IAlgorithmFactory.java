/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.seage.org/license/cpl-v10.html
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.aal.algorithm;

import java.io.Serializable;
import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.aal.data.ProblemConfig;

/**
 *
 * @author Richard Malek
 */
public interface IAlgorithmFactory extends Serializable
{
    Class getAlgorithmClass();

    /**
     *
     * @param config
     *  Config
     *      |_ Algorithm
     *      |   |_ Parameter
     *      |   |_ ...
     *      |   |_ Parameter
     *      |_ Instance
     * @return IAlgorithmAdapter
     * @throws Exception
     */
    IAlgorithmAdapter createAlgorithm(ProblemInstanceInfo instance, ProblemConfig config) throws Exception;
    //DataNode getAlgorithmParameters(DataNode params) throws Exception;
}

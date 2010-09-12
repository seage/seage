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
 *     - Added problem annotations
 */
package org.seage.problem.sat;

import org.seage.aal.Annotations;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IPhenotypeEvaluator;
import org.seage.aal.ProblemProvider;
import org.seage.data.DataNode;

/**
 *
 * @author Richard Malek
 */
@Annotations.ProblemId("sat")
@Annotations.ProblemName("Boolean Satisfiability Problem")
public class SatProblemProvider extends ProblemProvider
{

    public IAlgorithmAdapter initAlgorithm(DataNode params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IPhenotypeEvaluator initPhenotypeEvaluator() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initProblemInstance(DataNode params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void visualizeSolution(Object[] solution) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

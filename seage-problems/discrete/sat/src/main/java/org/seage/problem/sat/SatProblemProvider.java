/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 *     - Added problem annotations
 */
package org.seage.problem.sat;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.data.DataNode;

/**
 *
 * @author Richard Malek
 */
@Annotations.ProblemId("SAT")
@Annotations.ProblemName("Boolean Satisfiability Problem")
public class SatProblemProvider extends ProblemProvider
{

    public ProblemInstanceInfo initProblemInstance(ProblemConfig params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IPhenotypeEvaluator initPhenotypeEvaluator() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object[][] generateInitialSolutions(int numSolutions, ProblemInstanceInfo instance) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void visualizeSolution(Object[] solution, ProblemInstanceInfo instance) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}

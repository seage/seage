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
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.aal.algorithm;

import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;

/**
 * Problem provider interface
 * @author Richard Malek
 * 
 */
public interface IProblemProvider
{
    // Returns meta-data on the problem to be solved
    // @see ProblemInfo
    ProblemInfo getProblemInfo() throws Exception;

    // Returns the algorithm factory
    IAlgorithmFactory getAlgorithmFactory(String algId) throws Exception;

    // Initializes (reads) a problem instance.
    ProblemInstanceInfo initProblemInstance(ProblemConfig params) throws Exception;

    //  Initializes an evaluator of solutions in phenotype representation
    // (i.e. in general representation of a problem solution).
    IPhenotypeEvaluator initPhenotypeEvaluator() throws Exception;

    // Generates the very first solution(s).
    // Solutions can be random, hungry, or other.
    Object[][] generateInitialSolutions(int numSolutions, ProblemInstanceInfo instance) throws Exception;

    // Visualizes solution, usually produces a picture.
    public void visualizeSolution(Object[] solution, ProblemInstanceInfo instance) throws Exception;
}

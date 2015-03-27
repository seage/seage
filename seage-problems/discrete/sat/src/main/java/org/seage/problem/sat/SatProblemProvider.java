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
 *     - Added problem annotations
 */
package org.seage.problem.sat;

import java.io.FileInputStream;
import java.io.InputStream;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;

/**
 *
 * @author Richard Malek
 */
@Annotations.ProblemId("SAT")
@Annotations.ProblemName("Boolean Satisfiability Problem")
public class SatProblemProvider extends ProblemProvider
{
	@Override
	public Formula initProblemInstance(ProblemInstanceInfo instanceInfo) throws Exception
	{
		String type = instanceInfo.getValueStr("type");
        String path = instanceInfo.getValueStr("path");
        
        InputStream stream;
        if(type.equals("resource"))        
            stream = getClass().getResourceAsStream(path);
        else
            stream = new FileInputStream(path);
        
        Formula formula = null;
        
        try{
        	formula = new Formula(instanceInfo, FormulaReader.readClauses(stream));
        }catch(Exception ex){
            System.err.println("TspProblemProvider.initProblemInstance - readCities failed, path: " + path);
            throw ex;
        }
        
        return formula;
        
	}	
 

	@Override
	public IPhenotypeEvaluator initPhenotypeEvaluator(
			ProblemInstance problemInstance) throws Exception
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Object[][] generateInitialSolutions(ProblemInstance problemInstance,
			int numSolutions, long randomSeed) throws Exception
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}


	@Override
	public void visualizeSolution(Object[] solution,
			ProblemInstanceInfo problemInstanceInfo) throws Exception
	{
		throw new UnsupportedOperationException("Not supported yet.");		
	}
}

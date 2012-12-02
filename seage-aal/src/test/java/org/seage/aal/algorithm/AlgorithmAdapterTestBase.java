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

import java.util.Random;

import org.seage.aal.data.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;

/**
 *
 * @author rick
 */
public abstract class AlgorithmAdapterTestBase {
    protected AlgorithmAdapterTester _tester;
    
    protected IAlgorithmAdapter _algorithm; 
    protected Object[][] _solutions;
    protected AlgorithmParams _algParams;
    protected AlgorithmReport _algReport;
      
    public AlgorithmAdapterTestBase() throws Exception
    {
    	int NUMSOL=100;
    	int SOLLEN=100;
    	Random rnd = new Random(4);
    	_solutions = new Integer[NUMSOL][];
    	
    	for(int i=0;i<NUMSOL;i++)
    	{
    		_solutions[i] = new Integer[SOLLEN];
    		for(int j=0;j<SOLLEN;j++)
    		{
    			_solutions[i][j] = j+1;
    		}
    		for(int j=0;j<SOLLEN;j++)
    		{
    			int ix1 = rnd.nextInt(SOLLEN);
    			int ix2 = rnd.nextInt(SOLLEN);
    			Object a = _solutions[i][ix1]; 
    			_solutions[i][ix1] = _solutions[i][ix2];
    			_solutions[i][ix2] = a;
    		}
    	}
    }  

    public abstract void testPhenotype() throws Exception;

    public abstract void testAlgorithm() throws Exception;
    
    public abstract void testAlgorithmWithParamsAtZero() throws Exception;
    
    public abstract void testAsyncRunning() throws Exception;
    
    public abstract void testReport() throws Exception;

}

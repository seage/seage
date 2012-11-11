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

import org.seage.aal.data.AlgorithmParams;

/**
 *
 * @author rick
 */
public abstract class AlgorithmAdapterTestBase {
    protected AlgorithmAdapterTester _tester;
    
    protected IAlgorithmAdapter _algorithm; 
    protected Object[][] _solutions;
    protected AlgorithmParams _algParams;
      
    public AlgorithmAdapterTestBase() throws Exception
    {
        _solutions = new Integer[][] {
                new Integer[]{1,2,3,4,5}, 
                new Integer[]{2,3,4,5,1},
                new Integer[]{3,4,5,1,2},
                new Integer[]{4,5,1,2,3},
                new Integer[]{5,1,2,3,4}};
    }  

    public abstract void testPhenotype() throws Exception;

    public abstract void testAlgorithm() throws Exception;
    
    public abstract void testAlgorithmWithParamsAtZero() throws Exception;
    
    public abstract void testAsyncRunning() throws Exception;

}

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

package org.seage.aal.algorithm.particles;

import org.junit.Before;
import org.junit.Test;
import org.seage.aal.algorithm.*;
import org.seage.aal.algorithm.genetics.GeneticAlgorithmAdapter;
import org.seage.aal.data.AlgorithmParams;

/**
 *
 * @author rick
 */
public class ParticleSwarmAdapterTest extends AlgorithmAdapterTestBase{
    
    public ParticleSwarmAdapterTest() throws Exception
    {
        super();
        // TODO Auto-generated constructor stub
    }

    @Before
    public void initAlgorithm() throws Exception
    {
        _algorithm = new ParticleSwarmAdapter(null, null, false, "")
        {
            
            @Override
            public Object[][] solutionsToPhenotype() throws Exception
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                // TODO Auto-generated method stub
                
            }
        };        
        _algParams = new AlgorithmParams("");
        
        _tester = new AlgorithmAdapterTester(_algorithm, _solutions, _algParams);
    }
    
    @Override
    @Test
    public void testPhenotype() throws Exception
    {
        _tester.testPhenotype();
        
    }
    
    @Override
    @Test
    public void testAlgorithm() throws Exception
    {
        _tester.testAlgorithm();
    }

    @Override
    @Test
    public void testAlgorithmWithParamsAtZero() throws Exception
    {
        _tester.testAlgorithmWithParamsAtZero();
    }
    
    @Test
    @Override    
    public void testAsyncRunning() throws Exception
    {
        _tester.testAsyncRunning();        
    }
}

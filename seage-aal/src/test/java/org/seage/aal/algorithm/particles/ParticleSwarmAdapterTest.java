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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmAdapterTester;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.data.DataNode;
import org.seage.metaheuristic.particles.Particle;

/**
 *
 * @author rick
 */
@Ignore("Adapter class not fully implemented yet")
public class ParticleSwarmAdapterTest extends AlgorithmAdapterTestBase{
    
    public ParticleSwarmAdapterTest() throws Exception
    {
        super();        
    }

    @Before
    public void initAlgorithm() throws Exception
    {
        _algorithm = new ParticleSwarmAdapter(null, null, false, "")
        {
            
            @Override
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                _initialParticles = new Particle[source.length];
                
                for(int i=0;i<source.length;i++)
                {
                    _initialParticles[i] = new Particle(source[i].length);
                    double[] coords = new double[source[i].length];
                    for(int j=0;j<coords.length;j++)
                        coords[j] = (Integer)source[i][j];
                    _initialParticles[i].setCoords(coords);
                }
                
            }
            @Override
            public Object[][] solutionsToPhenotype() throws Exception
            {
                List<Object[]> result = new ArrayList<Object[]>();
                
                for(int i=0;i<_initialParticles.length;i++)
                {
                    Object[] coords = new Object[_initialParticles[i].getCoords().length];
                    for(int j=0;j<coords.length;j++)
                        coords[j] = (int)_initialParticles[i].getCoords()[j];
                    result.add(coords);
                }
                return (Object[][]) result.toArray(new Object[][]{});
            }
        };        
        
        _algParams = new AlgorithmParams();
        //_algParams.putValue("problemID", "ParticleSwarmTest");
        //_algParams.putValue("instance", "TestInstance");
        
        DataNode params = new DataNode("Parameters");
        params.putValue("maxIterationCount", 0);
        params.putValue("numSolutions", 0);
        params.putValue("maxVelocity", 0);
        params.putValue("minVelocity", 1);
        params.putValue("inertia", 0);
        params.putValue("alpha", 1);
        params.putValue("beta", 0); 
        
        _algParams.putDataNodeRef(params);
        
        _tester = new AlgorithmAdapterTester(_algorithm, /*_solutions,*/ _algParams);
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
    
    @Test
	@Override
	public void testReport() throws Exception
	{
		_tester.testReport();		
	}
    
    @Test
    @Override
	public void testAlgorithmWithParamsNull() throws Exception
	{
		_tester.testAlgorithmWithParamsNull();		
	}
}

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

package org.seage.problem.tsp.antcolony;

import java.util.ArrayList;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.antcolony.AntColonyAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspProblemInstance;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("AntColony")
@Annotations.AlgorithmName("AntColony")
public class TspAntColonyFactory implements IAlgorithmFactory
{
	public Class<AntColonyAdapter> getAlgorithmClass() {
        return AntColonyAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm(ProblemInstance instance) throws Exception
    {        
        IAlgorithmAdapter algorithm;
        City[] cities = ((TspProblemInstance)instance).getCities();
        TspGraph graph = new TspGraph(cities);
        AntBrain brain = new TspAntBrain(graph, graph.getNodes().get(1) );
        algorithm = new AntColonyAdapter(brain, graph)
		{			
        	@Override
			public void solutionsFromPhenotype(Object[][] source) throws Exception
			{
				_ants = new Ant[source.length];
				for(int i=0;i<_ants.length;i++)
				{
					ArrayList<Integer> nodes = new ArrayList<Integer>();
					for(int j=0;j<source[i].length;j++)
						nodes.add((Integer)source[i][j]+1);
					_ants[i] = new Ant(nodes);
				}								
			}
			@Override
			public Object[][] solutionsToPhenotype() throws Exception
			{
				Object[][] result = new Object[_ants.length][];
				for(int i=0;i<_ants.length;i++)
				{
					result[i] = new Integer[_ants[i].getNodeIDs().size()];
					for(int j=0;j<result[i].length;j++)
					{						
						result[i][j] = _ants[i].getNodeIDs().get(j)-1;//.toArray(new Integer[]{});
					}	
				}
				return result;
			}			
		};

        return algorithm;
    }

}
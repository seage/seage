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
package org.seage.aal.algorithm.antcolony;

import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.data.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.AntColonyEvent;
import org.seage.metaheuristic.antcolony.Graph;

/**
 * AntColony adapter class
 * @author rick
 *
 */
@AlgorithmParameters({
    @Parameter(name="numAnts", min=0, max=100000, init=100),
    @Parameter(name="iterationCount", min=0, max=1000000, init=100),
    @Parameter(name="alpha", min=1, max=10, init=1),
    @Parameter(name="beta", min=1, max=10, init=3),   
    @Parameter(name="defaultPheromone", min=00001, max=0.1, init=0.00001),
    @Parameter(name="qantumOfPheromone", min=1, max=1000, init=10),   
    @Parameter(name="localEvaporation", min=0.7, max=0.98, init=0.95)
})
public class AntColonyAdapter implements IAlgorithmAdapter
{
	private AntColony _antColony;
	private AntColonyListener _algorithmListener;
	
	public AntColonyAdapter(AntBrain brain, Graph graph)
	{
		_algorithmListener = new AntColonyListener();
		_antColony = new AntColony(brain, graph);
		_antColony.addAntColonyListener(_algorithmListener);
	}
	
    @Override
    public void setParameters(AlgorithmParams params) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void startSearching() throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void startSearching(boolean async) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stopSearching() throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isRunning()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public AlgorithmReport getReport() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void solutionsFromPhenotype(Object[][] source) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Object[][] solutionsToPhenotype() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    private class AntColonyListener implements IAlgorithmListener<AntColonyEvent>
    {

		@Override
		public void algorithmStarted(AntColonyEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void algorithmStopped(AntColonyEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void newBestSolutionFound(AntColonyEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void noChangeInValueIterationMade(AntColonyEvent e)
		{
			// TODO Auto-generated method stub
			
		}
    	
    }

}

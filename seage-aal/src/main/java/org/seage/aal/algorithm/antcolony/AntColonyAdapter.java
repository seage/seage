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
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.data.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.data.DataNode;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.antcolony.Ant;
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
public abstract class AntColonyAdapter extends AlgorithmAdapterImpl
{
	protected AntColony _antColony;
	private AntColonyListener _algorithmListener;
	protected Graph _graph;
	private AlgorithmParams _params;
	protected AntBrain _brain;
	protected Ant[] _ants;
	
	public AntColonyAdapter(AntBrain brain, Graph graph)
	{
		_params = null;
		_brain = brain;
		_graph = graph;
		_algorithmListener = new AntColonyListener();
		_antColony = new AntColony(graph);
		_antColony.addAntColonyListener(_algorithmListener);
	}
	
    @Override
    public void setParameters(AlgorithmParams params) throws Exception
    {
    	_params = params;
        _params.putValue("id", "TabuSearch");
        DataNode p = params.getDataNode("Parameters");
        //int numAnts = p.getValueInt("numAnts");
        int iterationCount = p.getValueInt("iterationCount");
        double alpha = p.getValueDouble("alpha");
        double beta = p.getValueDouble("beta");
        double defaultPheromone = p.getValueDouble("defaultPheromone");
        double quantumOfPheromone = p.getValueDouble("qantumOfPheromone");
        double localEvaporation = p.getValueDouble("localEvaporation");
        _antColony.setParameters(iterationCount, alpha, beta, quantumOfPheromone, defaultPheromone, localEvaporation);
        
    }

    @Override
    public void startSearching() throws Exception
    {
    	_antColony.startExploring(_graph.getNodes().values().iterator().next(), _ants);
        
    }

    @Override
    public void stopSearching() throws Exception
    {
        _antColony.stopExploring();
    }

    @Override
    public boolean isRunning()
    {
        return _antColony.isRunning();
    }

    @Override
    public AlgorithmReport getReport() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    private class AntColonyListener implements IAlgorithmListener<AntColonyEvent>
    {

		@Override
		public void algorithmStarted(AntColonyEvent e)
		{
			_algorithmStarted = true;
			
		}

		@Override
		public void algorithmStopped(AntColonyEvent e)
		{
			_algorithmStopped = true;
			
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

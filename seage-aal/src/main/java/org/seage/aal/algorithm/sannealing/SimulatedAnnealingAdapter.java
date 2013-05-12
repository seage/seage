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
 *     Jan Zmatlik
 *     - Initial implementation
 *     Richard Malek
 *     - Added annotations
 */

package org.seage.aal.algorithm.sannealing;

import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.data.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.data.DataNode;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.metaheuristic.sannealing.Solution;

/**
 * 
 * @author Jan Zmatlik
 */
@AlgorithmParameters({ 
	@Parameter(name = "annealCoeficient", min = 0.1, max = 1, init = 0.99), 
	@Parameter(name = "maxInnerIterations", min = 1, max = 100000, init = 100),
    @Parameter(name = "maxTemperature", min = 10, max = 1000000, init = 100), 
    @Parameter(name = "minTemperature", min = 0, max = 10000, init = 1),
    @Parameter(name = "maxOneStepAcceptedSolutions", min = 0, max = 1000000, init = 100), 
    @Parameter(name = "numSolutions", min = 1, max = 1, init = 1) })
public abstract class SimulatedAnnealingAdapter extends AlgorithmAdapterImpl
{
    protected SimulatedAnnealing _simulatedAnnealing;
    protected Solution[]         _solutions;
    private AlgorithmParams      _params;
    // private Solution _bestSolution;
    private AlgorithmReporter    _reporter;
    private String               _searchID;
    private long                 _numberOfIterationsDone             = 0;
    private long                 _numberOfNewSolutions           = 0;
    private long                 _lastImprovingIteration = 0;
    private double               _initObjectiveValue             = Double.MAX_VALUE;

    public SimulatedAnnealingAdapter(IObjectiveFunction objectiveFunction, IMoveManager moveManager, boolean maximizing, String searchID) throws Exception
    {
        _simulatedAnnealing = new SimulatedAnnealing(objectiveFunction, moveManager);
        _simulatedAnnealing.addSimulatedAnnealingListener(new SimulatedAnnealingListener());
        _reporter = new AlgorithmReporter(searchID);
    }

    @Override
    public void startSearching(AlgorithmParams params) throws Exception
    {
    	if(params==null)
    		throw new Exception("Parameters not set");
    	setParameters(params);
    	
        _reporter = new AlgorithmReporter(_searchID);
        _reporter.putParameters(_params);

        _numberOfIterationsDone = _numberOfNewSolutions = _lastImprovingIteration = 0;
        _simulatedAnnealing.startSearching(_solutions[0]);
    }
    
    @Override
    public void stopSearching() throws Exception
    {
        _simulatedAnnealing.stopSearching();

        while (isRunning())
            Thread.sleep(100);
    }

    @Override
    public boolean isRunning()
    {
        return _simulatedAnnealing.isRunning();
    }

    @Override
    public AlgorithmReport getReport() throws Exception
    {
        _reporter.putStatistics(_numberOfIterationsDone, _numberOfNewSolutions, _lastImprovingIteration, _initObjectiveValue, _simulatedAnnealing.getBestSolution().getObjectiveValue(),
                _simulatedAnnealing.getBestSolution().getObjectiveValue());

        return _reporter.getReport();
    }

    public void setParameters(AlgorithmParams params) throws Exception
    {
        _params = params;
        _params.putValue("id", "SimulatedAnnealing");

        DataNode p = params.getDataNode("Parameters");

        _simulatedAnnealing.setMaximalTemperature(p.getValueInt("maxTemperature"));
        _simulatedAnnealing.setMinimalTemperature(p.getValueDouble("minTemperature"));
        _simulatedAnnealing.setAnnealingCoefficient(p.getValueDouble("annealCoeficient"));
        _simulatedAnnealing.setMaximalInnerIterationCount(p.getValueInt("maxInnerIterations"));
        _simulatedAnnealing.setMaximalAcceptedSolutionsPerOneStepCount(p.getValueInt("maxOneStepAcceptedSolutions"));        
    }

    private class SimulatedAnnealingListener implements IAlgorithmListener<SimulatedAnnealingEvent>
    {
		@Override
		public void algorithmStarted(SimulatedAnnealingEvent e)
		{
			_algorithmStarted = true;
	        _initObjectiveValue = e.getSimulatedAnnealing().getCurrentSolution().getObjectiveValue();
		}

		@Override
		public void algorithmStopped(SimulatedAnnealingEvent e)
		{
			 _numberOfIterationsDone = e.getSimulatedAnnealing().getCurrentIteration();			
		}
		
		@Override
		public void newBestSolutionFound(SimulatedAnnealingEvent e)
	    {
	        try
	        {
	            Solution s = e.getSimulatedAnnealing().getBestSolution();
	
	            _reporter.putNewSolution(System.currentTimeMillis(), e.getSimulatedAnnealing().getCurrentIteration(), s.getObjectiveValue(), s.toString());
	            _numberOfNewSolutions++;
	            _lastImprovingIteration = e.getSimulatedAnnealing().getCurrentIteration();
	        }
	        catch (Exception ex)
	        {
	            ex.printStackTrace();
	        }
	    }		

		@Override
		public void iterationPerformed(SimulatedAnnealingEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void noChangeInValueIterationMade(SimulatedAnnealingEvent e)
		{
			// TODO Auto-generated method stub
			
		}
    }

}

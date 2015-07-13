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
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
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
	@Parameter(name = "numIterations", min = 1, max = 999999999, init = 100),
    @Parameter(name = "maxTemperature", min = 1000, max = 999999999, init = 100), 
    @Parameter(name = "minTemperature", min = 0, max = 99999, init = 1),
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

		_solutions[0] = _simulatedAnnealing.getBestSolution();
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

        _simulatedAnnealing.setMaximalTemperature(_params.getValueInt("maxTemperature"));
        _simulatedAnnealing.setMinimalTemperature(_params.getValueDouble("minTemperature"));
        //_simulatedAnnealing.setAnnealingCoefficient(_params.getValueDouble("annealCoeficient"));
        _simulatedAnnealing.setMaximalIterationCount(_params.getValueInt("numIterations"));
        //_simulatedAnnealing.setMaximalAcceptedSolutionsPerOneStepCount(_params.getValueInt("maxOneStepAcceptedSolutions"));        
    }

    private class SimulatedAnnealingListener implements IAlgorithmListener<SimulatedAnnealingEvent>
    {
		@Override
		public void algorithmStarted(SimulatedAnnealingEvent e)
		{
			_algorithmStarted = true;	        
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
	            
	            if(_numberOfNewSolutions==0)
	            	_initObjectiveValue = s.getObjectiveValue();
	            
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
			
		}

		@Override
		public void noChangeInValueIterationMade(SimulatedAnnealingEvent e)
		{
			
		}
    }

}

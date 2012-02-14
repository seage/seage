/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 *     Richard Malek
 *     - Added annotations
 */

package org.seage.aal.algorithm.sannealing;

import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.data.AlgorithmParams;
import org.seage.data.DataNode;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.ISimulatedAnnealingListener;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmatlik
 */
@AlgorithmParameters({
    @Parameter(name="numSolutions", min=1, max=1, init=1),
    @Parameter(name="maxTemperature", min=10, max=1000000, init=100),
    @Parameter(name="minTemperature", min=0, max=10000, init=1),
    @Parameter(name="annealCoeficient", min=0.1, max=1, init=0.99),
    @Parameter(name="maxInnerIterations", min=1, max=1000000, init=100),
    @Parameter(name="numInnerSuccesses", min=0, max=100000, init=100)
})
public abstract class SimulatedAnnealingAdapter extends AlgorithmAdapterImpl implements ISimulatedAnnealingListener
{
    protected SimulatedAnnealing _simulatedAnnealing;
    protected Solution _initialSolution;
    private AlgorithmParams _params;
    //private Solution _bestSolution;
    private AlgorithmReporter _reporter;
    private String _searchID;    
    private long _numberOfIterations = 0;
    private long _numberOfNewSolutions = 0;
    private long _lastIterationNumberNewSolution = 0;
    private double _initObjectiveValue = Double.MAX_VALUE;

    public SimulatedAnnealingAdapter( Solution initialSolution,
                                IObjectiveFunction  objectiveFunction,
                                IMoveManager moveManager,
                                boolean maximizing,
                                String searchID) throws Exception
    {
        _initialSolution = initialSolution;
        _simulatedAnnealing = new SimulatedAnnealing( objectiveFunction , moveManager );

        _reporter = new AlgorithmReporter( searchID );
    }

    public void startSearching() throws Exception {
        _reporter = new AlgorithmReporter( _searchID );
        _reporter.putParameters( _params );

        _numberOfIterations = _numberOfNewSolutions = _lastIterationNumberNewSolution = 0;
        _simulatedAnnealing.startSearching( _initialSolution );
    }
    
    public void stopSearching() throws Exception {
        _simulatedAnnealing.stopSearching();
        
        while(isRunning())
            Thread.sleep(100);
    }
    
    public boolean isRunning() {
        return _simulatedAnnealing.isRunning();
    }

    public AlgorithmReport getReport() throws Exception
    {
          _reporter.putStatistics (
                  _numberOfIterations,
                  _numberOfNewSolutions,
                  _lastIterationNumberNewSolution,
                  _initObjectiveValue,
                  _simulatedAnnealing.getBestSolution().getObjectiveValue(),
                  _simulatedAnnealing.getBestSolution().getObjectiveValue()
                 );

        return _reporter.getReport();
    }

    public Solution getInitialSolution() {
        return _initialSolution;
    }

    public void setInitialSolution(Solution _initialSolution) {
        this._initialSolution = _initialSolution;
    }

    public void setParameters(AlgorithmParams params) throws Exception
    {
        _params = params;

        DataNode p = params.getDataNode("Parameters");
        
        _simulatedAnnealing.setMaximalTemperature( p.getValueInt("maxTemperature") );
        _simulatedAnnealing.setMinimalTemperature( p.getValueDouble("minTemperature") );
        _simulatedAnnealing.setAnnealingCoefficient( p.getValueDouble("annealCoeficient") );
        _simulatedAnnealing.setMaximalIterationCount( p.getValueInt("maxInnerIterations") );
        _simulatedAnnealing.setMaximalSuccessIterationCount( p.getValueInt("numInnerSuccesses" ) );
        _simulatedAnnealing.addSimulatedAnnealingListener( this );
    }


    //############################ EVENTS ###############################//
    public void newBestSolutionFound(SimulatedAnnealingEvent e) 
    {
        try
        {
            //System.out.println( e.getSimulatedAnnealing().getBestSolution().getObjectiveValue());

            Solution s = e.getSimulatedAnnealing().getBestSolution();

            _reporter.putNewSolution(System.currentTimeMillis(), e.getSimulatedAnnealing().getCurrentIteration(), s.getObjectiveValue(), s.toString());
            _numberOfNewSolutions++;
            _lastIterationNumberNewSolution = e.getSimulatedAnnealing().getCurrentIteration();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void newIterationStarted(SimulatedAnnealingEvent e) {
        _numberOfIterations = e.getSimulatedAnnealing().getCurrentIteration();
    }

    public void simulatedAnnealingStarted(SimulatedAnnealingEvent e) {
        _initObjectiveValue = e.getSimulatedAnnealing().getCurrentSolution().getObjectiveValue();
    }

    public void simulatedAnnealingStopped(SimulatedAnnealingEvent e) {
    }

}

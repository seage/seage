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
 */

package org.seage.aal.sannealing;

import org.seage.aal.AlgorithmReport;
import org.seage.aal.AlgorithmReporter;
import org.seage.aal.IAlgorithmAdapter;
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
public abstract class SimulatedAnnealingAdapter implements IAlgorithmAdapter, ISimulatedAnnealingListener
{
    protected SimulatedAnnealing _simulatedAnnealing;
    protected Solution _initialSolution;

    private Solution _bestSolution;
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

    public void startSearching(DataNode params) throws Exception {
        _reporter = new AlgorithmReporter( _searchID );
        _reporter.putParameters( params );

        _numberOfIterations = _numberOfNewSolutions = _lastIterationNumberNewSolution = 0;

        setParameters( params );
        _simulatedAnnealing.startSearching( _initialSolution );
    }

    public AlgorithmReport getReport() throws Exception
    {
          _reporter.putStatistics (
                  _numberOfIterations,
                  _numberOfNewSolutions,
                  _lastIterationNumberNewSolution,
                  _initObjectiveValue,
                  _bestSolution.getObjectiveValue(),
                  _bestSolution.getObjectiveValue()
                 );

        return _reporter.getReport();
    }

    public Solution getInitialSolution() {
        return _initialSolution;
    }

    public void setInitialSolution(Solution _initialSolution) {
        this._initialSolution = _initialSolution;
    }

    private void setParameters(DataNode param) throws Exception
    {
        _simulatedAnnealing.setMaximalTemperature( param.getValueInt("maxTemperature") );
        _simulatedAnnealing.setMinimalTemperature( param.getValueDouble("minTemperature") );
        _simulatedAnnealing.setAnnealingCoefficient( param.getValueDouble("annealCoeficient") );
        _simulatedAnnealing.setMaximalIterationCount( param.getValueInt("maxInnerIterations") );
        _simulatedAnnealing.setMaximalSuccessIterationCount( param.getValueInt("numInnerSuccesses" ) );
        _simulatedAnnealing.addSimulatedAnnealingListener( this );
    }

    public void stopSearching() throws Exception {
        _simulatedAnnealing.stopSearching();
    }

    //############################ EVENTS ###############################//
    public void newBestSolutionFound(SimulatedAnnealingEvent e) 
    {
        try
        {
            System.out.println( e.getSimulatedAnnealing().getBestSolution().getObjectiveValue());

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
        _bestSolution = e.getSimulatedAnnealing().getBestSolution();
    }

}

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
public class SimulatedAnnealingAdapter implements IAlgorithmAdapter, ISimulatedAnnealingListener
{
    private SimulatedAnnealing _simulatedAnnealing;
    private AlgorithmReporter _reporter;
    private String _searchID;
    private Solution _initialSolution;
    private Solution _bestSolution;

    public SimulatedAnnealingAdapter( Solution initialSolution,
                                IObjectiveFunction  objectiveFunction,
                                IMoveManager moveManager,
                                boolean maximizing,
                                String searchID)
    {
        _initialSolution = initialSolution;
        _simulatedAnnealing = new SimulatedAnnealing( objectiveFunction , moveManager );
    }

    public void startSearching(DataNode params) throws Exception {
        _reporter = new AlgorithmReporter( _searchID );
        _reporter.putParameters( params );

        setParameters( params );
        _simulatedAnnealing.startSearching( _initialSolution );

    }

    public DataNode getReport() throws Exception
    {
//        int num = _solutions.length;// > 10 ? 10 : solutions.length;
//        double avg = 0;
//        for (int i = 0; i < num; i++)
//            avg +=_solutions[i].getObjectiveValue()[0];
//        avg /= num;
//
//        _reporter.putStatistics(_statNumIter, _statNumNewSol, _statLastIterNewSol, _statInitObjVal, avg, _statEndObjVal);
//
//        return _reporter.getReport();
        return null;
    }

    public void solutionsFromPhenotype(Object[][] source) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object[][] solutionsToPhenotype() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void setParameters(DataNode param) throws Exception
    {
        _simulatedAnnealing.setMaximalTemperature( param.getValueInt("maxTemperature") );
        _simulatedAnnealing.setMinimalTemperature( param.getValueDouble("minTemperature") );
        _simulatedAnnealing.setAnnealingCoefficient( param.getValueDouble("annealCoeficient") );
        _simulatedAnnealing.setMaximalIterationCount( param.getValueInt("maxInnerIterations") );
        _simulatedAnnealing.setMaximalSuccessIterationCount( param.getValueInt("numInnerSuccesses" ));
        _simulatedAnnealing.addSimulatedAnnealingListener( this );
    }

    public void stopSearching() throws Exception {
        _simulatedAnnealing.stopSearching();
    }

    /****************************************************************/
    public void newBestSolutionFound(SimulatedAnnealingEvent e) {
        System.out.println("Best+: " + e.getSimulatedAnnealing().getBestSolution().getObjectiveValue());
    }

    public void newCurrentSolutionFound(SimulatedAnnealingEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void simulatedAnnealingStarted(SimulatedAnnealingEvent e) {
        System.out.println("SA Started");
    }

    public void simulatedAnnealingStopped(SimulatedAnnealingEvent e) {
        System.out.println("SA Stopped");
        _bestSolution = e.getSimulatedAnnealing().getBestSolution();
    }


}

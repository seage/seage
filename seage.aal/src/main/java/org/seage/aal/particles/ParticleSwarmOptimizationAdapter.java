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

package org.seage.aal.particles;

import org.seage.aal.AlgorithmReport;
import org.seage.aal.AlgorithmReporter;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.data.DataNode;
import org.seage.metaheuristic.particles.IVelocityManager;
import org.seage.metaheuristic.particles.IObjectiveFunction;
import org.seage.metaheuristic.particles.IParticleSwarmOptimizationListener;
import org.seage.metaheuristic.particles.ParticleSwarmOptimization;
import org.seage.metaheuristic.particles.ParticleSwarmOptimizationEvent;
import org.seage.metaheuristic.particles.Solution;
/**
 *
 * @author Jan Zmatlik
 */
public abstract class ParticleSwarmOptimizationAdapter implements IAlgorithmAdapter, IParticleSwarmOptimizationListener
{
    protected ParticleSwarmOptimization _particleSwarmOptimization;
    protected Solution[] _initialSolutions;

    private Solution _bestSolution;
    private AlgorithmReporter _reporter;
    private String _searchID;    
    private long _numberOfIterations = 0;
    private long _numberOfNewSolutions = 0;
    private long _lastIterationNumberNewSolution = 0;
    private double _initObjectiveValue = Double.MAX_VALUE;

    public ParticleSwarmOptimizationAdapter( Solution[] initialSolutions,
                                IObjectiveFunction  objectiveFunction,
                                IVelocityManager velocityManager,
                                boolean maximizing,
                                String searchID) throws Exception
    {
        _initialSolutions = initialSolutions;
        _particleSwarmOptimization = new ParticleSwarmOptimization( objectiveFunction , velocityManager );

        _reporter = new AlgorithmReporter( searchID );
    }

    public void startSearching(DataNode params) throws Exception {
        _reporter = new AlgorithmReporter( _searchID );
        _reporter.putParameters( params );

        setParameters( params );
        _particleSwarmOptimization.startSearching( _initialSolutions );
    }

    public AlgorithmReport getReport() throws Exception
    {
        // TODO: A - change AVG objective value
          _reporter.putStatistics (
                  _numberOfIterations,
                  _numberOfNewSolutions,
                  _lastIterationNumberNewSolution,
                  _initObjectiveValue,
                  0.0,//_bestSolution.getObjectiveValue()
                  0.0//_bestSolution.getObjectiveValue()
                 );

        return _reporter.getReport();
    }

//    public Solution getInitialSolution() {
//        return _initialSolution;
//    }
//
//    public void setInitialSolution(Solution _initialSolution) {
//        this._initialSolution = _initialSolution;
//    }

    private void setParameters(DataNode param) throws Exception
    {
        _particleSwarmOptimization.setMaximalIterationCount( param.getValueLong("maxIterationCount") );
        _particleSwarmOptimization.setMaximalVelocity( param.getValueDouble("maxVelocity") );

        _particleSwarmOptimization.addParticleSwarmOptimizationListener( this );
    }

    public void stopSearching() {
        _particleSwarmOptimization.stopSearching();
    }

    //############################ EVENTS ###############################//
    public void newBestSolutionFound(ParticleSwarmOptimizationEvent e) {
//        System.out.println( e.getParticleSwarmOptimization().getBestSolution().getObjectiveValue());
        _numberOfNewSolutions++;
        _lastIterationNumberNewSolution = _numberOfIterations;
    }

    public void newIterationStarted(ParticleSwarmOptimizationEvent e) {
        _numberOfIterations++;
    }

    public void particleSwarmOptimizationStarted(ParticleSwarmOptimizationEvent e) {
//        _initObjectiveValue = e.getParticleSwarmOptimization().getCurrentSolution().getObjectiveValue();
    }

    public void particleSwarmOptimizationStopped(ParticleSwarmOptimizationEvent e) {
        //_bestSolution = e.getParticleSwarmOptimization().getBestSolution();
    }
    
}

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

package org.seage.aal.particles;

import org.seage.aal.AlgorithmReport;
import org.seage.aal.AlgorithmReporter;
import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.data.DataNode;
import org.seage.metaheuristic.particles.IObjectiveFunction;
import org.seage.metaheuristic.particles.IParticleSwarmListener;
import org.seage.metaheuristic.particles.Particle;
import org.seage.metaheuristic.particles.ParticleSwarm;
import org.seage.metaheuristic.particles.ParticleSwarmEvent;

/**
 *
 * @author Jan Zmatlik
 */
@AlgorithmParameters({
    @Parameter(name="maxIterationCount", min=0, max=1000000, init=1000),
    @Parameter(name="numSolutions", min=1, max=1000000, init=100),
    @Parameter(name="maxVelocity", min=0, max=10000, init=1),
    @Parameter(name="minVelocity", min=0, max=10000, init=1),
    @Parameter(name="inertia", min=0, max=10000, init=10),
    @Parameter(name="alpha", min=0, max=100, init=1),
    @Parameter(name="beta", min=0, max=100, init=1)
})
public abstract class ParticleSwarmAdapter implements IAlgorithmAdapter, IParticleSwarmListener
{
    protected ParticleSwarm _particleSwarm;
    protected Particle[] _initialParticles;
    private Particle _bestParticle;

    private AlgorithmReporter _reporter;
    private String _searchID;

    private long _numberOfIterations = 0;
    private long _numberOfNewSolutions = 0;
    private long _lastIterationNumberNewSolution = 0;
    private double _initObjectiveValue = Double.MAX_VALUE;
    private double _evaluationsOfParticles = 0;
    private double _avgOfBestSolutions = 0;

    public ParticleSwarmAdapter( Particle[] initialParticles,
                                IObjectiveFunction  objectiveFunction,
                                boolean maximizing,
                                String searchID) throws Exception
    {
        _initialParticles = initialParticles;
        _particleSwarm = new ParticleSwarm( objectiveFunction );

        _reporter = new AlgorithmReporter( searchID );
    }

    public void startSearching(DataNode params) throws Exception {
        _reporter = new AlgorithmReporter( _searchID );
        _reporter.putParameters( params );

        setParameters( params );
        _particleSwarm.startSearching( _initialParticles );
    }

    public AlgorithmReport getReport() throws Exception
    {
          _reporter.putStatistics (
                  _numberOfIterations,
                  _numberOfNewSolutions,
                  _lastIterationNumberNewSolution,
                  _initObjectiveValue,
                  _avgOfBestSolutions,
                  _bestParticle.getEvaluation()
                 );

        return _reporter.getReport();
    }

    private void setParameters(DataNode param) throws Exception
    {
        _particleSwarm.setMaximalIterationCount( param.getValueLong("maxIterationCount") );
        _particleSwarm.setMaximalVelocity( param.getValueDouble("maxVelocity") );
        _particleSwarm.setMinimalVelocity( param.getValueDouble("minVelocity") );
        _particleSwarm.setInertia( param.getValueDouble("inertia") );
        _particleSwarm.setAlpha( param.getValueDouble("alpha") );
        _particleSwarm.setBeta( param.getValueDouble("beta") );

        _particleSwarm.addParticleSwarmOptimizationListener( this );
    }

    public void stopSearching() {
        _particleSwarm.stopSearching();
    }

    //############################ EVENTS ###############################//
    public void newBestSolutionFound(ParticleSwarmEvent e) {
        _bestParticle = e.getParticleSwarm().getBestParticle();
        _numberOfNewSolutions++;
        _evaluationsOfParticles += _bestParticle.getEvaluation();
        _avgOfBestSolutions = _evaluationsOfParticles / _numberOfNewSolutions;
        _lastIterationNumberNewSolution = _numberOfIterations;
    }

    public void newIterationStarted(ParticleSwarmEvent e) {
        _numberOfIterations++;
    }

    public void particleSwarmStarted(ParticleSwarmEvent e) {
        _initObjectiveValue = e.getParticleSwarm().getBestParticle().getEvaluation();
    }

    public void particleSwarmStopped(ParticleSwarmEvent e) {
        _bestParticle = e.getParticleSwarm().getBestParticle();
    }
    
}

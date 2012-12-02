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

package org.seage.aal.algorithm.particles;

import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.data.AlgorithmParams;
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
public abstract class ParticleSwarmAdapter  extends AlgorithmAdapterImpl implements IParticleSwarmListener
{
    protected ParticleSwarm _particleSwarm;
    protected Particle[] _initialParticles;
    private Particle _bestParticle;
    private AlgorithmParams _params;
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

    @Override
    public void startSearching(AlgorithmParams params) throws Exception 
    {
    	if(params==null)
    		throw new Exception("Parameters not set");
    	setParameters(params);
    	
        _reporter = new AlgorithmReporter( _searchID );
        _reporter.putParameters( _params );
        _particleSwarm.startSearching( _initialParticles );
    }
    
    @Override
    public void stopSearching() throws Exception {
        _particleSwarm.stopSearching();
        
        while(isRunning())
            Thread.sleep(100);
    }
    
    @Override
    public boolean isRunning() {
        return _particleSwarm.isRunning();
    }

    @Override
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

    public void setParameters(AlgorithmParams params) throws Exception
    {
        _params = params;
        _params.putValue("id", "ParticleSwarm");
        DataNode p = params.getDataNode("Parameters");
        _particleSwarm.setMaximalIterationCount( p.getValueLong("maxIterationCount") );
        _particleSwarm.setMaximalVelocity( p.getValueDouble("maxVelocity") );
        _particleSwarm.setMinimalVelocity( p.getValueDouble("minVelocity") );
        _particleSwarm.setInertia( p.getValueDouble("inertia") );
        _particleSwarm.setAlpha( p.getValueDouble("alpha") );
        _particleSwarm.setBeta( p.getValueDouble("beta") );

        _particleSwarm.addParticleSwarmOptimizationListener( this );
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

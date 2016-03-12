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
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
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
        @Parameter(name = "numSolutions", min = 10, max = 1000, init = 100),
        @Parameter(name = "iterationCount", min = 10, max = 1000000, init = 100),
        @Parameter(name = "alpha", min = 1, max = 10, init = 1),
        @Parameter(name = "beta", min = 1, max = 10, init = 3),
        @Parameter(name = "defaultPheromone", min = 0.00001, max = 1.0, init = 0.00001),
        @Parameter(name = "qantumOfPheromone", min = 1, max = 1000, init = 10),
        @Parameter(name = "localEvaporation", min = 0.5, max = 0.98, init = 0.95)
})
public abstract class AntColonyAdapter extends AlgorithmAdapterImpl
{
    protected AntColony _antColony;
    //private AntColonyListener _algorithmListener;
    protected Graph _graph;
    private AlgorithmParams _params;
    protected AntBrain _brain;
    protected Ant[] _ants;

    private AlgorithmReporter _reporter;
    private long _statNumIterationsDone;
    private long _statNumNewBestSolutions;
    private long _statLastImprovingIteration;
    private double _initialSolutionValue;
    private double _bestSolutionValue;
    public double _averageSolutionValue;

    public AntColonyAdapter(AntBrain brain, Graph graph)
    {
        _params = null;
        _brain = brain;
        _graph = graph;
        //_algorithmListener = ;
        _antColony = new AntColony(graph, brain);
        _antColony.addAntColonyListener(new AntColonyListener());
    }

    public void setParameters(AlgorithmParams params) throws Exception
    {
        _params = params;

        int iterationCount = _params.getValueInt("iterationCount");
        double alpha = _params.getValueDouble("alpha");
        double beta = _params.getValueDouble("beta");
        double defaultPheromone = _params.getValueDouble("defaultPheromone");
        double quantumOfPheromone = _params.getValueDouble("qantumOfPheromone");
        double localEvaporation = _params.getValueDouble("localEvaporation");
        _antColony.setParameters(iterationCount, alpha, beta, quantumOfPheromone, defaultPheromone, localEvaporation);

    }

    @Override
    public void startSearching(AlgorithmParams params) throws Exception
    {
        if (params == null)
            throw new Exception("Parameters not set");
        setParameters(params);

        _reporter = new AlgorithmReporter("AntColony");
        _reporter.putParameters(_params);

        _statLastImprovingIteration = 0;
        _statNumIterationsDone = 0;
        _statNumNewBestSolutions = 0;
        _averageSolutionValue = 0;
        _initialSolutionValue = _bestSolutionValue = Double.MAX_VALUE;

        _antColony.startExploring(_graph.getNodes().values().iterator().next(), _ants);

    }

    @Override
    public void stopSearching() throws Exception
    {
        _antColony.stopExploring();

        while (isRunning())
            Thread.sleep(100);
    }

    @Override
    public boolean isRunning()
    {
        return _antColony.isRunning();
    }

    @Override
    public AlgorithmReport getReport() throws Exception
    {
        _reporter.putStatistics(_statNumIterationsDone,
                _statNumNewBestSolutions, _statLastImprovingIteration,
                _initialSolutionValue, _averageSolutionValue, _bestSolutionValue);
        return _reporter.getReport();
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
            _statNumIterationsDone = e.getAntColony().getCurrentIteration();
        }

        @Override
        public void newBestSolutionFound(AntColonyEvent e)
        {
            AntColony alg = e.getAntColony();

            _averageSolutionValue = _bestSolutionValue = alg.getGlobalBest();
            _statLastImprovingIteration = alg.getCurrentIteration();
            _averageSolutionValue = _bestSolutionValue = alg.getGlobalBest();

            if (_statNumNewBestSolutions == 0)
                _initialSolutionValue = _bestSolutionValue;

            _statNumNewBestSolutions++;

            try
            {
                _reporter.putNewSolution(System.currentTimeMillis(),
                        alg.getCurrentIteration(),
                        alg.getGlobalBest(),
                        "TBD");
            }
            catch (Exception ex)
            {
                _logger.warn(ex.getMessage(), ex);
            }
        }

        @Override
        public void noChangeInValueIterationMade(AntColonyEvent e)
        {
        }

        @Override
        public void iterationPerformed(AntColonyEvent e)
        {
            if (e.getAntColony().getCurrentIteration() == 1)
                _initialSolutionValue = e.getAntColony().getGlobalBest();
        }

    }

}

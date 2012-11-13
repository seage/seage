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
 *     - Event raising extension
 *     Martin Zaloga
 *     - Reimplementation
 */
package org.seage.metaheuristic.antcolony;

import java.util.*;

import org.seage.metaheuristic.AlgorithmEventProducerBase;
import org.seage.metaheuristic.IAlgorithmListener;

/**
 * 
 * @author Martin Zaloga
 */
public class AntColony
{
	private AlgorithmEventProducerBase<IAlgorithmListener<AntColonyEvent>, AntColonyEvent> _eventProducer;
	private double _roundBest;
	private double _globalBest;
	private List<Edge> _bestPath;
	private List<List<Edge>> _reports;
	private Graph _graph;
	private Ant[] _ants;

	private int _numIterations;
	private boolean _started, _stopped;
	private boolean _keepRunning;
	private int _currentIteration;
	private double _alpha;
	private double _beta;
	private double _quantumPheromone;

	public AntColony(Graph graph)
	{
		_eventProducer = new AlgorithmEventProducerBase<IAlgorithmListener<AntColonyEvent>, AntColonyEvent>(new AntColonyEvent(this));
		_graph = graph;

		_roundBest = Double.MAX_VALUE;
		_globalBest = Double.MAX_VALUE;		
		_started = false;
		_stopped = false;
	}
	
	public void addAntColonyListener(IAlgorithmListener<AntColonyEvent> listener)
	{
		_eventProducer.addGeneticSearchListener(listener);
	}

	public void removeAntColonyListener(IAlgorithmListener<AntColonyEvent> listener)
	{
		_eventProducer.removeGeneticSearchListener(listener);
	}
	
	public void setParameters(int numIterations, double alpha, double beta, double quantumPheromone, double defaultPheromone, double evaporCoeff) throws Exception
	{
		_numIterations = numIterations;
		_alpha = alpha;
		_beta = beta;
		_quantumPheromone = quantumPheromone;
		//_antBrain.setParameters(alpha, beta, quantumPheromone);
		_graph.setEvaporCoeff(evaporCoeff);
		_graph.setDefaultPheromone(defaultPheromone);
	}

	/**
	 * Main part of ant-colony algorithm
	 */
	public void startExploring(Node startingNode, Ant[] ants) throws Exception
	{
		_ants = ants;
		for(Ant a : _ants)
			a.getBrain().setParameters(_alpha, _beta, _quantumPheromone);
		
		_reports = new ArrayList<List<Edge>>();
		_started = _keepRunning = true;
		_stopped = false;		
		_currentIteration = 0;
		_eventProducer.fireAlgorithmStarted();
		while(_currentIteration++ < _numIterations && _keepRunning)
		//for (int i = 0; i < _numIterations && _keepRunning == true; i++)
		{
			for (int j = 0; j < _ants.length && _keepRunning; j++)
			{
				_reports.add(_ants[j].explore(startingNode));
			}
			solveRound();
			_graph.evaporate();
			for (int j = 0; j < _ants.length && _keepRunning; j++)
			{
				_ants[j].leavePheromone();
			}
		}
		_eventProducer.fireAlgorithmStopped();
		_stopped = true;
	}
	
	public void stopExploring()
	{
		_keepRunning = false;
	}

//	private void createAnts() throws Exception
//	{
//		_ants = new Ant[_numAnts];
//		_antBrain.setParameters(_graph.getNodeList().size(), _alpha, _beta);
//		for (int i = 0; i < _numAnts; i++)
//			_ants[i] = new Ant(_antBrain, _graph, _quantumPheromone);
//
//	}

	/**
	 * Evaluation for each iteration
	 */
	private void solveRound()
	{
		double pathLength = 0;
		int counter = 0;
		for (List<Edge> vector : _reports)
		{
			if (_bestPath == null)
			{
				_bestPath = vector;
			}
			pathLength = _ants[counter]._distanceTravelled;
			counter++;
			if (pathLength < _roundBest)
			{
				_roundBest = pathLength;
			}
			if (_roundBest < _globalBest)
			{
				_globalBest = _roundBest;
				_bestPath = vector;
				_eventProducer.fireNewBestSolutionFound();
			}
		}
		 //System.out.println("This round best was  : " + _roundBest);
		 //System.out.println("The global best was : " + _globalBest + "\n");
		_roundBest = Double.MAX_VALUE;
		_reports.clear();
	}

	/**
	 * The best solution
	 * 
	 * @return The best path
	 */
	public List<Edge> getBestPath()
	{
		return _bestPath;
	}

	/**
	 * Value finding of the best solution
	 * 
	 * @return - Value of the best solution
	 */
	public double getGlobalBest()
	{
		return _globalBest;
	}
	
	public int getCurrentIteration()
	{
		return _currentIteration;
	}

	public boolean isRunning()
	{
		return _started && _keepRunning && !_stopped;
	}
}

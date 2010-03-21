/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.metaheuristic.antcolony;

import java.util.*;

/**
 *
 * @author Richard Malek (original)
 */
public class AntColony implements Observer
{
	private double _roundBest = Double.MAX_VALUE;
	private double _globalBest = Double.MAX_VALUE;
	private Vector<Edge> _bestPath;
	private Vector<Vector<Edge>> _reports = new Vector<Vector<Edge>>();
	private int _numAnts;
	private int _numIterations;
	private int _round;
	private boolean _running = true;
        private Graph _graph;


	public AntColony(int numAnts, int numIterations, Graph graph)
	{
            _graph = graph;
            this._numAnts = numAnts;
		this._numIterations = numIterations;
	}
	public void beginExploring()
	{
		_round = 0;
		while (_running)
		{
			while (_round < _numIterations)
			{
				spawnAnts();
				_round++;

			}
			if (_round == _numIterations)
			{
				_running = false;
			}
		}
	}

	public void update(Observable o, Object arg)
	{
		_reports.add((Vector<Edge>)arg);

		if (_reports.size() == _numAnts)
		{

			solveRound();
		}

	}

	private void solveRound()
	{
		double roundTotal = 0d;

		for (Vector<Edge> vector : _reports)
		{
                        if(_bestPath == null)
                            _bestPath = vector;

			for (Edge edges : vector)
			{
				roundTotal += edges.getEdgeLength();
			}
			if (roundTotal < _roundBest)
			{
				_roundBest = roundTotal;
			}

			if (_roundBest < _globalBest)
			{
				_globalBest = _roundBest;
				_bestPath = vector;
			}
		}

		System.out.println("Round " + _round + "\n----------------------------");
		System.out.println("This round best was  : " + _roundBest);
		System.out.println("The global best was : " + _globalBest + "\n");

		reinforceUpdateAndDecay();

		_roundBest = Double.MAX_VALUE;
		_reports.clear();
	}

	private void spawnAnts()
	{
		for (int i = 0; i < _numAnts; i++)
		{
			Ant someAnt = new Ant(_graph);
                        _reports.add(someAnt.explore());
		}
                solveRound();
	}

	private void reinforceUpdateAndDecay()
	{
		if (_globalBest/_roundBest > .8 )
		{
			for (Edge best : _bestPath)
			{
				best.adjustGlobalPheromone((1/best.getEdgeLength()));
			}
		}

		for (Edge updateLocal : _graph.getEdgeList())
		{
			updateLocal.adjustGlobalPheromone(updateLocal.getLocalPheromone());
			updateLocal.resetLocalPheromone();
			if (!(updateLocal.getGlobalPheromone() >= 0))
			{
				updateLocal.adjustGlobalPheromone(-(1/updateLocal.getEdgeLength()));
			}
		}
	}
}

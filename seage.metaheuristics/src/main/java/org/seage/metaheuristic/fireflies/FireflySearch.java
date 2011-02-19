/*******************************************************************************
 * Copyright (c) 2001 Robert Harder
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Robert Harder
 *     - Initial implementation
 *     Richard Malek
 *     - Merge with SEAGE
 *     Karel Durkota
 */

package org.seage.metaheuristic.fireflies;

import java.util.Collections;
import java.util.Random;
import java.util.logging.Logger;

/**
 * This version of the {@link FireflySearch} does not create any new threads, making it
 * ideal for embedding in Enterprise JavaBeans. The {@link #startSolving} method
 * blocks until the given number of iterations have been completed.
 *
 * @author Karel Durkota
 */
public class FireflySearch extends FireflySearchBase{
    private static final Logger log = Logger.getLogger("org.seage");//FireflySearch.class.getName());

    private int _iterationCount;
    private int _currentIteration;
    private int _populationCount;

    private boolean _withDecreasingRandomness;
    private boolean _withHillClimbingBestSolution;
    private double _initialIntensity;
    private double _initialRandomness;
    private double _finalRandomness;
    private double _absorption;
    private double _timeStep;
    
//    private double _randomSubjectPct;
//    private double _mutateSubjectPct;
//    private double _eliteSubjectPct;
//    private double _crossLengthPct;

    private boolean _keepSearching;

    private Population _population;
    private FireflyOperator _operator;
    private ObjectiveFunction _objectiveFunction;

    private Solution _bestSolution;
    private SolutionComparator _solutionComparator;

    private Random _random;
    private boolean _maximizing;
    private int _iterationsToGo;

    /**
     * CONSTRUCTOR
     * @param operator
     * @param evaluator
     */
    public FireflySearch(FireflyOperator operator, ObjectiveFunction objectiveFunction)
    {
        _operator = operator;
        _objectiveFunction = objectiveFunction;
        _population = new Population();
        _random = new Random();

        _bestSolution = null;
        _solutionComparator = new SolutionComparator();

        _keepSearching = false;
    }

    public FireflyOperator getOperator()
    {
        return _operator;
    }

    public Solution[] getSolutions() throws Exception
    {
        try
        {
             return _population.getSolutions(_populationCount);
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    public void stopSolving()
    {
        _keepSearching = false;
    }

    public boolean isSolving() { return true; }

    public Solution getBestSubject()
    {
        return _bestSolution;
    }


    private void evaluatePopulation(Population population) throws Exception
    {
        try
        {
            for (int i = 0; i < population.getSize(); i++)
            {
                Solution solution = population.getSolution(i);
                solution.setObjectiveValue(_objectiveFunction.evaluate(solution));
            }
            Collections.sort(population.getList(), _solutionComparator);
        }
        catch (Exception ex)
        {
            throw ex;
        }

    }

    public void startSolving(Solution[] solutions) throws Exception
    {
        try{
            
            _keepSearching = true;
            long startTime = System.currentTimeMillis();
            fireFireflySearchStarted();

            _bestSolution = null;
            _currentIteration = 0;

            Population workPopulation = new Population();
            _population.removeAll();
            for (int i = 0; i < _populationCount; i++)
            {
                //System.out.println(subjects[i].hashCode());
                if (i < solutions.length)
                    _population.addSolution(solutions[i]);
                else
                    break;
            }
            double prevFitness,currBestFitness;
            if(_maximizing)
            {
                prevFitness = Double.MAX_VALUE;
                currBestFitness = Double.MAX_VALUE;
            }
            else
            {
                prevFitness = Double.MIN_VALUE;
                currBestFitness = Double.MIN_VALUE;
            }
            int i=0;
            while (i++ < _iterationsToGo && _keepSearching)
            {
                log.finer("---------------------------\n"+i+"-th ITERATION");
                _currentIteration++;

                evaluatePopulation(_population);
                double d = 0;
                for(int s1=0;s1<_population.getSize()-1;s1++){
                    for(int s2=s1+1;s2<_population.getSize();s2++){
                        d=d+_operator.getDistance(_population.getSolutions()[s1],_population.getSolutions()[s2]);
                    }
                }
                log.finer("average distance = "+(double)d/(_population.getSize()*(_population.getSize()+1)/2));
                //_population.removeTwins();

//                for(int j=0;j<_population.getSize();j++){
//                    System.out.println("\nVypis:"+_population.getSolutions()[j].toString()+" - "+_population.getSolutions()[j].getObjectiveValue()[0]);
//                }

                if (_bestSolution == null || _solutionComparator.compare(_population.getBestSolution(), _bestSolution) == -1)
                {
                    _bestSolution = (Solution)_population.getBestSolution().clone();
                    //System.out.print(i+"\t");
                    fireNewBestSolution();
                }
                else
                    ;//fireNoChangeInValueIterationMade();

                    // check a worse solution than previous
                currBestFitness = _objectiveFunction.evaluate(_population.getBestSolution())[0];//.getObjectiveValue()[0];
                //System.out.println(currBestFitness);
		if(currBestFitness != _population.getBestSolution().getObjectiveValue()[0])     // SOLVED
                    throw new Exception("Fitness function failed");
                //prevFitness = currFitness;

                Collections.shuffle(_population.getList());
//                for(int j=0;j<_population.getSize();j++){
//                    System.out.println("\nVypis:"+_population.getSolutions()[j].toString()+" - "+_population.getSolutions()[j].getObjectiveValue()[0]);
//                }
                workPopulation.removeAll();
                workPopulation.mergePopulation(_population);
                for(int s1=0;s1<_population.getSize();s1++){
                    for(int s2=0;s2<_population.getSize();s2++){
                        if(_maximizing){
                            if(workPopulation.getSolution(s1).getObjectiveValue()[0]
                                < workPopulation.getSolution(s2).getObjectiveValue()[0]){
                                _operator.attract(_population.getSolutions()[s1],workPopulation.getSolutions()[s2],_currentIteration);

                                if(_population.getSolutions()[s1].equals(workPopulation.getSolutions()[s2])){
//                                    System.out.println("EQUALS\nOLD:"+_population.getSolutions()[s1].toString());
//                                    _operator.randomSolution(_population.getSolutions()[s1]);
                                    _operator.modifySolution(_population.getSolutions()[s1]);
//                                    System.out.println("NEW:"+_population.getSolutions()[s1].toString());
                                }
//                                else
//                                    System.out.println("Not equals.");
                            }
                        }
                         else{
                            if(workPopulation.getSolution(s1).getObjectiveValue()[0]
                                > workPopulation.getSolution(s2).getObjectiveValue()[0]){
                                _operator.attract(_population.getSolutions()[s1],workPopulation.getSolutions()[s2],_currentIteration);

                                if(_population.getSolutions()[s1].equals(workPopulation.getSolutions()[s2])){
//                                    System.out.println("EQUALS\nOLD:"+_population.getSolutions()[s1].toString());
                                    _operator.modifySolution(_population.getSolutions()[s1]);
//                                    System.out.println("NEW:"+_population.getSolutions()[s1].toString());
                                }
//                                else
//                                    System.out.println("Not equals.");
                            }
                        }
                        //temporary solution for not getting in the same solutions

                    }
                }
                // hill climbing of best solution
//                double bestEval = _population.getBestSolution().getObjectiveValue()[0];
                if(this.getWithHillClimbingBestSolution()){
                    Solution best = (Solution)_population.getBestSolution().clone();
                    _operator.modifySolution(best);
                    if(_solutionComparator.compare(best,_population.getBestSolution()) == -1)
                        _population.addSolution(best);
                }



                //temporary solution for not getting in the same solutions
//                for(int s1=0;s1<_population.getSize();s1++){
//                    for(int s2=0;s2<_population.getSize();s2++){
//                        if(_population.getSolutions()[s1].equals(_population.getSolutions()[s2])){
//                            _population.getSolutions()[s2]=_operator.randomSolution();
//                        }
//                    }
//                }
            }

            evaluatePopulation(_population);
            log.finer(_population.getBestSolution().toString());
            fireFireflySearchStopped();
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    public void setObjectiveFunction(ObjectiveFunction function) throws Exception {
        _objectiveFunction = function;
    }

    public void setBestSolution(Solution solution) {
        _bestSolution = solution;
    }

    public void setIterationsToGo(int iterations) {
        _iterationsToGo = iterations;
    }

    public void setMaximizing(boolean maximizing) {
        _maximizing = maximizing;
    }

    public void setPopulationCount(int pop){
        _populationCount=pop;
    }

    public int getPopulationCount(){
        return _populationCount;
    }

    public ObjectiveFunction getObjectiveFunction() {
        return _objectiveFunction;
    }

    public Solution getBestSolution() {
        return _bestSolution;
    }

    public int getIterationsToGo() {
        return _iterationsToGo;
    }

    public boolean isMaximizing() {
        return _maximizing;
    }

    public double getAbsorption() {
        return _absorption;
    }

    public void setAbsorption(double _absorption) {
        this._absorption = _absorption;
    }

    public int getCurrentIteration() {
        return _currentIteration;
    }

    public void setCurrentIteration(int _currentIteration) {
        this._currentIteration = _currentIteration;
    }

    public double getFinalRandomness() {
        return _finalRandomness;
    }

    public void setFinalRandomness(double _finalRandomness) {
        this._finalRandomness = _finalRandomness;
    }

    public double getInitialIntensity() {
        return _initialIntensity;
    }

    public void setInitialIntensity(double _initialIntensity) {
        this._initialIntensity = _initialIntensity;
    }

    public double getInitialRandomness() {
        return _initialRandomness;
    }

    public void setInitialRandomness(double _initialRandomness) {
        this._initialRandomness = _initialRandomness;
    }

    public int getIterationCount() {
        return _iterationCount;
    }

    public void setIterationCount(int _iterationCount) {
        this._iterationCount = _iterationCount;
    }

    public double getTimeStep() {
        return _timeStep;
    }

    public void setTimeStep(double _timeStep) {
        this._timeStep = _timeStep;
    }

    public boolean isWithDecreasingRandomness() {
        return _withDecreasingRandomness;
    }

    public void setWithDecreasingRandomness(boolean _withDecreasingRandomness) {
        this._withDecreasingRandomness = _withDecreasingRandomness;
    }

    public void setWithDecreasingRandomness(int _withDecreasingRandomness) {
        if(_withDecreasingRandomness>0)
            this.setWithDecreasingRandomness(true);
        else
            this.setWithDecreasingRandomness(false);
    }

    public void setWithHillClimbingBestSolution(boolean _withHillClimbingBestSolution) {
        this._withHillClimbingBestSolution=_withHillClimbingBestSolution;
    }

    public boolean getWithHillClimbingBestSolution() {
        return this._withHillClimbingBestSolution;
    }
}

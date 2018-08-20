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
package org.seage.problem.rosenbrock.fireflies;


import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.Solution;

/**
 *
 * @author Administrator
 */
class ContinuousFireflyOperator extends FireflyOperator{
 
    
    static boolean _withDecreasingRandomness;
    static double _initialIntensity;
    static double _initialRandomness;
    static double _finalRandomness;
    static double _absorption;
    static double _randomness;
    static double _timeStep;
    public static Double[][][] _facilityLocations;
    static double[] _minBoundery;
    static double[] _maxBoundery;
    static int _dimension;

    public ContinuousFireflyOperator(){
        _withDecreasingRandomness=false;
        _initialIntensity=1;
        _initialRandomness=1;
        _finalRandomness=0.2;
        _absorption=0.1;
        _timeStep=0.5;
        _dimension=2;
    }

    public ContinuousFireflyOperator(double initialIntensity,double initialRandomness,double finalRandomness,double absorption,double timeStep,boolean withDecreasingRandomness, int dimension, double xMin, double xMax){
        _withDecreasingRandomness=withDecreasingRandomness;
        _initialIntensity=initialIntensity;
        _initialRandomness=initialRandomness;
        _finalRandomness=finalRandomness;
        _absorption=absorption;
        _timeStep=timeStep;
        _minBoundery=new double[dimension];
        _maxBoundery=new double[dimension];
        _dimension=dimension;
        for(int i=0;i<dimension;i++)
        {
        	_minBoundery[i]=xMin;
        	_minBoundery[i]=xMax;
        }
    }

    /**
     * returns Euclidian distance of two vectors
     * @param s1
     * @param s2
     * @return
     */
    public double getDistance(Solution s1, Solution s2) {
        // for QAP distance of two solution will be their hammings distance
        double distance = 0;
        ContinuousSolution as1 = (ContinuousSolution)s1;
        ContinuousSolution as2 = (ContinuousSolution)s2;
        for(int i=0;i<as1._assign.length;i++){
            distance+=(as1._assign[i]-as2._assign[i])*(as1._assign[i]-as2._assign[i]);
        }
        distance=Math.sqrt(distance);
        return distance;
    }
/**
 * Attract solution s0 to s1 means to get s0 a bit closer to s1. In coninuous space firefly
 * algorithm solves it by equation:
 *     s0 = s0 + beta*exp(-absorption*distance^2)*(s1 - s0) + randomness(rand - 0.5)
 * In dicrete space, where solution is not a vector, but permutation it has to be done a bit differently
 * Similar effect can be achieved by creating a mutant of s0 and s1. Standard procedure for mutating two
 * parmutations is to find what are common for both, that we definitelly want to keep in a new mutant.
 * The rest is going to be generated in a way of probabilities such that
 *    with probability of beta*exp(-absorption*distance^2) were going to put there gene from s1
 *    with probability of randomness(rand - 0.5) a random gene (not yet used)
 *    else put gene from s0
 * @param s0
 * @param s1
 * @param iter
 */
    public void attract(Solution s0, Solution s1, int iter) {
        //beta step
        @SuppressWarnings("unused")
		Double[] cs0 = ((ContinuousSolution)s0)._assign,cs1 = ((ContinuousSolution)s1)._assign;
        // compute beta
        Double[] result=betaStep(s0,s1,_absorption);
        if(_withDecreasingRandomness)
            _randomness = _finalRandomness + (_initialRandomness - _finalRandomness)*Math.exp(-iter*_timeStep);
        else
            _randomness = _initialRandomness;
        result=alphaStep(result,_randomness);
        //alpha step
        ((ContinuousSolution)s0)._assign=result;
    }



    /**
     * Generates random solution(=permutation)
     * @return
     */
    public Solution randomSolution() {
        Double[] result = new Double[_dimension];
        for(int i=0;i<_dimension;i++){
            result[i] = (_maxBoundery[i]-_minBoundery[i])*Math.random()+_minBoundery[i];
        }
        ContinuousSolution cs = new ContinuousSolution(result);
        return cs;
    }

    /**
     * Edits input's solution into random generated solution(=permutation)
     * @param solution
     */
    @Override
    public void randomSolution(Solution solution) {
        ContinuousSolution q = (ContinuousSolution)solution;
        Double[] result = new Double[q._assign.length];
        for(int i=0;i<q._assign.length;i++){
            result[i] = (_maxBoundery[i]-_minBoundery[i])*Math.random()+_minBoundery[i];
        }
        q._assign=result;
    }

    /**
     * Beta step consists of:
     *  1) extracting what is common for both solution
     *  2) with beta-probability fill the gaps from solution solution2, otherwise with solution1
     *  3) if there remains empty gaps, fill them randomly
     * @param solution1
     * @param solution2
     * @param gamma
     * @return
     */
    public Double[] betaStep(Solution solution1, Solution solution2, double gamma){
        
        Double[] cs1 = ((ContinuousSolution)solution1)._assign;
        Double[] cs2 = ((ContinuousSolution)solution2)._assign;
        double beta = _initialIntensity/(1 + gamma * Math.pow(getDistance(solution1,solution2),2));        
        
        Double[] result = new Double[cs1.length];
        for(int i=0;i<result.length;i++){
            result[i] = cs1[i] + beta*(cs2[i]-cs1[i]);
        }
        return result;
        }

    /**
     * Make alpha-many swaps in solution
     * @param solution
     * @param alpha
     * @return
     */
    public static Double[] alphaStep (Double[] solution, double alpha){
        Double[] result = new Double[solution.length];
        for(int i=0;i<result.length;i++){
            result[i]=solution[i] + alpha * (Math.random() - .5);
        }
        return result;
    }
    
    public void modifySolution(Solution solution) {
        Double[] sol = ((ContinuousSolution)solution)._assign;
        Double[] result = new Double[sol.length];
        for(int i=0;i<result.length;i++){
            result[i]=sol[i] + _randomness * (Math.random() - .5);
        }
        ((ContinuousSolution)solution)._assign = result;
    }
}

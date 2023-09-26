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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * .
 * Contributors:
 *   Richard Malek
 *   - Initial implementation
 */

package org.seage.problem.rosenbrock.fireflies;


import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.Solution;

/**
 * .
 *
 * @author Richard Malek
 */
class ContinuousFireflyOperator extends FireflyOperator{
  boolean withDecreasingRandomness;
  double initialIntensity;
  double initialRandomness;
  double finalRandomness;
  double absorption;
  double randomness;
  double timeStep;
  public   Double[][][] facilityLocations;
  double[] minBoundery;
  double[] maxBoundery;
  int dimension;

  public ContinuousFireflyOperator(){
    this.withDecreasingRandomness = false;
    this.initialIntensity = 1;
    this.initialRandomness = 1;
    this.finalRandomness = 0.2;
    this.absorption = 0.1;
    this.timeStep = 0.5;
    this.dimension = 2;
  }

  public ContinuousFireflyOperator(double initialIntensity,double initialRandomness,double finalRandomness,double absorption,double timeStep,boolean withDecreasingRandomness, int dimension, double xMin, double xMax){
    this.withDecreasingRandomness = withDecreasingRandomness;
    this.initialIntensity = initialIntensity;
    this.initialRandomness = initialRandomness;
    this.finalRandomness = finalRandomness;
    this.absorption = absorption;
    this.timeStep = timeStep;
    this.minBoundery = new double[dimension];
    this.maxBoundery = new double[dimension];
    this.dimension = dimension;
    for (int i = 0; i < dimension; i++) {
      this.minBoundery[i] = xMin;
      this.minBoundery[i] = xMax;
    }
  }

  /**
   * returns Euclidian distance of two vectors.
   *
   * @param s1 .
   * @param s2 .
   * @return .
   */
  public double getDistance(Solution s1, Solution s2) {
    // for QAP distance of two solution will be their hammings distance
    double distance = 0;
    ContinuousSolution as1 = (ContinuousSolution) s1;
    ContinuousSolution as2 = (ContinuousSolution) s2;
    for (int i = 0; i < as1.assign.length; i++) {
      distance += (as1.assign[i] - as2.assign[i]) * (as1.assign[i] - as2.assign[i]);
    }
    distance = Math.sqrt(distance);
    return distance;
  }

  /**
   * Attract solution s0 to s1 means to get s0 a bit closer to s1. In coninuous space firefly
   * algorithm solves it by equation:
   *   s0 = s0 + beta*exp(-absorption*distance^2)*(s1 - s0) + randomness(rand - 0.5)
   * In dicrete space, where solution is not a vector, 
   * but permutation it has to be done a bit differently
   * Similar effect can be achieved by creating 
   * a mutant of s0 and s1. Standard procedure for mutating two
   * parmutations is to find what are common for both, 
   * that we definitelly want to keep in a new mutant.
   * The rest is going to be generated in a way of probabilities such that
   *  with probability of beta*exp(-absorption*distance^2) were going to put there gene from s1
   *  with probability of randomness(rand - 0.5) a random gene (not yet used)
   *  else put gene from s0
   *
   * @param s0 .
   * @param s1 .
   * @param iter .
   */
  public void attract(Solution s0, Solution s1, int iter) {
    //beta step
    @SuppressWarnings("unused")
    Double[] cs0 = ((ContinuousSolution) s0).assign;
    Double[] cs1 = ((ContinuousSolution) s1).assign;
    // compute beta
    Double[] result = betaStep(s0, s1, absorption);
    if (withDecreasingRandomness) {
      randomness = 
        finalRandomness + (initialRandomness - finalRandomness) * Math.exp(-iter * timeStep);
    } else {
      randomness = initialRandomness;
    }
    result = alphaStep(result, randomness);
    //alpha step
    ((ContinuousSolution) s0).assign = result;
  }

  /**
   * Generates random solution(=permutation).
   *
   * @return .
   */
  public Solution randomSolution() {
    Double[] result = new Double[dimension];
    for (int i = 0; i < dimension; i++) {
      result[i] = (maxBoundery[i] - minBoundery[i]) * Math.random() + minBoundery[i];
    }

    return new ContinuousSolution(result);
  }

  /**
   * Edits input's solution into random generated solution(=permutation).
   *
   * @param solution .
   */
  @Override
  public void randomSolution(Solution solution) {
    ContinuousSolution q = (ContinuousSolution) solution;
    Double[] result = new Double[q.assign.length];
    for (int i = 0; i < q.assign.length; i++) {
      result[i] = (maxBoundery[i] - minBoundery[i]) * Math.random() + minBoundery[i];
    }
    q.assign = result;
  }

  /**
   * .
   * Beta step consists of:
   *  1) extracting what is common for both solution
   *  2) with beta-probability fill the gaps from solution solution2, otherwise with solution1
   *  3) if there remains empty gaps, fill them randomly
   *
   * @param solution1 .
   * @param solution2 .
   * @param gamma .
   * @return .
   */
  public Double[] betaStep(Solution solution1, Solution solution2, double gamma){
    
    Double[] cs1 = ((ContinuousSolution) solution1).assign;
    Double[] cs2 = ((ContinuousSolution) solution2).assign;
    double beta = initialIntensity / (1 + gamma * Math.pow(getDistance(solution1, solution2), 2));
    
    Double[] result = new Double[cs1.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = cs1[i] + beta * (cs2[i] - cs1[i]);
    }
    return result;
  }

  /**
   * Make alpha-many swaps in solution.
   *
   * @param solution .
   * @param alpha .
   * @return .
   */
  public static Double[] alphaStep(Double[] solution, double alpha) {
    Double[] result = new Double[solution.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = solution[i] + alpha * (Math.random() - .5);
    }
    return result;
  }
      
  public void modifySolution(Solution solution) {
    Double[] sol = ((ContinuousSolution) solution).assign;
    Double[] result = new Double[sol.length];
    for (int i = 0; i < result.length; i++){
      result[i] = sol[i] + randomness * (Math.random() - .5);
    }
    ((ContinuousSolution) solution).assign = result;
  }
}

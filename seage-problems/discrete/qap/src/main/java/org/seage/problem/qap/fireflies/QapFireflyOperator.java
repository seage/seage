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

package org.seage.problem.qap.fireflies;

import java.util.ArrayList;
import java.util.Collections;

import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.Solution;
import org.seage.problem.qap.AssignmentProvider;

/**
 * QapFireflyOperator implements Operators required by Firefly Algorithm
 * @author Karel Durkota
 */
public class QapFireflyOperator extends FireflyOperator {

    static boolean _withDecreasingRandomness;
    static double _initialIntensity;
    static double _initialRandomness;
    static double _finalRandomness;
    static double _absorption;
    static double _timeStep;
    public static Double[][][] _facilityLocations;

    public QapFireflyOperator(){
        _withDecreasingRandomness=false;
        _initialIntensity=1;
        _initialRandomness=1;
        _finalRandomness=0.2;
        _absorption=0.1;
        _timeStep=0.5;
    }

    public QapFireflyOperator(double initialIntensity,double initialRandomness,double finalRandomness,double absorption,double timeStep,boolean withDecreasingRandomness){
        _withDecreasingRandomness=withDecreasingRandomness;
        _initialIntensity=initialIntensity;
        _initialRandomness=initialRandomness;
        _finalRandomness=finalRandomness;
        _absorption=absorption;
        _timeStep=timeStep;
    }

    /**
     * returns Hamming's distance of two solutions(=permutations)
     * @param s1
     * @param s2
     * @return
     */
    public double getDistance(Solution s1, Solution s2) {
        // for QAP distance of two solution will be their hammings distance
        double distance = 0;
        QapSolution qaps1=(QapSolution)s1,qaps2=(QapSolution)s2;
        for(int i=0;i<qaps1.getAssign().length;i++){
            if(qaps1.getAssign()[i]!=qaps2.getAssign()[i])
                distance++;
        }
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
    @Override
    public void attract(Solution s0, Solution s1, int iter) {
        //beta step
        QapSolution qaps0 = (QapSolution)s0,qaps1 = (QapSolution)s1;
        qaps0._assign=betaStep(qaps0._assign,qaps1._assign,_absorption);
        double randomness=0;
        if(_withDecreasingRandomness)
            randomness = _finalRandomness + (_initialRandomness - _finalRandomness)*Math.exp(-iter*_timeStep);
        else
            randomness = _initialRandomness;
        qaps0._assign=alphaStep(qaps0._assign,randomness);
        //alpha step
    }



    /**
     * Generates random solution(=permutation)
     * @return
     */
    @Override
    public Solution randomSolution() {
        return new QapSolution(AssignmentProvider.createRandomAssignment(QapFireflyOperator._facilityLocations));
    }

    /**
     * Edits input's solution into random generated solution(=permutation)
     * @param solution
     */
    @Override
    public void randomSolution(Solution solution) {
        QapSolution q = (QapSolution)solution;
        q._assign=AssignmentProvider.createRandomAssignment(_facilityLocations);
    }

    /**
     * Edits the solution(=permutation) by swaping two elements' positions
     * @param solution
     */
    @Override
    public void modifySolution(Solution solution) {
        QapSolution q = (QapSolution)solution;
        int i1 = (int)(Math.random()*q.getAssign().length);
        int i2=i1;
        while(i1==i2){
            i2 = (int)(Math.random()*q.getAssign().length);
        }
        q.getAssign()[i1]=q.getAssign()[i1]+q.getAssign()[i2];
        q.getAssign()[i2]=q.getAssign()[i1]-q.getAssign()[i2];
        q.getAssign()[i1]=q.getAssign()[i1]-q.getAssign()[i2];
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
    public static Integer[] betaStep(Integer[] solution1, Integer[] solution2, double gamma){
        int length = solution1.length;
        Integer[] mutant = new Integer[length];
        // preparing some needed variables
        int distance = 0;
        for(int i=0;i<solution1.length;i++){
            if(solution1[i]!=solution2[i])
                distance++;
        }
        double beta = _initialIntensity/(1 + gamma * Math.pow( distance, 2));
        ArrayList<Integer> unusedPositions = new ArrayList<Integer>();
        int[] positions = new int[length];
        ArrayList<Integer> unusedElements = new ArrayList<Integer>();
        for(int i = 0; i < length; i++){
//            System.out.print("+"+i+" ");
            positions[i]=1;
            //unusedPositions.add(i);
            unusedElements.add(solution1[i]);
        }
        // extracting what is in common of both solutions
        for(int i = 0; i < length; i++){
            if(solution1[i] == solution2[i]){
//                System.out.print("-"+i+", ");
                mutant[i]=solution1[i];
                positions[i]=0;
                //unusedPositions.remove(i);
                unusedElements.remove(unusedElements.indexOf((int)mutant[i]));
            }
        }
        // filling the gaps with beta probability
        for(int i=0;i<length;i++)
            if(positions[i]==1)
                unusedPositions.add(i);
        Collections.shuffle(unusedPositions);
        for(Integer i : unusedPositions){
            if(Math.random() < beta && unusedElements.contains(solution2[i])){
                mutant[i]=solution2[i];
                unusedElements.remove(unusedElements.indexOf(mutant[i]));
            }
            else if(unusedElements.contains(solution1[i])){
                mutant[i]=solution1[i];
                unusedElements.remove(unusedElements.indexOf(mutant[i]));
            }
        }
        for(int i = 0; i < length; i++){
            if(mutant[i] == null){
                Collections.shuffle(unusedElements);
                mutant[i]=unusedElements.remove(0);
                }
            }
        return mutant;
        }

    /**
     * Make alpha-many swaps in solution
     * @param solution
     * @param alpha
     * @return
     */
    public static Integer[] alphaStep (Integer[] solution, double alpha){
        Integer[] result = new Integer[solution.length];
        for(int i=0;i<solution.length;i++){
            result[i]=solution[i];
        }
        int num = (int) Math.floor((alpha - 1)*Math.random() + 1);
        ArrayList<Integer> positions = new ArrayList<Integer>();
        ArrayList<Integer> elements = new ArrayList<Integer>();
        // pick the positions and their elements randomly
        for(int i = 0; i < num; i++){
            int r = (int) Math.floor(Math.random()*solution.length);
            if(!positions.contains(r)){
                positions.add(r);
                elements.add(solution[r]);
            }
        }
        Collections.shuffle(elements);
        for(Integer i : positions){
            while(solution[i] == elements.get(0) && elements.size()>1){
                Collections.shuffle(elements);
            }
            result[i]=elements.remove(0);
        }
        return result;
    }
}

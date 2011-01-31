/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.qap.fireflies;

import java.util.ArrayList;
import java.util.Collections;
import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.Solution;

/**
 *
 * @author Administrator
 */
public class QapFireflyOperator extends FireflyOperator { 

    static boolean _withDecreasingRandomness;
    static double _initialIntensity;
    static double _initialRandomness;
    static double _finalRandomness;
    static double _absorption;
    static double _timeStep;

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

    public static double getDistance(Solution s1, Solution s2) {
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
 * Attract solution s0 to s1 means to get it a bit closer to s1. In coninuous space firefly
 * algorithm solves it by equation:
 *     s0 = s0 + beta*exp(-absorption*distance^2)*(s1 - s0) + randomness(rand - 0.5)
 * In dicreet space, where solution is not a vector, but permutation it has to be done a bit differently
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
        double randomness = _finalRandomness + (_initialRandomness - _finalRandomness)*Math.exp(-iter*_timeStep);
        double distance = QapFireflyOperator.getDistance(s0,s1);
        double beta = _initialIntensity/(1 + _absorption*Math.pow(distance,2));
        double alfa = randomness*(Math.random());
        QapSolution qaps0 = (QapSolution)s0,qaps1 = (QapSolution)s1;
        // MUTATE TWO SOLUTIONS
        /**
         * find out what s0 and s1 has common, that we want to keep in a mutant
         */
        ArrayList<Integer> temp = new ArrayList<Integer>(),temp2 = new ArrayList<Integer>();
        int[] pos = new int[qaps0.getAssign().length];
        int[] perm = new int[qaps0.getAssign().length];

        System.out.println("\n***Attracting "+s0.getObjectiveValue()[0]+" to "+s1.getObjectiveValue()[0]);
        System.out.println("beta = "+beta+"; alfa = "+alfa+"; (r = "+distance+"; randomness = "+randomness);;
        for(int i=0;i<qaps0.getAssign().length;i++){
            System.out.print(qaps0.getAssign()[i]+", ");
        }
        System.out.println("");
        for(int i=0;i<qaps0.getAssign().length;i++){
            System.out.print(qaps1.getAssign()[i]+", ");
        }
        System.out.println("");
        Integer[] newSol = new Integer[qaps0.getAssign().length];
        for(int i=0;i<qaps0.getAssign().length;i++){
            if((int)(qaps0.getAssign()[i])==(int)(qaps1.getAssign()[i])){
                newSol[i]=qaps0.getAssign()[i];
//                System.out.print(newSol[i]);
                pos[i]=-1;
                perm[newSol[i]]=-1;
//                leftNumberss.remove((int)newSol[i]);
            }
        }
        /**
         * fill out the missing fields in new solution in a random way
         */
        for(int i=0;i<pos.length;i++)
            temp2.add(i);
        Collections.shuffle(temp2);
        for(Integer i : temp2){
            if(pos[i]==-1)
                continue;
            double r = Math.random();
            if(r<beta && perm[qaps1.getAssign()[i]]!=-1){
                newSol[i]=qaps1.getAssign()[i];
                perm[newSol[i]]=-1;
            }
            else if(r<beta+alfa){
                temp.clear();
                for(int j=0;j<pos.length;j++){
                    if(perm[j]!=-1)
                        temp.add(j);
                }
                Collections.shuffle(temp);
                newSol[i]=temp.get(0);
                perm[newSol[i]]=-1;
            }
            else if(perm[qaps0.getAssign()[i]]!=-1){
                newSol[i]=qaps0.getAssign()[i];
                perm[qaps0.getAssign()[i]]=-1;
            }
            else{
                temp.clear();
                for(int j=0;j<pos.length;j++){
                    if(perm[j]!=-1)
                        temp.add(j);
                }
                Collections.shuffle(temp);
                newSol[i]=temp.get(0);
                perm[newSol[i]]=-1;
            }
//            System.out.print(newSol[i]+", ");
            pos[i]=-1;
        }

        for(int i=0;i<qaps0.getAssign().length;i++){
            System.out.print(newSol[i]+", ");
        }
        System.out.println("end attraction");
        qaps0._assign=newSol;
    }

}

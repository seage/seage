/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp.EFA;

import org.seage.problem.qap.EFA.*;
import java.util.ArrayList;
import java.util.Collections;
import org.seage.metaheuristic.EFA.*;
import org.seage.problem.qap.AssignmentProvider;

/**
 *
 * @author Administrator
 */
public class TspEFAOperator extends EFAOperator {

    static boolean _withDecreasingRandomness;
    static double _initialIntensity;
    static double _initialRandomness;
    static double _finalRandomness;
    static double _absorption;
    static double _timeStep;
    public static Double[][][] _facilityLocations;

    public TspEFAOperator(){
        _withDecreasingRandomness=false;
        _initialIntensity=1;
        _initialRandomness=1;
        _finalRandomness=0.2;
        _absorption=0.1;
        _timeStep=0.5;
    }

    public TspEFAOperator(double initialIntensity,double initialRandomness,double finalRandomness,double absorption,double timeStep,boolean withDecreasingRandomness){
        _withDecreasingRandomness=withDecreasingRandomness;
        _initialIntensity=initialIntensity;
        _initialRandomness=initialRandomness;
        _finalRandomness=finalRandomness;
        _absorption=absorption;
        _timeStep=timeStep;
    }

    public double getDistance(Solution s1, Solution s2) {
        // for QAP distance of two solution will be their hammings distance
        double distance = 0;
        TspSolution qaps1=(TspSolution)s1,qaps2=(TspSolution)s2;
        for(int i=0;i<qaps1.getTour().length;i++){
            if(qaps1.getTour()[i]!=qaps2.getTour()[i])
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
        double distance = 0;
        TspSolution qaps0 = (TspSolution)s0,qaps1 = (TspSolution)s1;
        for(int i=0;i<qaps1.getTour().length;i++){
            if(qaps1.getTour()[i]!=qaps0.getTour()[i])
                distance++;
        }
        double beta = _initialIntensity/(1 + _absorption*Math.pow(distance,2));
        double alfa = randomness*(Math.random()-.5);
        // MUTATE TWO SOLUTIONS
        /**
         * find out what s0 and s1 has common, that we want to keep in a mutant
         */
        ArrayList<Integer> temp = new ArrayList<Integer>(),temp2 = new ArrayList<Integer>();
        int[] pos = new int[qaps0.getTour().length];
        int[] perm = new int[qaps0.getTour().length];

//        System.out.println("\n***Attracting "+s0.getObjectiveValue()[0]+" to "+s1.getObjectiveValue()[0]);
//        if(Math.random()<0.05)
//            System.out.println("beta = "+beta+"; alfa = "+alfa+"; (r = "+distance+"; randomness = "+randomness);;
//        for(int i=0;i<qaps0.getAssign().length;i++){
//            System.out.print(qaps0.getAssign()[i]+", ");
//        }
//        System.out.println("");
//        for(int i=0;i<qaps0.getAssign().length;i++){
//            System.out.print(qaps1.getAssign()[i]+", ");
//        }
//        System.out.println("");
        int[] newSol = new int[qaps0.getTour().length];

        /**
         * set what they have the same in random order!
         */
        for(int i=0;i<pos.length;i++)
            temp2.add(i);
//        Collections.shuffle(temp2);
//        for(int i=0;i<qaps0.getAssign().length;i++){
//            if((int)(qaps0.getAssign()[i])==(int)(qaps1.getAssign()[i]) && Math.random()<beta){
//                newSol[i]=qaps0.getAssign()[i];
//                pos[i]=-1;
//                perm[newSol[i]]=-1;
//            }
//        }
        /**
         * fill out the missing fields in new solution in a random way
         */
        Collections.shuffle(temp2);
        for(Integer i : temp2){
            if(pos[i]==-1)
                continue;
            double r = Math.random();
            // with beta probability put there gene from s1
            if(r<beta && perm[qaps1.getTour()[i]]!=-1){
                newSol[i]=qaps1.getTour()[i];
                perm[newSol[i]]=-1;
            }
            // with alfa probability put there random gene
            else if(r<beta+alfa){
                temp.clear();
                for(int j=0;j<pos.length;j++){
                    if(perm[j]!=-1)
                        temp.add(j);
                }
                Collections.shuffle(temp);
                newSol[i]=temp.get(0);
                perm[newSol[i]]=-1;
            }// else put there original gene
            else if(perm[qaps0.getTour()[i]]!=-1){
                newSol[i]=qaps0.getTour()[i];
                perm[qaps0.getTour()[i]]=-1;
            }
            else{// in case all latter genes were used, put there again the random one
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

//        for(int i=0;i<qaps0.getAssign().length;i++){
//            System.out.print(newSol[i]+", ");
//        }
//        System.out.println("end attraction");
        boolean equal=true;
        for(int i=0;i<newSol.length;i++){
            if(qaps1.getTour()[i]!=newSol[i])
                equal=false;
        }
        if(equal){
            int i1 = (int)(Math.random()*newSol.length);
            int i2=i1;
            while(i1==i2){
                i2 = (int)(Math.random()*newSol.length);
            }
            newSol[i1]=newSol[i1]+newSol[i2];
            newSol[i2]=newSol[i1]-newSol[i2];
            newSol[i1]=newSol[i1]-newSol[i2];
        }
        qaps0._tour=newSol;
    }


    @Override
    public Solution randomSolution() {
        return new TspSolution(TourProvider.createRandomTour((TspEFAOperator.city));
    }

    @Override
    public void randomSolution(Solution solution) {
        QapSolution q = (QapSolution)solution;
        q._assign=AssignmentProvider.createRandomAssignment(_facilityLocations);
    }

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




}

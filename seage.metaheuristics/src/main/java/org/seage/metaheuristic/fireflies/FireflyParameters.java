/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.metaheuristic.fireflies;

/**
 *
 * @author Administrator
 */
class FireflyParameters {

    boolean withDecreasingRandomness;
    double initialIntensity;
    double initialRandomness;
    double finalRandomness;
    double absorption;
    double timeStep;
    int populationSize;

    public FireflyParameters(){
        withDecreasingRandomness=false;
        initialIntensity=1;
        initialRandomness=1;
        finalRandomness=0.2;
        absorption=0.2;
        timeStep=1;
        populationSize=100;
    }
}

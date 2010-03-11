/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.metaheuristic.particles;

/**
 *
 * @author Jan Zmatlik
 */
public class VelocityManager implements IVelocityManager
{

    public void calculateNewVelocity(Particle particle, Particle localMinimum, Particle globalMinimum, double alpha, double beta, double inertia)
    {
        double[] randomVector1 = new double[particle.getCoords().length];
        double[] randomVector2 = new double[particle.getCoords().length];

        setRandomVector(randomVector1);
        setRandomVector(randomVector2);

        // w as inertial weight
        // about inertial weight - http://tracer.uc3m.es/tws/pso/basics.html
        // {*} as Hadamard Product
        // g as global minimum
        // l as local minimum
        // Vi as velocity vector of current particle
        // Xi as coords of current particle
        // T as iteration counter
        // Al as Alpha - Learning parametr or acceleration constant
        // Be as Beta - Learning parametr or acceleration constant
        // e1 as random vector
        // e2 as random vector
        //
        // V(T+1) = w*Vi(T) + Al*e1{*}[g - Xi(T)] + Be*e2[l - Xi(T)]

        particle.setVelocity(multiplicationScalarVector(inertia, particle.getVelocity()));
        particle.setVelocity
                (
                    additionVectorVector
                    (
                        particle.getVelocity()
                        ,
                        additionVectorVector
                        (
                            multiplicationVectorVector(
                                multiplicationScalarVector(alpha, randomVector1),
                                subtractionVectorVector(globalMinimum.getCoords(), particle.getCoords())
                            )
                            ,
                            multiplicationVectorVector(
                                multiplicationScalarVector(beta, randomVector2),
                                subtractionVectorVector(localMinimum.getCoords(), particle.getCoords())
                            )
                        )
                    )
                );
    }

    public void calculateNewLocations(Particle particle)
    {
        additionVectorVectorToFirstVector(particle.getCoords(), particle.getVelocity());
    }

    private void setRandomVector(double[] vector)
    {
        for(int i = 0; i < vector.length; i++)
            vector[i] = Math.random();
    }

    // w = u + (-1)v
    private double[] subtractionVectorVector(double[] a, double[] b)
    {
        double[] resultVector = new double[a.length];
        for(int i = 0; i < a.length; i++)
            resultVector[i] = a[i] - b[i];
        return resultVector;
    }

    private double[] multiplicationVectorVector(double[] a, double[] b)
    {
        double[] resultVector = new double[a.length];
        for(int i = 0; i < a.length; i++)
              resultVector[i] = a[i] * b[i];
        return resultVector;
    }

    private double[] multiplicationScalarVector(double scalar, double[] vector)
    {
        double[] resultVector = new double[vector.length];
        for(int i = 0; i < vector.length; i++)
            resultVector[i] = scalar * vector[i];
        return resultVector;
    }

    private double[] additionVectorVector(double[] a, double[] b)
    {
        double[] resultVector = new double[a.length];
        for(int i = 0; i < a.length; i++)
            resultVector[i] = a[i] + b[i];
        return resultVector;
    }

    private void additionVectorVectorToFirstVector(double[] a, double[] b)
    {
        for(int i = 0; i < a.length; i++)
            a[i] = a[i] + b[i];
    }

}

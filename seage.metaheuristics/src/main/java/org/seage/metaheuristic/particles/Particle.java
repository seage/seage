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
public class Particle {

    private double[] _coords;

    private double[] _velocity;

    private double _evaluation = Double.MAX_VALUE;

    public Particle(int dimension)
    {
        _coords = new double[ dimension ];
        _velocity = new double[ dimension ];
    }

    public double[] getCoords() {
        return _coords;
    }

    public void setCoords(double[] coords) {
        this._coords = coords;
    }

    public double getEvaluation() {
        return _evaluation;
    }

    public void setEvaluation(double evaluation) {
        this._evaluation = evaluation;
    }

    public double[] getVelocity() {
        return _velocity;
    }

    public void setVelocity(double[] velocity) {
        this._velocity = velocity;
    }

    
}

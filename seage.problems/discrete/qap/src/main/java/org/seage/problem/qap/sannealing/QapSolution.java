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
package org.seage.problem.qap.sannealing;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Karel Durkota
 */
public abstract class QapSolution extends Solution
{
    /**
     * Represent order of cities
     */
    protected Integer[] _assign;

    /**
     * Array of cities
     */
    protected static Double[][][] _facilityLocation;

    public QapSolution(Double[][][] facilityLocation)
    {
        _assign = new Integer[ facilityLocation.length ];
        _facilityLocation = facilityLocation;
    }

    public Integer[] getAssign()
    {
        return _assign;
    }

     public void setAssign(Integer[] assign)
    {
        _assign = assign;
    }

    public Double[][][] getFacilityLocation()
    {
        return _facilityLocation;
    }
    
    public void setFacilityLocation(Double[][][] facilityLocation)
    {
        _facilityLocation = facilityLocation;
    }

    @Override
    public QapSolution clone()
    {
        QapSolution tspSolution = null;
        try
        {
            tspSolution = (QapSolution)super.clone();
            tspSolution.setAssign( _assign.clone() );
            tspSolution.setFacilityLocation( _facilityLocation );
            tspSolution.setObjectiveValue(getObjectiveValue());
        } catch (Exception ex)
        {
            Logger.getLogger(QapSolution.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tspSolution;
    }

}

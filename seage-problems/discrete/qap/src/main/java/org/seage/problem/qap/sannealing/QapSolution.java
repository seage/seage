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
        _assign = new Integer[facilityLocation.length];
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
        QapSolution qapSolution = null;
        try
        {
            qapSolution = (QapSolution) super.clone();
            qapSolution.setAssign(_assign.clone());
            qapSolution.setFacilityLocation(_facilityLocation);
            qapSolution.setObjectiveValue(getObjectiveValue());
        }
        catch (Exception ex)
        {
            Logger.getLogger(QapSolution.class.getName()).log(Level.SEVERE, null, ex);
        }
        return qapSolution;
    }

    public String toString()
    {
        StringBuffer s = new StringBuffer();

        s.append("[");
        for (int i = _assign.length - 1; i >= 1; i--)
        {
            s.append((_assign[i] + 1));
            s.append(",");
        }
        s.append((_assign[_assign[0]] + 1) + "]");

        return s.toString();
    } // end toString

}

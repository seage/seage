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
package org.seage.metaheuristic.sannealing;

/**
 * @author Jan Zmatlik
 */
public interface ISimulatedAnnealing
{
    double getMaximalTemperature();
    void setMaximalTemperature(double maximalTemperature);

    double getMinimalTemperature();
    void setMinimalTemperature(double minimalTemperature);

    double getAnnealingCoefficient();
    void setAnnealingCoefficient(double alpha);

    long getMaximalIterationCount();
    void setMaximalInnerIterationCount(long _maximalIterationCount);

    long getMaximalSuccessIterationCount();
    void setMaximalAcceptedSolutionsPerOneStepCount(long _maximalSuccessIterationCount);

    long getCurrentIteration();

    Solution getBestSolution();
    public Solution getCurrentSolution();

    void startSearching(Solution solution);
    void stopSearching();
}

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
    void setMaximalIterationCount(long _maximalIterationCount);

    long getMaximalSuccessIterationCount();
    void setMaximalSuccessIterationCount(long _maximalSuccessIterationCount);

    long getCurrentIteration();

    Solution getBestSolution();
    public Solution getCurrentSolution();

    void startSearching(Solution solution);
    void stopSearching();
}

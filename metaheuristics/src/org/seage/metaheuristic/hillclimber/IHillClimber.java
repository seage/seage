/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.metaheuristic.hillclimber;

/**
 *
 * @author rick
 */
public interface IHillClimber
{
    void setIterationCount(int count);
    void startSearching(Solution solution);
    Solution getBestSolution();
}

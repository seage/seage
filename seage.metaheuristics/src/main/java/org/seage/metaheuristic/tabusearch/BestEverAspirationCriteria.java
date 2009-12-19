package org.seage.metaheuristic.tabusearch;

/**
 * Determine if the proposed tabu move should in fact be allowed
 * because it results in a value better than the current
 * best solution's value.
 * This is the most common aspiration criteria used in tabu search.
 * <br />
 * Thanks to Victor Wiley from the University of Texas at Austin
 * for writing the first draft of this class.
 *
 * @see AspirationCriteria
 * @see Solution
 * @see Move
 * @see TabuSearch
 * @since 1.0.2
 * @author Victor Wiley
 * @author Robert Harder
 * @author Richard Malek
 */
public class BestEverAspirationCriteria implements AspirationCriteria
{

    
    /**
     * Determine if the proposed tabu move should in fact be allowed
     * because it results in a value better than the current
     * best solution's value.
     * This is the most common aspiration criteria used in tabu search.
     * <br />
     * Thanks to Victor Wiley from the University of Texas at Austin
     * for writing the first draft of this class.
     *
     * @param soln The solution to be modified
     * @param move The proposed move
     * @param value The resulting objective function value
     * @param tabuSearch The {@link TabuSearch} controlling the transaction
     * @see AspirationCriteria
     * @see Solution
     * @see Move
     * @see TabuSearch
     * @since 1.0.1
     * @author Victor Wiley
     * @author Robert Harder
     */
    public boolean overrideTabu( 
        final Solution solution, 
        final Move proposedMove, 
        final double[] proposedValue, 
        final ITabuSearch tabuSearch )
    {
		return
			//comparator.compare(solution, tabuSearch.getBestSolution()) == 1 ? true : false;
			TabuSearch.isFirstBetterThanSecond(
				proposedValue,
				tabuSearch.getBestSolution().getObjectiveValue(),
				tabuSearch.isMaximizing())
			? true
			: false;
    }   // end overrideTabu

}   // end class BestEverAspirationCriteria

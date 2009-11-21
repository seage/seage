package org.seage.metaheuristic.genetics;

/**
 * Summary description for GeneticSearchListener.
 */
public interface GeneticSearchListener extends java.util.EventListener
{
	public void geneticSearchStarted(GeneticSearchEvent e);
	public void geneticSearchStopped(GeneticSearchEvent e);
	public void newBestSolutionFound(GeneticSearchEvent e);
	public void noChangeInValueIterationMade(GeneticSearchEvent e);
}

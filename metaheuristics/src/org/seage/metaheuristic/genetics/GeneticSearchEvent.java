package org.seage.metaheuristic.genetics;

/**
 * Summary description for GeneticSearchEvent.
 */
public class GeneticSearchEvent extends java.util.EventObject
{
	
    public GeneticSearchEvent(GeneticSearchBase source)
    {   
        super( source );
    }

    public final GeneticSearchBase getGeneticSearch()
    {
            return (GeneticSearchBase)source;
    }
}

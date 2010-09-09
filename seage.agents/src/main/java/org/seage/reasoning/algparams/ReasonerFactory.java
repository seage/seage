package org.seage.reasoning.algparams;

import org.seage.data.DataNode;

/**
 * A factory interface for a Reasoner creation
 * @author rick
 */
public interface ReasonerFactory
{
    Reasoner createReasoner (DataNode param) throws Exception;
}


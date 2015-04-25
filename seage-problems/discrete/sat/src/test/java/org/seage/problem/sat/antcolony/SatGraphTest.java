package org.seage.problem.sat.antcolony;

import static org.junit.Assert.*;

import org.junit.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.problem.sat.Formula;

public class SatGraphTest
{
	@Test
    public void testSatGraph() throws Exception 
    {
		IProblemProvider provider = ProblemProvider.getProblemProviders().get("SAT");
		ProblemInstanceInfo pii = provider.getProblemInfo().getProblemInstanceInfo("uf20-01");
		Formula formula = (Formula)provider.initProblemInstance(pii);
		
		SatGraph graph = new SatGraph(formula);
		assertNotNull(graph);
    }
}

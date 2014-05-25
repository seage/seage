package org.seage.problem.tsp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.Instance;
import org.seage.aal.problem.ProblemProvider;
import org.seage.problem.tsp.tour.TspOptimalTour;
import org.seage.problem.tsp.tour.TspOptimalTourBerlin52;
import org.seage.problem.tsp.tour.TspOptimalTourPcb442;

public class TspPhenotypeEvaluatorTest {

//	@Test
//	public void testTspPhenotypeEvaluator() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testEvaluate() throws Exception 
	{	
		testEvaluateTour(new TspOptimalTourBerlin52());
		testEvaluateTour(new TspOptimalTourPcb442());		
	}
	
	private void testEvaluateTour(TspOptimalTour tour) throws Exception
	{
		IProblemProvider provider = ProblemProvider.getProblemProviders().get("TSP");
		Instance instance = provider.initProblemInstance(provider.getProblemInfo().getInstanceInfo(tour.Name));
		TspPhenotypeEvaluator evaluator = new TspPhenotypeEvaluator(((TspProblemInstance)instance).getCities());
		
		Integer[] s = tour.OptimalTour;
		TspOptimalTour.printTour(s);
		assertEquals(tour.OptimalLength, (int)evaluator.evaluate(s)[0]);
		
		s = TspOptimalTour.mirrorTour(tour.OptimalTour);
		TspOptimalTour.printTour(s);
		assertEquals(tour.OptimalLength, (int)evaluator.evaluate(s)[0]);
		
		s = TspOptimalTour.shiftTour(tour.OptimalTour, 10);
		TspOptimalTour.printTour(s);
		assertEquals(tour.OptimalLength, (int)evaluator.evaluate(s)[0]);
		
		s = TspOptimalTour.mirrorTour(TspOptimalTour.shiftTour(tour.OptimalTour, 10));
		TspOptimalTour.printTour(s);
		assertEquals(tour.OptimalLength, (int)evaluator.evaluate(s)[0]);
	}
	

//	@Test
//	public void testCompare() {
//		fail("Not yet implemented");
//	}

}

package org.seage.problem.tspsannealing;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.sannealing.TspGreedySolution;
import org.seage.problem.tsp.sannealing.TspObjectiveFunction2;
import org.seage.problem.tsp.sannealing.TspRandomSolution;
import org.seage.problem.tsp.sannealing.TspSolution;

public class TspObjectiveFunction2Test {

	private TspObjectiveFunction2 _objFunc;
	private City[] _cities;
	@Before
	public void setUp() throws Exception {
		_cities = new City[5];
		_cities[0] = new City(1, 0.0, 0.0);
		_cities[1] = new City(2, 1.0, 0.0);
		_cities[2] = new City(3, 1.0, 1.0);
		_cities[3] = new City(4, 1.0, 0.0);
		_cities[4] = new City(5, 0.5, 0.5);
		
		_objFunc = new TspObjectiveFunction2(_cities);		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEvaluate() throws Exception {
		TspSolution s = new TspGreedySolution(_cities);
		s.setTour(new Integer[]{0,1,2,3,4});
		
		double o = _objFunc.getObjectiveValue(s);
		double[] o2 = _objFunc.evaluate(s, new int[]{0,1});
		double[] o3 = _objFunc.evaluate(s, new int[]{0,2});
		double[] o4 = _objFunc.evaluate(s, new int[]{0,4});
	}

	@Test
	public void testGetObjectiveValue() {
		fail("Not yet implemented");
	}

}

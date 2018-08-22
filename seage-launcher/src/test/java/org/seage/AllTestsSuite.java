package org.seage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({
    org.seage.misc.AllTests.class,
    org.seage.metaheuristic.AllTests.class,
    org.seage.aal.AllTests.class,
    org.seage.hh.AllTests.class,    
//    org.seage.problem.jssp.AllTests.class,
//    org.seage.problem.qap.AllTests.class,
//    org.seage.problem.sat.AllTests.class,
    org.seage.problem.tsp.AllTests.class,
    org.seage.launcher.AllTests.class,
    })
public class AllTestsSuite
{

}
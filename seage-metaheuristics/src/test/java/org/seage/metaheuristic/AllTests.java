package org.seage.metaheuristic;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClassnameFilters({"org.seage.metaheuristic.*", "!.*AllTests"})
public class AllTests
{

}

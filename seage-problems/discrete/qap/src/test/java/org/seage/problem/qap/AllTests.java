package org.seage.problem.qap;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClassnameFilters({"org.seage.problem.qap.*", "!.*AllTests"})
public class AllTests
{

}

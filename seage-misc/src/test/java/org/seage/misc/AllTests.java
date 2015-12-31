package org.seage.misc;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClassnameFilters({"org.seage.classutil.*", "org.seage.thread.*", "!.*AllTests"})
public class AllTests
{

}

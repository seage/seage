package org.seage.knowledgebase;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClassnameFilters({"org.seage.knowledgebase.*", "!.*AllTests"})
public class AllTests
{

}

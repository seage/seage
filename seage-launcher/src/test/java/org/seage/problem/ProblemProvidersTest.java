package org.seage.problem;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Problem providers tests")
@SelectPackages("org.seage.problem")
@IncludeClassNamePatterns("^.*ProblemProviderTest$")
public class ProblemProvidersTest {
}

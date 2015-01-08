package org.seage.classutil;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ClassPathAnalyzerTest {

	@Test
	public void test() 
	{
		ClassPathAnalyzer cpa = new ClassPathAnalyzer("seage.problem");
		
		List<String> list = cpa.analyzeClassPath();
	}

}

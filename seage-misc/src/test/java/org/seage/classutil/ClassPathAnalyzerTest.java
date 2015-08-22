package org.seage.classutil;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

public class ClassPathAnalyzerTest
{

    @Test
    public void test()
    {
        ClassPathAnalyzer cpa = new ClassPathAnalyzer("seage.problem");

        List<String> list = cpa.analyzeClassPath();
        Assert.assertNotNull(list);
    }

}

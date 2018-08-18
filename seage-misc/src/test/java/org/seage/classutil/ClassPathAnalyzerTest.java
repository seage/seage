package org.seage.classutil;

import java.util.List;

import org.junit.jupiter.api.Test;

import junit.framework.Assert;

public class ClassPathAnalyzerTest
{

    @Test
    public void test()
    {
        ClassPathAnalyzer cpa = new ClassPathAnalyzer("seage.problem");

        List<String> list = cpa.analyzeClassPath();
        assertNotNull(list);
    }

}

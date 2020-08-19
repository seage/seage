package org.seage.classutil;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import java.util.List;

public class ClassPathAnalyzerTest
{

    @Test
    public void test()
    {
        ClassPathAnalyzer cpa = new ClassPathAnalyzer("seage.problem");

        List<String> list = cpa.analyzeClassPath();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

}

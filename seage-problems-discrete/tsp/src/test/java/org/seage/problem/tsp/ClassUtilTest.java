package org.seage.problem.tsp;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.problem.IProblemProvider;
import org.seage.classutil.ClassInfo;
import org.seage.classutil.ClassPathAnalyzer;
import org.seage.classutil.ClassUtil;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import java.util.List;

public class ClassUtilTest
{

    @Test
    public void test() throws Exception
    {   
//        ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
//
//        String guavaBasePackageName = Function.class.getPackage().getName();
//        ImmutableSet<ClassPath.ClassInfo> classes = classPath.getTopLevelClasses(TspProblemProvider.class.getPackage().getName());
//        
//        classes.
        TspProblemProvider p = new TspProblemProvider();
//        //ClassUtil.searchForClasses(IAlgorithmFactory.class, this.getClass().getPackage().getName()
        ClassInfo[] ci = ClassUtil.searchForClasses(IProblemProvider.class, this.getClass().getPackage().getName());
//
        assertNotNull(ci);
        assertTrue(ci.length > 0);
        
        String[] s = ClassUtil.searchForInstancesInJar("instances", this.getClass().getPackage().getName());
        assertNotNull(s);
        assertTrue(s.length > 0);
    }

}

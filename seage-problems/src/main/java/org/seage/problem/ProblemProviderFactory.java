package org.seage.problem;

import java.util.HashMap;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.problem.tsp.TspProblemProvider;

public class ProblemProviderFactory 
{    
    static Class<?>[] providers = {
            TspProblemProvider.class
    };    
            
    @SuppressWarnings("unchecked")
    public static synchronized HashMap<String, IProblemProvider<Phenotype<?>>> getProblemProviders() throws Exception
    {
        HashMap<String, IProblemProvider<Phenotype<?>>> result = new HashMap<String, IProblemProvider<Phenotype<?>>>();
        
        for (Class<?> c : providers) {            
            result.put(c.getAnnotation(Annotations.ProblemId.class).value(), (IProblemProvider<Phenotype<?>>)c.newInstance());
        }
        
        return result;
    }
}

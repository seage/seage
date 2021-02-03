package org.seage.problem.sat.antcolony;

import java.util.ArrayList;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.antcolony.AntColonyAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;
import org.seage.problem.sat.SatPhenotype;

@Annotations.AlgorithmId("AntColony")
@Annotations.AlgorithmName("AntColony")
public class SatAntColonyFactory implements IAlgorithmFactory<SatPhenotype, Ant> {

  @Override
  public Class<?> getAlgorithmClass() {
    return AntColonyAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<SatPhenotype, Ant> createAlgorithm(ProblemInstance instance,
      IPhenotypeEvaluator<SatPhenotype> phenotypeEvaluator) throws Exception {
    Formula formula = (Formula) instance;
    Graph graph = new SatGraph(formula, new FormulaEvaluator(formula));
    SatAntBrain brain = new SatAntBrain(graph, formula);

    return new AntColonyAdapter<SatPhenotype, Ant>(graph, phenotypeEvaluator) {

      @Override
      public void solutionsFromPhenotype(SatPhenotype[] source) throws Exception {
        _ants = new Ant[source.length];
        for (int i = 0; i < _ants.length; i++) {
          ArrayList<Integer> nodes = new ArrayList<Integer>();
          for (int j = 1; j <= source[i].getSolution().length; j++)
            nodes.add((Boolean) source[i].getSolution()[j - 1] == true ? j : -j);
          _ants[i] = new Ant(brain, graph, nodes);
        }
      }

      @Override
      public SatPhenotype[] solutionsToPhenotype() throws Exception {
        SatPhenotype[] result = new SatPhenotype[_ants.length];
        for (int i = 0; i < _ants.length; i++) {
          result[i] = new SatPhenotype(new Boolean[_ants[i].getNodeIDsAlongPath().size()]);
          for (int j = 0; j < result[i].getSolution().length; j++) {
            Boolean[] s = result[i].getSolution();
            Integer value = (Integer) _ants[i].getNodeIDsAlongPath().get(j);
            s[j] = value > 0;// .toArray(new
          }
        }
        return result;
      }

      @Override
      public SatPhenotype solutionToPhenotype(Ant solution) throws Exception {
        // TODO Auto-generated method stub
        return null;
      }

    };
  }

}

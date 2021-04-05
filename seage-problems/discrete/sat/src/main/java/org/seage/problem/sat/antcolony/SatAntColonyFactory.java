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
        ants = new Ant[source.length];
        for (int i = 0; i < ants.length; i++) {
          ArrayList<Integer> nodes = new ArrayList<Integer>();
          for (int j = 1; j <= source[i].getSolution().length; j++) {
            nodes.add((Boolean) source[i].getSolution()[j - 1] == true ? j : -j);
          }
          ants[i] = new Ant(brain, graph, nodes);
        }
      }

      @Override
      public SatPhenotype[] solutionsToPhenotype() throws Exception {
        SatPhenotype[] result = new SatPhenotype[ants.length];
        for (int i = 0; i < ants.length; i++) {
          result[i] = solutionToPhenotype(ants[i]);
        }
        return result;
      }

      @Override
      public SatPhenotype solutionToPhenotype(Ant solution) throws Exception {        
        SatPhenotype result = new SatPhenotype(new Boolean[solution.getNodeIDsAlongPath().size()]);
        for (int i = 0; i < result.getSolution().length; i++) {
          Integer value = (Integer) solution.getNodeIDsAlongPath().get(i);
          result.getSolution()[i] = value > 0;
        }
        double[] objVals = this.phenotypeEvaluator.evaluate(result);
        result.setObjValue(objVals[0]);
        result.setScore(objVals[1]);
        return result;
      }

    };
  }

}

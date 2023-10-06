package org.seage.metaheuristic.genetics;

import java.util.ArrayList;
import java.util.List;

public class ContinuousGeneticOperator<S extends Subject<Double>>
    extends BasicGeneticOperator<S, Double> {
  private Limit[] _limits;

  public static class Limit {
    public double Min = 0;
    public double Max = 0;

    public Limit(double min, double max) {
      Min = min;
      Max = max;
    }
  }

  public ContinuousGeneticOperator(Limit[] limits) {
    _limits = limits;
  }

  // @Override
  public Subject<Double>[] crossOver0(Subject<Double> parent1, Subject<Double> parent2)
      throws Exception {
    @SuppressWarnings("unchecked")
    Subject<Double>[] children = (Subject<Double>[]) new Subject<?>[2];

    children[0] = parent1.clone();
    children[1] = parent2.clone();

    for (Subject<Double> s : children) {
      for (int i = 0; i < parent1.getChromosome().getLength(); i++) {
        if (_crossLengthCoef > _random.nextDouble()) {
          double value =
              (parent1.getChromosome().getGene(i) + parent2.getChromosome().getGene(i)) / 2;
          s.getChromosome().setGene(i, value);
        }
      }
    }
    return children;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<S> crossOver(S parent1, S parent2) throws Exception {   
    S child1 = (S)parent1.clone();
    S child2 = (S)parent2.clone();

    for (int i = 0; i < parent1.getChromosome().getLength(); i++) {
      if (_crossLengthCoef > _random.nextDouble()) {
        double x1 = child1.getChromosome().getGene(i);
        double x2 = child2.getChromosome().getGene(i);
        child1.getChromosome().setGene(i, x2);
        child2.getChromosome().setGene(i, x1);
      }
    }

    return List.of(child1, child2);
  }

  // @Override
  @Override
  public S mutate(S subject) throws Exception {
    for (int i = 0; i < subject.getChromosome().getLength(); i++) {
      if (_mutateLengthCoef > _random.nextDouble()) {
        int sign = _random.nextBoolean() ? 1 : -1;
        double value = subject.getChromosome().getGene(i);
        value += sign * value * _random.nextDouble() / 20; // 5% max
        value = Math.max(value, _limits[i].Min);
        value = Math.min(value, _limits[i].Max);
        // double value = _limits[i].Min + _random.nextDouble() * (_limits[i].Max - _limits[i].Min);
        subject.getChromosome().setGene(i, value);
      }
    }
    return subject;
  }

  @Override
  public S randomize(S subject) throws Exception {
    for (int i = 0; i < subject.getChromosome().getLength(); i++) {
      double value = _limits[i].Min + _random.nextDouble() * (_limits[i].Max - _limits[i].Min);
      subject.getChromosome().setGene(i, value);
    }
    return subject;
  }

}

package org.seage.aal.algorithm;

public abstract class Phenotype<T> {
  protected T _solution;

  public Phenotype(T solution) {
    _solution = solution;
  }

  public T getSolution() {
    return _solution;
  }

  public abstract String toText();

  public abstract void fromText(String text);

  // public abstract boolean isSame(Phenotype<T> p);

}

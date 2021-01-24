package org.seage.aal.algorithm;

import org.seage.data.HashHelper;

public abstract class Phenotype<T> {
  protected T solution;

  public Phenotype(T solution) {
    this.solution = solution;
  }

  public T getSolution() {
    return this.solution;
  }

  public abstract String toText();

  public abstract void fromText(String text);

  public String computeHash() throws Exception {
    return HashHelper.hashFromString(toText());    
  }

  // public abstract boolean isSame(Phenotype<T> p);

}

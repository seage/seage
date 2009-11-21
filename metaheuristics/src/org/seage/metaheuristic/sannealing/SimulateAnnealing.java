package org.seage.metaheuristic.sannealing;

/**
 * @author Jan Zmátlík
 */

public class SimulateAnnealing implements ISimulatedAnnealing
{ //extends Thread


  /**
   * The current temperature.
   */
  protected double temperature;

  /**
   * The current solution
   */
  protected double currentSolution;

  /**
   * The best solution.
   */
  protected double bestSolution;

  /**
   * The current order of cities.
   */
  public int order[];

  /**
   * The best order of cities.
   */
  public int minimalorder[];

  /**
   * Constructor
   *
   * @param owner of this object.
   */
  public SimulateAnnealing(ISimulatedAnnealing owner)
  {
    this.owner = owner;
    order = new int[owner.getCount()];
    minimalorder = new int[owner.getCount()];
  }

  /**
   * Called to determine if annealing should take place.
   *
   * @param d The distance.
   * @return True if annealing should take place.
   */
  public boolean anneal(double distance)
  {
    if (this.temperature < 1.0E-4)
      return (distance > 0.0) ? true : false;
   
    if (Math.random() < Math.exp(distance / this.temperature))
      return true;
    else
      return false;
  }

  /**
   * This method is called to
   * perform the simulated annealing.
   */
  public void start()
  {
    // pocet iteraci, jenom kvuli vypisu
    int iterationCount = 1;
    // pocet iteraci horsich ci stejnych delek cest
    int sameOrWorsePathCount = 0;
    // nastavime pocatecni teplotu
    this.temperature = owner.getMaximalTemperature();

    this.owner.setMessage("Adjusted maximal temperature");

    // vytvori serazenou poslopnost pro aktualni pozice mest
    initorder(order);
    // a pro minimalni pozice mest
    initorder(minimalorder);

    // zjisti se delka cesty mezi mesty a ze zacatku ji prohlasime jako nejkratsi
    bestSolution = currentSolution = owner.getSolution();

    this.owner.setMessage("Maximum path length: " + currentSolution);

    while (sameOrWorsePathCount < 50 && temperature >= owner.getMinimalTemperature())
    {
        this.owner.setMessage("---");

        double count = Math.pow(owner.getCount() , 2);
      // make adjustments to city order(annealing)
      for (int j2 = 0; j2 < count; j2++)
      {
        int i1 = (int)Math.floor((double)owner.getCount() * Math.random());
        int j1 = (int)Math.floor((double)owner.getCount() * Math.random());

        //this.owner.setMessage("i1: " + i1);
        //this.owner.setMessage("j1: " + j1);

        double distance = owner.getDistance(i1, i1 + 1) + owner.getDistance(j1, j1 + 1) - owner.getDistance(i1, j1) - owner.getDistance(i1 + 1, j1 + 1);

        //this.owner.setMessage("+ owner.getDistance(i1, i1 + 1): " + owner.getDistance(i1, i1 + 1));
        //this.owner.setMessage("+ owner.getDistance(j1, j1 + 1): " + owner.getDistance(j1, j1 + 1));
        //this.owner.setMessage("- owner.getDistance(i1, j1): " + owner.getDistance(i1, j1));
        //this.owner.setMessage("- owner.getDistance(i1 + 1, j1 + 1): " + owner.getDistance(i1 + 1, j1 + 1));

        //this.owner.setMessage("Distance: " + distance);

        if (anneal(distance))
        {
          if (j1 < i1)
          {
            int k1 = i1;
            i1 = j1;
            j1 = k1;
          }
          
          for (; j1 > i1; j1--) {
            int i2 = order[i1 + 1];
            order[i1 + 1] = order[j1];
            order[j1] = i2;
            i1++;
          }
        }
      }

      // See if this improved anything
      currentSolution = owner.getSolution();
      this.owner.setMessage("NEW path length: " + currentSolution);

      // pokud je aktualni cesta mensi nez nejmensi cesta
      if (currentSolution < bestSolution) {
        // tak ji uloz, a uloz poradi uzlu
        bestSolution = currentSolution;
        for (int k2 = 0; k2 < owner.getCount(); k2++)
          minimalorder[k2] = order[k2];
        sameOrWorsePathCount = 0;
      }
      // pokud neni proved dalsi iteraci
      else
        sameOrWorsePathCount++;

      this.owner.setMessage("MINIMAL path length: " + bestSolution);
      this.owner.setMessage("Same or worse path count: " + sameOrWorsePathCount);
      temperature = owner.getAnnealingCoefficient() * temperature;
      this.owner.setMessage("Actual temperature: " + temperature);
      iterationCount++;
    }

    owner.setMessage("Solution found after " + iterationCount + " cycles." );
  }

  /**
   * Set the specified array to have a list of the cities in
   * order.
   *
   * @param an An array to hold the cities.
   */
//  public void initorder(int an[])
//  {
//    for (int i = 0; i < owner.getCount(); i++)
//      an[i] = i;
//  }

    public double getAnnealingCoefficient() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getMaximalTemperature() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getMinimalTemperature() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getSolution() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAnnealingCoefficient(double alpha) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMaximalTemperature(double temp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMinimalTemperature(double temp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void start() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
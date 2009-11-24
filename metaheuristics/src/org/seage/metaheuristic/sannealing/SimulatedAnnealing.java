package org.seage.metaheuristic.sannealing;


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

/**
 * @author Jan Zmátlík
 */

public class SimulatedAnnealing implements ISimulatedAnnealing
{

  /**
   * The maximal temperature.
   */
  private double _currentTemperature;

  /**
   * The maximal temperature.
   */
  private double _maximalTemperature;

  /**
   * The minimal temperature.
   */
  private double _minimalTemperature;

  /**
   * The annealing coeficient
   */
  private double _annealCoefficient;

  /**
   * Provide firing Events, registering listeners
   */
  private SimulatedAnnealingListenerProvider _listenerProvider = new SimulatedAnnealingListenerProvider();
  
  
  private boolean _fireNewBestSolutionFound = false;

  private boolean _fireNewCurrentSolutionFound = false;

  private boolean _fireSimulatedAnnealingStopped = false;

  private boolean _fireSimulatedAnnealingStarted = false;

  private Solution _solution;



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
  public SimulatedAnnealing(Solution solution)
  {
    _solution = solution;
    //order = new int[owner.getCount()];
    //minimalorder = new int[owner.getCount()];
  }  

  /**
   * Called to determine if annealing should take place.
   *
   * @param distance The distance.
   * @return True if annealing should take place.
   */
  public boolean anneal(double distance)
  {   
    return (Math.random() < Math.exp(distance / _currentTemperature)) ? true : false;
  }

  /**
   * This method is called to
   * perform the simulated annealing.
   */
  public void start()
  {
    // pocet iteraci, jenom kvuli vypisu
    int iterationCount = 1;

    _currentTemperature = _maximalTemperature;

      System.out.println("Maximal and current temperature " + _currentTemperature);

      _fireSimulatedAnnealingStarted = true;
    //this.owner.setMessage("Adjusted maximal temperature");

    // vytvori serazenou poslopnost pro aktualni pozice mest
    //***initorder(order);
    // a pro minimalni pozice mest
    //***initorder(minimalorder);

    // zjisti se delka cesty mezi mesty a ze zacatku ji prohlasime jako nejkratsi
    //*** pocatecni reseni
    //***bestSolution = currentSolution = owner.getSolution();

    Solution bestSolution = _solution;
      System.out.println("Solut " + _solution.getObjectiveValue());
    bestSolution.setObjectiveValue(123);

      System.out.println("Best " + bestSolution.getObjectiveValue());
      System.out.println("Solut " + _solution.getObjectiveValue() );

    //this.owner.setMessage("Maximum path length: " + currentSolution);

    while (_currentTemperature >= _minimalTemperature)
    {
        System.out.println(iterationCount);
        System.out.println("Temp " + _currentTemperature);

        performOneIteration();        


        //***double count = Math.pow(owner.getCount() , 2);
      // make adjustments to city order(annealing)
     /* for (int j2 = 0; j2 < count; j2++)
      {
        int i1 = (int)Math.floor((double)owner.getCount() * Math.random());
        int j1 = (int)Math.floor((double)owner.getCount() * Math.random());*/

        //this.owner.setMessage("i1: " + i1);
        //this.owner.setMessage("j1: " + j1);

        //***double distance = owner.getDistance(i1, i1 + 1) + owner.getDistance(j1, j1 + 1) - owner.getDistance(i1, j1) - owner.getDistance(i1 + 1, j1 + 1);

        //this.owner.setMessage("+ owner.getDistance(i1, i1 + 1): " + owner.getDistance(i1, i1 + 1));
        //this.owner.setMessage("+ owner.getDistance(j1, j1 + 1): " + owner.getDistance(j1, j1 + 1));
        //this.owner.setMessage("- owner.getDistance(i1, j1): " + owner.getDistance(i1, j1));
        //this.owner.setMessage("- owner.getDistance(i1 + 1, j1 + 1): " + owner.getDistance(i1 + 1, j1 + 1));

        //this.owner.setMessage("Distance: " + distance);

        /*if (anneal(distance))
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
      }*/

      // See if this improved anything
      //***currentSolution = owner.getSolution();
      //***this.owner.setMessage("NEW path length: " + currentSolution);

      // pokud je aktualni cesta mensi nez nejmensi cesta
      /*if (currentSolution < bestSolution) {
        // tak ji uloz, a uloz poradi uzlu
        bestSolution = currentSolution;
        for (int k2 = 0; k2 < owner.getCount(); k2++)
          minimalorder[k2] = order[k2];
      }*/


      //***this.owner.setMessage("MINIMAL path length: " + bestSolution);
      _currentTemperature = _annealCoefficient * _currentTemperature;
      
      //***this.owner.setMessage("Actual temperature: " + _currentTemperature);
      fireEvents();
      iterationCount++;
    }
      _fireSimulatedAnnealingStopped = true;
      fireEvents();
  }

  private void performOneIteration()
  {
        //##################################
        // move randomly to new locations

        //##################################
        // calculate objective function F

        //##################################
        // accept new solution if better

        //##################################
        //
  }

  public final void addSimulatedAnnealingListener( ISimulatedAnnealingListener listener )
  {
    _listenerProvider.addSimulatedAnnealingListener( listener );
  }

  private void fireEvents()
  {
    if( _fireSimulatedAnnealingStarted )
    {
        _listenerProvider.fireSimulatedAnnealingStarted();
        _fireSimulatedAnnealingStarted = false;
    }

    if( _fireSimulatedAnnealingStopped )
    {
        _listenerProvider.fireSimulatedAnnealingStopped();
        _fireSimulatedAnnealingStopped = false;
    }

    if( _fireNewBestSolutionFound )
    {
        _listenerProvider.fireNewBestSolutionFound();
        _fireNewBestSolutionFound = false;
    }
    
    if( _fireNewCurrentSolutionFound )
    {
        _listenerProvider.fireNewCurrentSolutionFound();
        _fireNewCurrentSolutionFound = false;
    }
  }

  public double getMaximalTemperature()
  {
    return _maximalTemperature;
  }

  public void setMaximalTemperature(double maximalTemperature)
  {
    this._maximalTemperature = maximalTemperature;
  }

  public void setMinimalTemperature(double minimalTemperature)
  {
    this._minimalTemperature = minimalTemperature;
  }

  public double getMinimalTemperature()
  {
    return _minimalTemperature;
  }

  public void setAnnealingCoefficient(double alpha)
  {
    this._annealCoefficient = alpha;
  }
  
  public double getAnnealingCoefficient()
  {
    return this._annealCoefficient;
  }

  public double getSolution() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
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

  private Solution _currentSolution;

  private Solution _bestSolution;

  private IMoveManager _moveManager;

  private IObjectiveFunction _objectiveFunction;

  /**
   * The current solution
   */
  //protected double currentSolution;

  /**
   * The best solution.
   */
  //protected double bestSolution;

  /**
   * The current order of cities.
   */
  //public int order[];

  /**
   * The best order of cities.
   */
  //public int minimalorder[];

  /**
   * Constructor
   *
   * @param objectiveFunction is objective function.
   * @param moveManager is performing modification solution.
   */
  public SimulatedAnnealing(IObjectiveFunction objectiveFunction, IMoveManager moveManager)
  {
    //order = new int[owner.getCount()];
    //minimalorder = new int[owner.getCount()];
      _objectiveFunction = objectiveFunction;
      _moveManager = moveManager;
  }  

  /**
   * Called to determine if annealing should take place.
   *
   * @param Delta is value from objective function.
   * @return True if annealing should take place.
   */
  public boolean anneal(double delta)
  {   
    return ( Math.random() < Math.exp( delta / _currentTemperature ) ) ? true : false;
  }

  /**
   * This method is called to
   * perform the simulated annealing.
   */
  public void startSearching(Solution solution)
  {
    // pocet iteraci, jenom kvuli vypisu
    int iterationCount = 1;

    _currentTemperature = _maximalTemperature;

    _bestSolution = _currentSolution = solution;
    System.out.println("Maximal and current temperature " + _currentTemperature);

    _listenerProvider.fireSimulatedAnnealingStarted();

    // vytvori serazenou poslopnost pro aktualni pozice mest
    //***initorder(order);
    // a pro minimalni pozice mest
    //***initorder(minimalorder);

    // zjisti se delka cesty mezi mesty a ze zacatku ji prohlasime jako nejkratsi
    //*** pocatecni reseni
    //***bestSolution = currentSolution = owner.getSolution();

    while (_currentTemperature >= _minimalTemperature)
    {
        System.out.println("Iteration:" +  iterationCount);
        System.out.println("Temp " + _currentTemperature);

        performOneIteration();        


        //***double count = Math.pow(owner.getCount() , 2);
      // make adjustments to city order(annealing)
     /* for (int j2 = 0; j2 < count; j2++)
      {
        int i1 = (int)Math.floor((double)owner.getCount() * Math.random());
        int j1 = (int)Math.floor((double)owner.getCount() * Math.random());*/

        //***double distance = owner.getDistance(i1, i1 + 1) + owner.getDistance(j1, j1 + 1) - owner.getDistance(i1, j1) - owner.getDistance(i1 + 1, j1 + 1);

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
        // solution = length
      //***currentSolution = owner.getSolution();
      //***this.owner.setMessage("NEW path length: " + currentSolution);

      // pokud je aktualni cesta mensi nez nejmensi cesta
      /*if (currentSolution < bestSolution) {
        // tak ji uloz, a uloz poradi uzlu
        bestSolution = currentSolution;
        for (int k2 = 0; k2 < owner.getCount(); k2++)
          minimalorder[k2] = order[k2];
      }*/

      _currentTemperature = _annealCoefficient * _currentTemperature;
      
      iterationCount++;
      System.out.println();
    }
      _listenerProvider.fireSimulatedAnnealingStopped();
  }

  private void performOneIteration()
  {
      // porad stejny current solution
      System.out.println("CurrentLength: " + _currentSolution.getObjectiveValue());
        //##################################
        // move randomly to new locations
      Solution modifiedSolution = _moveManager.getModifiedSolution( _currentSolution );
      System.out.println("ModifiedLength: " + modifiedSolution.getObjectiveValue());


        //##################################
        // calculate objective function F
      _objectiveFunction.setObjectiveValue( modifiedSolution );
      System.out.println("NewModifiedLength: " + modifiedSolution.getObjectiveValue());
      double modifiedObjectiveValue = modifiedSolution.getObjectiveValue();

        //##################################
        // accept new solution if better
      if(modifiedObjectiveValue < _bestSolution.getObjectiveValue())
      {
          _bestSolution = modifiedSolution;
          _listenerProvider.fireNewBestSolutionFound();
      }

      while(!anneal( modifiedObjectiveValue - _currentSolution.getObjectiveValue() ) )
      {
        _currentSolution = modifiedSolution;
          System.out.println("OOOOOOOOOKKKKKKKKKKKKKKKK");
      }

      System.out.println("BestSolution: " + _bestSolution.getObjectiveValue());
  }

  public final void addSimulatedAnnealingListener( ISimulatedAnnealingListener listener )
  {
    _listenerProvider.addSimulatedAnnealingListener( listener );
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

  public Solution getSolution() {
        return _bestSolution;
    }

    public void stopSearching() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
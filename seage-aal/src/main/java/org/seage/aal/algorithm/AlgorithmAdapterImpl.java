/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.aal.algorithm;

import org.seage.aal.reporter.AlgorithmReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The very common AlgorithmAdapter implementation.
 * @author Richard Malek
 * @param <S> Solution template parameter.
 */
public abstract class AlgorithmAdapterImpl<P extends Phenotype<?>, S> 
    implements IAlgorithmAdapter<P, S> {
  private static final Logger logger = 
      LoggerFactory.getLogger(AlgorithmAdapterImpl.class.getName());

  protected boolean algorithmStarted = false;
  protected boolean algorithmStopped = false;

  protected AlgorithmReporter<P> reporter;
  protected IPhenotypeEvaluator<P> phenotypeEvaluator;

  public AlgorithmAdapterImpl(IPhenotypeEvaluator<P> phenotypeEvaluator) {
    this.phenotypeEvaluator = phenotypeEvaluator;
  }

  @Override
  public void startSearching(final AlgorithmParams params, boolean async) throws Exception {
    if (algorithmStarted && !algorithmStopped) {
      throw new RuntimeException("Algorithm already started, running.");
    }

    algorithmStarted = false;

    if (async == true) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            startSearching(params);
          } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
          }
        }
      }, this.getClass().getName()).start();

      int i = 0;
      while (!algorithmStarted) {
        Thread.sleep(200);
        if (i++ < 10) {
          ;// System.out.print("+");
        } else {
          stopSearching();
          throw new RuntimeException("Unable to start the algorithm asynchronously.");
        }
      }
    } else {
      startSearching(params);
    }
  }
}

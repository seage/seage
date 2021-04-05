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
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
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
 *
 * @author Richard Malek
 * @param <S>
 */
public abstract class AlgorithmAdapterImpl<P extends Phenotype<?>, S> implements IAlgorithmAdapter<P, S> {
  protected static final Logger _logger = LoggerFactory.getLogger(AlgorithmAdapterImpl.class.getName());

  protected boolean _algorithmStarted = false;
  protected boolean _algorithmStopped = false;

  protected AlgorithmReporter<P> _reporter;
  protected IPhenotypeEvaluator<P> _phenotypeEvaluator;

  public AlgorithmAdapterImpl(IPhenotypeEvaluator<P> phenotypeEvaluator) {
    this._phenotypeEvaluator = phenotypeEvaluator;
  }

  @Override
  public void startSearching(final AlgorithmParams params, boolean async) throws Exception {
    if (_algorithmStarted && !_algorithmStopped)
      throw new RuntimeException("Algorithm already started, running.");

    _algorithmStarted = false;

    if (async == true) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            startSearching(params);
          } catch (Exception ex) {
            _logger.error(ex.getMessage(), ex);
          }
        }
      }, this.getClass().getName()).start();

      int i = 0;
      while (!_algorithmStarted) {
        Thread.sleep(10);
        if (i++ < 10)
          ;// System.out.print("+");
        else {
          stopSearching();
          throw new RuntimeException("Unable to start the algorithm asynchronously.");
        }
      }
    } else
      startSearching(params);
  }
}

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
 *     Robert Harder
 *     - Initial implementation
 *     Richard Malek
 *     - Merge with SEAGE
 *     Karel Durkota
 */
package org.seage.metaheuristic.fireflies;

/**
 * These events are fired off by the {@link FireflySearch} and are useful for
 * extending the firefly search extras. These are lightweight events that only
 * contain a reference to their source object.
 *
 * @author Robert Harder, rharder@usa.net
 * @see FireflySearch
 * @see FireflySearchListener
 * @see java.util.EventObject
 * @version 1.0
 * @since 1.0
 */
public class FireflySearchEvent<S extends Solution> extends java.util.EventObject {

  /**
   * 
   */
  private static final long serialVersionUID = 3418158366582283978L;

  /**
   * Constructor that accepts the source of the event.
   * 
   * @param source The source of the event.
   * @since 1.0
   */
  public FireflySearchEvent(Object source) {
    super(source);
  } // end constructor

  /**
   * Casts the <code>source</code> as a {@link FireflySearch} prior to returning. It
   * is equivalent to <code>(FireflySearch) getSource()</code>.
   *
   * @return source as {@link FireflySearch}
   * @since 1.0
   */
  @SuppressWarnings("unchecked")
  public final IFireflySearch<S> getFireflySearch() {
    return (IFireflySearch<S>) source;
  } // end getFireflySearch

} // end class FireflySearchEvent

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
 *     Robert Harder
 *     - Initial implementation
 *     Richard Malek
 *     - Merge with SEAGE
 */
package org.seage.metaheuristic.tabusearch;

/**
 * These events are fired off by the {@link TabuSearch} and are useful for
 * extending the tabu search to include such things as long term memory and
 * other tabu extras. These are lightweight events that only contain a reference
 * to their source object.
 *
 * @author Robert Harder, rharder@usa.net
 * @see TabuSearch
 * @see TabuSearchListener
 * @see java.util.EventObject
 * @version 1.0
 * @since 1.0
 */
public class TabuSearchEvent extends java.util.EventObject {

  /**
   * 
   */
  private static final long serialVersionUID = -321748751786495861L;

  /**
   * Constructor that accepts the source of the event.
   * 
   * @param source The source of the event.
   * @since 1.0
   */
  public TabuSearchEvent(Object source) {
    super(source);
  } // end constructor

  /**
   * Casts the <tt>source</tt> as a {@link TabuSearch} prior to returning. It is
   * equivalent to <code>(TabuSearch) getSource()</code>.
   *
   * @return source as {@link TabuSearch}
   * @since 1.0
   */
  public final ITabuSearch getTabuSearch() {
    return (ITabuSearch) source;
  } // end getTabuSearch

} // end class TabuSearchEvent

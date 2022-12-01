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

package org.seage.aal.problem;

import org.seage.data.DataNode;

/**
 *
 * @author Richard Malek
 */
public class ProblemInstanceInfo extends DataNode {
  public enum ProblemInstanceOrigin {
    FILE, RESOURCE
  }

  private static final long serialVersionUID = 1L;

  protected String _instanceID;
  protected ProblemInstanceOrigin _origin;
  protected String _path;

  public ProblemInstanceInfo(DataNode instance) throws Exception {
    super(instance);
    _instanceID = getValueStr("id");
    _origin = ProblemInstanceOrigin.valueOf(getValueStr("type").toUpperCase());
    _path = getValueStr("path");
  }

  public ProblemInstanceInfo(String id, ProblemInstanceOrigin type, String path) {
    super("Instance");
    _instanceID = id;
    _origin = type;
    _path = path;
  }

  public ProblemInstanceOrigin getOrigin() {
    return _origin;
  }

  public String getPath() {
    return _path;
  }

  public String getInstanceID() {
    return _instanceID;
  }

  @Override
  public String toString() {
    return _instanceID + " - " + _origin + " - " + _path;
  }

}

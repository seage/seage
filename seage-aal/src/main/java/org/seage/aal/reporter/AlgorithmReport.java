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

package org.seage.aal.reporter;

import java.io.File;

import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A minor extension of the DataNode.
 * @author Richard Malek
 */
public class AlgorithmReport extends DataNode {
  private int id;

  public AlgorithmReport(String name) {
    super(name);
  }

  public AlgorithmReport(AlgorithmReport report) {
    super(report);
    this.id = report.id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
    putValue("id", id);
  }

}

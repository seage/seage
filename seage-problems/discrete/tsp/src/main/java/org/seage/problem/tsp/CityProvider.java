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
package org.seage.problem.tsp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Richard Malek
 */
public class CityProvider {
  private CityProvider() {}

  public static synchronized City[] readCities(InputStream stream) {
    ArrayList<City> result = new ArrayList<>();
    
    try (Scanner scanner = new Scanner(stream)) {

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.equals("EOF") || line.trim().startsWith("NODE_COORD_SECTION"))
          break;
      }

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.isBlank() || line.isEmpty()) continue;
        if (line.equals("EOF")) break;
        Double[] dataLine = readLine(line);
        result.add(new City(dataLine[0].intValue(), dataLine[1], dataLine[2]));
      }
    }
    return result.toArray(new City[0]);
  }

  private static Double[] readLine(String line) {
    ArrayList<Double> result = new ArrayList<>();
    Scanner scanner = new Scanner(line);
    scanner.useDelimiter("\\s+");
    for (int i = 0; i < 3; i++) {
      result.add(scanner.nextDouble());
    }
    scanner.close();
    return result.toArray(new Double[0]);
  }

  public static City[] generateCircleCities(int numCircleCities) {
    City[] result = new City[numCircleCities];

    for (int i = 0; i < result.length; i++) {
      double angle = 6.28 * i * (1.0 / numCircleCities);
      result[i] = new City(i, Math.cos(angle), Math.sin(angle));
    }

    return result;
  }
}

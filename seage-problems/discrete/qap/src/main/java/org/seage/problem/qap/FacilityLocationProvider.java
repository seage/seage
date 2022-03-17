/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */
package org.seage.problem.qap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Karel Durkota
 */
public class FacilityLocationProvider {
  synchronized public static Double[][][] readFacilityLocations(InputStream stream)
      throws Exception {
    Double[] allData;
    Double[][] res1, res2;
    Scanner scanner = new Scanner(stream);
    final int n; // instance size
    try {
      // Read the instance size - N
      String line = scanner.nextLine();
      // size of matrix
      n = Integer.valueOf(line.trim());

      res1 = new Double[n][n];
      res2 = new Double[n][n];

      allData = new Double[2 * n * n];
      int valuesRead = 0;

      // Read all data into one array: allData
      while (scanner.hasNext()) {
        line = scanner.nextLine();
        if (line.trim().isEmpty()) {          
          continue;
        }
        Double[] dataLine = readLine(line);
        for(int i = 0; i < dataLine.length;i++) {
          allData[i+valuesRead] = dataLine[i];         
        }
        valuesRead += dataLine.length;
      }
      // Split the one array (allData) into res1 (NxN, flows) and res2 (NxN, prices)
      for(int i=0;i<valuesRead;i++) {
        int type = i / (n*n);
        Double[][] m = type==0?res1:res2;
        int ix = (i-(type * n * n)) / n;
        int iy = i % n;
        m[ix][iy] = allData[i];
      }

    } finally {
      // ensure the underlying stream is always closed
      scanner.close();
    }
    Double[][][] res = new Double[2][n][n];
    res[0] = res1;
    res[1] = res2;

    return res;
  }

  private static Double[] readLine(String line) throws IOException {
    ArrayList<Double> result = new ArrayList<Double>();
    Scanner scanner = new Scanner(line.trim());
    scanner.useDelimiter("\\s+");
    while (scanner.hasNext()) {
      String value = scanner.next();
      result.add(Double.parseDouble(value));
    }
    scanner.close();
    return result.toArray(new Double[0]);
  }

  public static Double[][][] readFacilityLocations(String path) throws Exception {
    return FacilityLocationProvider.readFacilityLocations(new FileInputStream(path));
  }
}

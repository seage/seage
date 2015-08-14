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
package org.seage.problem.qap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *  @author Karel Durkota
 */
public class FacilityLocationProvider
{
    synchronized public static Double[][][] readFacilityLocations(InputStream stream) throws Exception
    {
        Double[][] res1, res2, res3;
        Scanner scanner = new Scanner(stream);
        final int n;
        try
        {

            String line = scanner.nextLine();
            //size of matrix
            n = Integer.valueOf(line.trim());
            //System.out.println("N = "+n);

            res1 = new Double[n][n];
            res2 = new Double[n][n];
            res3 = new Double[n][n];

            // read first matrix
            for (int i = 0; i < n; i++)
            {
                line = scanner.nextLine();
                if (line.trim().isEmpty())
                {
                    line = scanner.nextLine();
                }
                if (line.equals("EOF"))
                    break;
                //System.out.println(line);
                Double[] dataLine = readLine(line);
                res1[i] = dataLine;
            }

            /* read second matrix (optional)
             * if no matrix given, unit matrix is created
             */
            for (int i = 0; i < n; i++)
            {
                if (scanner.hasNext())
                {
                    line = scanner.nextLine();
                    if (line.trim().isEmpty())
                    {
                        line = scanner.nextLine();
                    }
                    //System.out.println(line);
                    Double[] dataLine = readLine(line);
                    res2[i] = dataLine;
                }
                else
                {
                    for (int j = 0; j < n; j++)
                    {
                        res2[i][j] = 1.0;
                    }
                }
            }

            /* read third matrix (optional)
             * if no matrix given, zero matrix is created
             */
            if (scanner.hasNext())
                scanner.nextLine();

            for (int i = 0; i < n; i++)
            {
                if (scanner.hasNext())
                {
                    line = scanner.nextLine();
                    if (line.trim().isEmpty())
                    {
                        i--;
                        continue;
                    }
                    Double[] dataLine = readLine(line);
                    res3[i] = dataLine;
                }
                else
                {
                    for (int j = 0; j < n; j++)
                    {
                        res3[i][j] = 0.0;
                    }
                }
            }
        }
        finally
        {
            //ensure the underlying stream is always closed
            scanner.close();
        }
        Double[][][] res = new Double[3][n][n];
        res[0] = res1;
        res[1] = res2;
        res[2] = res3;

        return res;
    }

    private static Double[] readLine(String line) throws IOException
    {
        ArrayList<Double> result = new ArrayList<Double>();
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(" ");
        while (scanner.hasNext())
        {
            try
            {
                result.add(Double.parseDouble(scanner.next()));
            }
            catch (NumberFormatException e)
            {

            }
        }
        scanner.close();
        return (Double[]) result.toArray(new Double[0]);
    }

    public static Double[][][] readFacilityLocations(String path) throws Exception
    {
        return FacilityLocationProvider.readFacilityLocations(new FileInputStream(path));
    }
}

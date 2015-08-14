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

import java.util.*;
import java.io.*;

/**
 *  @author Richard Malek
 */
public class CityProvider
{
    synchronized public static City[] readCities(InputStream stream) throws Exception
    {
        ArrayList<City> result = new ArrayList<City>();
        Scanner scanner = new Scanner(stream);
        try
        {

            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                if (line.equals("EOF") || line.trim().startsWith("NODE_COORD_SECTION"))
                    break;
            }

            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                if (line.equals("EOF"))
                    break;
                Double[] dataLine = readLine(line);
                result.add(new City(dataLine[0].intValue(), dataLine[1], dataLine[2]));
            }
        }
        finally
        {
            //ensure the underlying stream is always closed
            scanner.close();
        }
        return (City[]) result.toArray(new City[0]);
    }

    private static Double[] readLine(String line) throws IOException
    {
        ArrayList<Double> result = new ArrayList<Double>();
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(" ");
        //while ( scanner.hasNext() )
        for (int i = 0; i < 3; i++)
        {
            result.add(Double.parseDouble(scanner.next()));
        }
        scanner.close();
        return (Double[]) result.toArray(new Double[0]);
    }

    public static City[] generateCircleCities(int numCircleCities)
    {
        City[] result = new City[numCircleCities];

        for (int i = 0; i < result.length; i++)
        {
            double angle = 6.28 * i * (1.0 / numCircleCities);
            result[i] = new City(i, Math.cos(angle), Math.sin(angle));
        }

        return result;
    }
}

/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
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
    synchronized public static City[] readCities(String path) throws Exception
    {
        ArrayList<City> result = new ArrayList<City>();
        Scanner scanner = new Scanner(new File(path));
        try {
            
            while ( scanner.hasNextLine() )
            {
                String line = scanner.nextLine();
                if(line.equals("EOF") || line.trim().startsWith("NODE_COORD_SECTION"))
                    break;
            }

            while ( scanner.hasNextLine() )
            {
                String line = scanner.nextLine();
                if(line.equals("EOF"))
                    break;
                Double[] dataLine = readLine( line );
                result.add(new City(dataLine[0].intValue(), dataLine[1], dataLine[2]));
            }
        }
        finally {
          //ensure the underlying stream is always closed
          scanner.close();
        }
        return (City[])result.toArray(new City[0]);
    }

    private static Double[] readLine(String line) throws IOException
    {
        ArrayList<Double> result = new ArrayList<Double>();
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(" ");
        //while ( scanner.hasNext() )
        for(int i=0;i<3;i++)
        {
            result.add(Double.parseDouble(scanner.next()));
        }
        scanner.close();
        return (Double[])result.toArray(new Double[0]);
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

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
package org.seage.problem.qap;

import java.util.*;
import java.io.*;
/**
 *  @author Karel Durkota
 */
public class FacilityLocationProvider
{
    synchronized public static Double[][] readFacilityLocations(String path) throws Exception
    {
        TreeMap<Integer,TreeMap<Integer,Double>> result = new TreeMap<Integer,TreeMap<Integer,Double>>();
        Scanner scanner = new Scanner(new File(path));
        try {
            
            while ( scanner.hasNextLine() )
            {
                String line = scanner.nextLine();
                if(line.equals("EOF") || line.trim().startsWith("FACILITY_LOCATION_PRICE"))
                    break;
            }

            while ( scanner.hasNextLine() )
            {
                String line = scanner.nextLine();
                if(line.equals("EOF"))
                    break;
                Double[] dataLine = readLine( line );
                if(!result.containsKey(dataLine[0].intValue()))
                	result.put(dataLine[0].intValue(), new TreeMap<Integer,Double>());
                result.get(dataLine[0].intValue()).put(dataLine[1].intValue(), dataLine[2]);
                }
        }
        finally {
          //ensure the underlying stream is always closed
          scanner.close();
        }
        Double[][] res = new Double[result.size()][result.size()];
        for(int i=0;i<res.length;i++){
        	res[i] = result.get(i).values().toArray(new Double[0]);
        }
        return res;
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

//    public static City[] generateCircleCities(int numCircleCities)
//    {
//        City[] result = new City[numCircleCities];
//
//		for (int i = 0; i < result.length; i++)
//		{
//			double angle = 6.28 * i * (1.0 / numCircleCities);
//            result[i] = new City(i, Math.cos(angle), Math.sin(angle));
//		}
//
//        return result;
//    }
}

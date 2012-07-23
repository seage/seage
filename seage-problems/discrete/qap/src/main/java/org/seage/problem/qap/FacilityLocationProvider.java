/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 */

/**
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
    synchronized public static Double[][][] readFacilityLocations(InputStream stream) throws Exception
    {
        Double[][] res1,res2,res3;
        Scanner scanner = new Scanner(stream);
        final int n;
        try {
            
            String line = scanner.nextLine();
            //size of matrix
            n=Integer.valueOf(line.trim());
            //System.out.println("N = "+n);
            
            res1 = new Double[n][n];
            res2 = new Double[n][n];
            res3 = new Double[n][n];
            
            // read first matrix
            for(int i=0;i<n;i++)
            {
                line = scanner.nextLine();
                if(line.trim().isEmpty()){
                    line = scanner.nextLine();
                }
                if(line.equals("EOF"))
                    break;
                //System.out.println(line);
                Double[] dataLine = readLine( line );
                res1[i] = dataLine;
            }

            /* read second matrix (optional)
             * if no matrix given, unit matrix is created
             */
            for(int i=0;i<n;i++)
            {
                if(scanner.hasNext()){
                    line = scanner.nextLine();
                    if(line.trim().isEmpty()){
                        line = scanner.nextLine();
                    }
                    //System.out.println(line);
                    Double[] dataLine = readLine( line );
                    res2[i] = dataLine;
                }
                else{
                    for(int j=0;j<n;j++){
                        res2[i][j]=1.0;
                    }
                }
            }

            /* read third matrix (optional)
             * if no matrix given, zero matrix is created
             */
            if(scanner.hasNext())
                scanner.nextLine();

            for(int i=0;i<n;i++)
            {
                if(scanner.hasNext()){
                    line = scanner.nextLine();
                    if(line.trim().isEmpty()){
                        i--;
                        continue;
                    }
                    Double[] dataLine = readLine( line );
                    res3[i] = dataLine;
                }
                else{
                    for(int j=0;j<n;j++){
                        res3[i][j]=0.0;
                    }
                }
            }
        }
        finally {
          //ensure the underlying stream is always closed
          scanner.close();
        }
        Double[][][] res = new Double[3][n][n];
        res[0]=res1;
        res[1]=res2;
        res[2]=res3;

        return res;
    }

    private static Double[] readLine(String line) throws IOException
    {
        ArrayList<Double> result = new ArrayList<Double>();
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(" ");
        while ( scanner.hasNext() )
        {
            try{
                result.add(Double.parseDouble(scanner.next()));
            }catch(NumberFormatException e){
                
            }
        }
        scanner.close();
        return (Double[])result.toArray(new Double[0]);
    }

    public static Double[][][] readFacilityLocations(String path) throws Exception {
        return FacilityLocationProvider.readFacilityLocations(new FileInputStream(path));
    }
}

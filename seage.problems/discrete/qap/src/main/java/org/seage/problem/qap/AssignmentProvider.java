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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Karel Durkota
 */
public class AssignmentProvider
{
    public static void main(String[] args)
    {
        try
        {
            if(args.length == 0)
                throw new Exception("Usage: java org.seage.problem.tsp.TourProvider {data-qap-path}");

            long t0 = System.currentTimeMillis();
            
            System.out.println("Instance: "+args[0]);

            Double[][][] facilityLocation = FacilityLocationProvider.readFacilityLocations(args[0]);

            System.out.println("Facilities & Locations: "+facilityLocation.length);
            System.out.println();

            System.out.println("Read: "+(System.currentTimeMillis() - t0)  + " ms");
            t0 = System.currentTimeMillis();


            Integer[] assignment = createGreedyAssignment(facilityLocation);
            System.out.println("Creation: "+(System.currentTimeMillis() - t0) / 1000 + " s");
            t0 = System.currentTimeMillis();

            double totalPrice = getTotalPrice(assignment, facilityLocation);
            System.out.println("Evaluation: "+(System.currentTimeMillis() - t0) + " ms");
            t0 = System.currentTimeMillis();

            // IMPLEMENT IN FUTURE
            //Visualizer.instance().createTable(facilityLocation, assignment, "assignment.png", 1000, 1000);
            System.out.println("Visualization: "+(System.currentTimeMillis() - t0) + " ms");

            System.out.println();
            System.out.println("Assignment total price: "+totalPrice);
            System.out.println("Time: "+(System.currentTimeMillis() - t0) / 1000 + " s");


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static Integer[] createGreedyAssignment(Double[][][] facilityLocation) throws Exception
    {

        Integer[] assignment = new Integer[facilityLocation[0].length];

        // Greedy cheapset initialize
        int[] avail = new int[ facilityLocation[0].length ];
        System.out.println("in assignmentProfider's begining"+facilityLocation.length);
        System.out.println(facilityLocation[0].length);
        System.out.println(facilityLocation[0][0].length);
        System.out.println("assignment.length is "+assignment.length+" and avail.length is "+avail.length);
        for( int i = 0; i < avail.length; i++ )
        {
            assignment[i] = -1;
            avail[i] = i;
        }

        // count sums of all rows
        double[][] sumMat = new double[2][facilityLocation[0].length];
        sumMat[0] = sumOfRows(facilityLocation[0]);
        sumMat[1] = sumOfRows(facilityLocation[1]);

        for(int i=0;i<assignment.length;i++){
            int location = -1;
            double price = Double.MAX_VALUE;
            for( int j=0;j<avail.length;j++){
                double newPrice=0;
                double a=sumMat[0][i];
                double b=sumMat[1][j];
                double add=facilityLocation[2][i][j];

                if(sumMat[0][i]*sumMat[1][j] + facilityLocation[2][i][j] < price)
                    if(avail[j] >= 0){
                    price = sumMat[0][i]*sumMat[1][j] + facilityLocation[2][i][j];
                    location = j;
                }
            }
            assignment[i]=location;
            avail[location] = -1;
        }

        return assignment;
    }

    public static double[] sumOfRows(Double[][] matrix){
        double[] res = new double[matrix.length];
        for(int i=0;i<matrix.length;i++){
            res[i]=addArray(matrix[i]);
        }
        return res;
    }

    public static double addArray(Double[] row){
        double res = 0;
        for(int i=0;i<row.length;i++)
            res+=row[i];
        return res;
    }

    // TODO: A - create better implementation
    public static Integer[] createRandomAssignment(Double[][][] facilityLocation)
    {
        Integer[] assign = new Integer[ facilityLocation[0][0].length ];
        List<Integer> listAssign = new ArrayList();
        for (int i = 0; i < facilityLocation.length; i++) {
            listAssign.add(i);
        }
        Collections.shuffle(listAssign);
        return listAssign.toArray(new Integer[0]);
    }

    public static Integer[] createSortedAssignment(Double[][][] facilityLocation)
    {
        Integer[] tour = new Integer[ facilityLocation[0].length ];
        for(int i = 0; i < tour.length; i++)
            tour[i] = i;

        return tour;
    }

    public static double getTotalPrice(Integer[] assignment, Double[][][] facilityLocation) throws Exception
    {
        double price = 0;
        for(int i=0;i<facilityLocation[0][0].length;i++){
            for(int j=0;j<facilityLocation[0][0].length;i++){
                price+=facilityLocation[0][i][j]*facilityLocation[1][assignment[i]][assignment[j]];
            }
        }
        double addition=0;
        for(int i=0;i<facilityLocation[0][0].length;i++){
            addition+=facilityLocation[2][i][assignment[i]];
        }

        return price+addition;
    }
}

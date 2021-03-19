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
 * Contributors: David Omrai - Initial implementation
 */

package org.seage.sandbox;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// import org.seage.metaheuristic.genetics.Subject;
// import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.TourProvider;
import org.seage.problem.tsp.TspPhenotypeEvaluator;
import org.seage.problem.tsp.TspPhenotype;

public class MetadataGenerator {
    public static void main(String[] args) {
        try {
          String[] tspInstancesID = new String[]{
            "hyflex-tsp-0",
            "hyflex-tsp-8",
            "hyflex-tsp-2",
            "hyflex-tsp-7",
            "hyflex-tsp-6"
          };
          String[] satInstancesID = new String[]{
            "hyflex-sat-3",
            "hyflex-sat-5",
            "hyflex-sat-4",
            "hyflex-sat-10",
            "hyflex-sat-11"
          };
    
          new MetadataGenerator().run(tspInstancesID, satInstancesID);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    
      public void run(String[] tspInstancesID, String[] satInstancesID) throws Exception {
       tspMetaGenerator(1000, tspInstancesID);
      }

      public void tspMetaGenerator( int populationCount, String[] instancesID ) throws Exception {
        //iterate through all instances
        for (String instanceID: instancesID){
          String path = String.format("/org/seage/problem/tsp/instances/%s.tsp", instanceID);
          City[] cities = null;
          try(InputStream stream = getClass().getResourceAsStream(path)) {    
            cities = CityProvider.readCities(stream);
          }

          System.out.println("cities len " + cities.length);

          System.out.println("Population: " + populationCount);
          List<TspPhenotype> initialSolutions = generateRandomSubjects(cities, populationCount);


          TspPhenotypeEvaluator tspEval = new TspPhenotypeEvaluator(cities);

          for (int i = 0; i < populationCount; i++) {
            System.out.println(tspEval.evaluate(initialSolutions.get(i))[0]);
          }
        } 
      }

      private List<TspPhenotype> generateRandomSubjects(City[] cities, int subjectCount) throws Exception {
        List<TspPhenotype> result = new ArrayList<>(subjectCount);
    
        Integer[][] tours = new Integer[subjectCount][];
        for (int k = 0; k < subjectCount; k++){
          tours[k] = TourProvider.createRandomTour(cities.length);
        }
        
        for (int k = 0; k < subjectCount; k++)
          result.add(new TspPhenotype(tours[k]));
        return result;
      }
}

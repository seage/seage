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
import java.util.List;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.TourProvider;

public class MetadataGenerator {
    public static void main(String[] args) {
        try {
          // String instanceID = "eil51"; // 426
          // String instanceID = "berlin52"; // 7542
          // String instanceID = "ch130"; // 6110
          // String instanceID = "lin318"; // 42029
          // String instanceID = "pcb442"; // 50778
          // String instanceID = "u574"; // 36905
          String instanceID = "hyflex-tsp-9"; // 426
    
          new MetadataGenerator().run(instanceID);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    
      public void run(String instanceID) throws Exception {
        String path = String.format("/org/seage/problem/tsp/instances/%s.tsp", instanceID);
        City[] cities = null;
        try(InputStream stream = getClass().getResourceAsStream(path)) {    
          cities = CityProvider.readCities(stream);
        }
        int populationCount = 1000;
        System.out.println("Population: " + populationCount);
        List<Subject<Integer>> initialSolutions = generateInitialSubjects(cities, populationCount);

        System.out.println(initialSolutions);
      }

      private List<Subject<Integer>> generateInitialSubjects(City[] cities, int subjectCount) throws Exception {
        ArrayList<Subject<Integer>> result = new ArrayList<>(subjectCount);
    
        Integer[][] tours = new Integer[subjectCount][];
        for (int k = 0; k < subjectCount; k++)
          tours[k] = TourProvider.createRandomTour(cities.length);
        
        for (int k = 0; k < subjectCount; k++)
          result.add(new Subject<>(tours[k]));
        return result;
      }
}

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
 *               David Omrai - Implementation
 */

package org.seage.hh.experimenter.configurator;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.hh.knowledgebase.db.DbManager;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.seage.hh.knowledgebase.db.mapper.ExperimentTaskMapper;

/**
 * New Feedback configurator
 * @author David Omrai
 */
public class NewFeedbackConfigurator extends Configurator {
  private double spread;


  /**
   * Default constructor, does nothing.
   */
  public NewFeedbackConfigurator() {
    // Default empty constructor.
  }

  /**
   * 
   * @param problemId
   * @param algorithmId
   * @param limit
   * @return
   */
  public List<ExperimentTaskRecord> getBestExperimentTasks(
    String problemId, String algorithmId, int limit) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      ExperimentTaskMapper mapper = session.getMapper(ExperimentTaskMapper.class);
      return null; //mapper.getBestExperimentTaks(problemId, algorithmId, limit);
    }
  }

  @Override
  public ProblemConfig[] prepareConfigs(
      ProblemInfo problemInfo, String instanceID, String algID, int numConfigs)
      throws Exception {

    List<ProblemConfig> results = new ArrayList<>();

    results.add(createConfig(problemInfo, instanceID, algID, 0));
    for (int i = 1; i < numConfigs; i++) {
      double s = Math.random() * this.spread;
      results.add(createConfig(problemInfo, instanceID, algID, s));
    }

    return results.toArray(new ProblemConfig[0]);
  }
  
  private ProblemConfig createConfig(
      ProblemInfo problemInfo, String instanceID, String algID, double spread)
      throws Exception {
    DataNode problem = new DataNode("Problem");
    problem.putValue("id", problemInfo.getValue("id"));
    problem.putDataNode(problemInfo.getDataNode("Instances").getDataNodeById(instanceID));
    
    DataNode algorithm = new DataNode("Algorithm");
    algorithm.putValue("id", algID);
    
    DataNode params = new DataNode("Parameters");
    ProblemConfig config = new ProblemConfig("Config");
    for (DataNode dn : problemInfo
        .getDataNode("Algorithms").getDataNodeById(algID).getDataNodes("Parameter"))
    {
      params.putValue(dn.getValueStr("name"), null);
      // instead of null add a value that makes sense
      // try to find how the value is set by other configurators
      List<ExperimentTaskRecord> bestRes = getBestExperimentTasks(
        dn.getValueStr("name"), algID, 1);

      // params.putValue(dn.getValueStr("name"), 
      //     bestDefaultParams.get(problemInfo.getValue("id"))
      //       .get(algID).get(dn.getValueStr("name")));
    }      

    algorithm.putDataNode(params);

    config.putDataNode(problem);
    config.putDataNode(algorithm);
    return config;
  }

}

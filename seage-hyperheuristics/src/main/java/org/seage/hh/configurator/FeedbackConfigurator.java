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

package org.seage.hh.configurator;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
import org.seage.hh.knowledgebase.db.DbManager;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.seage.hh.knowledgebase.db.mapper.ExperimentTaskMapper;

/**
 * New Feedback configurator.
 *
 * @author David Omrai
 */
public class FeedbackConfigurator extends Configurator {
  private double spread;


  /**
   * Default constructor, does nothing.
   */
  public FeedbackConfigurator(double spread) {
    this.spread = spread;
  }

  /** 
   * .
   *
   * @param problemId .
   * @param algorithmId .
   * @param limit .
   * @return .
   */
  public List<ExperimentTaskRecord> getBestExperimentTasks(
      String algorithmId, String problemId, int limit) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      ExperimentTaskMapper mapper = session.getMapper(ExperimentTaskMapper.class);    
      return mapper.getBestExperimentTasks(algorithmId, problemId, limit);
    }
  }

  /**
   * .
   *
   * @param algorithmId .
   * @param problemId .
   * @param instanceId .
   * @param limit .
   * @return
   *
   * @throws Exception .
   */
  public List<ExperimentTaskRecord> getBestExperimentTasks(
      String algorithmId, String problemId, String instanceId, int limit) throws Exception {
    try (SqlSession session = DbManager.getSqlSessionFactory().openSession()) {
      ExperimentTaskMapper mapper = session.getMapper(ExperimentTaskMapper.class);    
      return mapper.getBestExperimentTasks2(algorithmId, problemId, instanceId, limit);
    }
  }

  @Override
  public ProblemConfig[] prepareConfigs(
      ProblemInfo problemInfo, String instanceID, String algID, int numConfigs)
      throws Exception {

    // Get best config from db
      
    List<ProblemConfig> results = new ArrayList<>();

    // Get best config from db
    List<ExperimentTaskRecord> bestRes = getBestExperimentTasks(
        algID, problemInfo.getValue("id").toString(), instanceID, numConfigs);

    // Use best records to fill results to given numConfigs size
    if (!bestRes.isEmpty()) {
      for (int i = 0; i <= numConfigs / bestRes.size(); i++) {
        for (ExperimentTaskRecord expTaskRec : bestRes) {
          if (results.size() == numConfigs) {
            break;
          }
  
          results.add(createConfig(problemInfo, instanceID, algID, expTaskRec));
        }
      }
    }

    return results.toArray(new ProblemConfig[0]);
  }

  private ProblemConfig createConfig(
        ProblemInfo problemInfo, String instanceID, String algID, ExperimentTaskRecord expTaskRec
  ) throws Exception {
    // Extract the config from bestRes
    DataNode bestConfNode = XmlHelper.readXml(expTaskRec.getXmlConfig());


    DataNode problem = new DataNode("Problem");
    problem.putValue("id", problemInfo.getValue("id"));
    problem.putDataNode(problemInfo.getDataNode("Instances").getDataNodeById(instanceID));
    
    DataNode algorithm = new DataNode("Algorithm");
    algorithm.putValue("id", algID);
    
    DataNode params = new DataNode("Parameters");
    ProblemConfig config = new ProblemConfig("Config");

    for (DataNode dn : problemInfo
        .getDataNode("Algorithms").getDataNodeById(algID).getDataNodes("Parameter")) {
      String paramName = dn.getValueStr("name");
      params.putValue(paramName, null);
      // instead of null add a value that makes sense
      // try to find how the value is set by other configurators
      double min = dn.getValueDouble("min");
      double max = dn.getValueDouble("max");
      double init = dn.getValueDouble("init");
      double sign = Math.random() > 0.5 ? 1 : -1;
      double delta = (max - min) * this.spread * sign;
      
      double value = init;
      // Do not modify the default value for the 'iterationCount'
      if (!paramName.equals("iterationCount")) {
        if (bestConfNode.containsValue(paramName)) {
          value = Double.parseDouble(bestConfNode.getValue(paramName).toString());
        }        
        value = Math.max(min, Math.min(max, value + delta));
      }
      params.putValue(paramName, value);
    }      

    algorithm.putDataNode(params);

    config.putDataNode(problem);
    config.putDataNode(algorithm);
    
    return config;
  }
}

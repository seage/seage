package org.seage.hh.knowledgebase.db.mapper;

import org.seage.hh.knowledgebase.db.Experiment;

public interface ExperimentMapper {
  int insertExperiment(Experiment experiment);

  Experiment getExperiment(int id);
  
  int getMaxExperimentId();

  int getExperimentCount();
}

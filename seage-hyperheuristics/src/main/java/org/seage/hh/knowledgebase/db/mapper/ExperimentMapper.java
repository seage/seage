package org.seage.hh.knowledgebase.db.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.seage.hh.knowledgebase.db.Experiment;

public interface ExperimentMapper {
  
  @Insert("INSERT INTO seage.experiments(start_date, duration, experiment_type, hostname, config) VALUES(#{duration}, #{startDate}, #{experimentType}, #{hostname}, #{config})")
  @SelectKey(statement="call identity()", keyProperty="id", before=false, resultType=int.class)
  int insertExperiment(Experiment experiment);

  // @Results(id = "userResult", value = {
  //   @Result(property = "id", column = "id", id = true),
  //   @Result(property = "startDate", column = "start_date"),
  //   @Result(property = "duration", column = "duration"),
  //   @Result(property = "experimentType", column = "experiment_type"),
  //   @Result(property = "hostname", column = "hostname"),
  //   @Result(property = "config", column = "config")
  // })
  @Select("SELECT * FROM seage.experiments WHERE id = #{id}")
  Experiment getExperiment(int id);

  @Select("SELECT * FROM seage.experiments")
  List<Experiment> getExperiments();

  // int getMaxExperimentId();

  @Select("SELECT count(*) FROM seage.experiments")
  int getExperimentCount();
}

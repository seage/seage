package org.seage.hh.knowledgebase.db.mapper;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;
import org.seage.hh.experimenter.Experiment;

public interface ExperimentMapper {
  class ExperimentSqlProvider implements ProviderMethodResolver {
    private ExperimentSqlProvider() {}
    
    public static String insertExperimentSql() {
      return new SQL()
        .INSERT_INTO("seage.experiments")
        .VALUES("experiment_type", "#{experimentType}")
        .VALUES("experiment_id", "#{experimentID}::uuid")
        .VALUES("problem_id", "#{problemID}")
        .VALUES("instance_id", "#{instanceID}")
        .VALUES("algorithm_id", "#{algorithmID}")
        .VALUES("config", "#{config}")
        .VALUES("start_date", "#{startDate}")
        .VALUES("end_date", "#{endDate}")          
        .VALUES("score", "#{score}")
        .VALUES("scoreCard", "#{scoreCard}")
        .VALUES("host_info", "#{hostInfo}")
        .VALUES("format_version", "#{formatVersion}")
        .toString();
    }
  }

  @InsertProvider(type = ExperimentSqlProvider.class, method = "insertExperimentSql")
  // @Options(useGeneratedKeys = true, keyProperty = "id")
  int insertExperiment(Experiment experiment);


  @Select("SELECT * FROM seage.experiments WHERE experiment_id = #{experimentId}")
  @Results(id = "experimentResult", value = {
      @Result(property = "experimentType", column = "experiment_type"),
      @Result(property = "experimentID", column = "experiment_id"),
      @Result(property = "problemID", column = "problem_id"),
      @Result(property = "instanceID", column = "instance_id"),
      @Result(property = "algorithmID", column = "algorithm_id"),
      @Result(property = "config", column = "config"),
      @Result(property = "startDate", column = "start_date"),
      @Result(property = "endDate", column = "end_date"),      
      @Result(property = "score", column = "score"),
      @Result(property = "scoreCard", column = "score_card"),
      @Result(property = "hostInfo", column = "host_info"),
      @Result(property = "formatVersion", column = "format_version"),
  })
  Experiment getExperiment(UUID experimentId);

  @Select("SELECT * FROM seage.experiments")
  @ResultMap("experimentResult")
  List<Experiment> getExperiments();

  @Select("SELECT count(*) FROM seage.experiments")
  int getExperimentCount();

  @SuppressWarnings("LineLength")
  @Update("UPDATE seage.experiments SET end_date = #{endDate} WHERE experiment_id = #{experimentID}::uuid")
  void updateEndDate(@Param("experimentID") UUID experimentID, @Param("endDate") Date endDate);

  @SuppressWarnings("LineLengthCheck")
  @Update("UPDATE seage.experiments SET score = #{score} WHERE experiment_id = #{experimentID}::uuid")
  void updateScore(@Param("experimentID") UUID experimentID, @Param("score") double score);

  @SuppressWarnings("LineLengthCheck")
  @Update(
      "UPDATE seage.experiments SET scoreCard = #{scoreCard} WHERE experiment_id = #{experimentID}::uuid")
  void updateScoreCard(@Param("experimentID") UUID experimentID, @Param("scoreCard") String scoreCard);
}

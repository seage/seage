package org.seage.hh.knowledgebase.db.mapper;

import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;

public interface ExperimentTaskMapper {

  class ExperimentTaskSqlProvider implements ProviderMethodResolver {
    private ExperimentTaskSqlProvider() {}

    public static String insertExperimentTask() {
      return new SQL()
        .INSERT_INTO("seage.experiment_tasks")
        .VALUES("experiment_task_id", "#{experimentTaskID}::uuid")
        .VALUES("experiment_id", "#{experimentID}::uuid")          
        .VALUES("job_id", "#{jobID}")
        .VALUES("stage_id", "#{stageID}")
        .VALUES("problem_id", "#{problemID}")
        .VALUES("instance_id", "#{instanceID}")
        .VALUES("algorithm_id", "#{algorithmID}")
        .VALUES("config_id", "#{configID}")
        .VALUES("start_date", "#{startDate}")
        .VALUES("end_date", "#{endDate}")
        .VALUES("score", "#{score}")
        .VALUES("score_delta", "#{scoreDelta}")
        .VALUES("config", "#{config}")
        .VALUES("statistics", "#{statistics}")
        .toString();
    }
  }
  
  @InsertProvider(type = ExperimentTaskSqlProvider.class, method = "insertExperimentTask")
  int insertExperimentTask(ExperimentTaskRecord experimentTask);

  @Select("SELECT * FROM seage.experiment_tasks WHERE experiment_task_id = #{experimentTaskID}")
  @Results(id = "experimentResult", value = {
      @Result(property = "experimentTaskID", column = "experiment_task_id"),
      @Result(property = "experimentType", column = "experiment_type"),
      @Result(property = "experimentID", column = "experiment_id"),
      @Result(property = "jobID", column = "job_id"),
      @Result(property = "stageID", column = "stage_id"),
      @Result(property = "problemID", column = "problem_id"),
      @Result(property = "instanceID", column = "instance_id"),
      @Result(property = "algorithmID", column = "algorithm_id"),
      @Result(property = "configID", column = "config_id"),
      @Result(property = "startDate", column = "start_date"),
      @Result(property = "endDate", column = "end_date"),
      @Result(property = "scoreDelta", column = "score_delta"),
      @Result(property = "score", column = "score"),
      @Result(property = "config", column = "config"),
      @Result(property = "statistics", column = "statistics")
  })
  ExperimentTaskRecord getExperimentTask(UUID experimentTaskID);

  @Select("SELECT * FROM seage.experiment_tasks")
  @ResultMap("experimentTasksResult")
  List<ExperimentTaskRecord> getExperimentTasks();

  @Select("SELECT * FROM seage.experiment_tasks" +
  "where seage.experiment_tasks.problem_id=#{problemId}" +
  "AND seage.experiment_tasks.algorithm_id=#{algorithmId}" +
  "order by seage.experiment_tasks.score desc" +
  "limit #{limit}")
  @ResultMap("bestExperimentsTaskResults")
  List<ExperimentTaskRecord> getBestExperimentTaks(@Param("problemId") String problemId, @Param("algorithmId") String algorithmId, @Param("limit") int limit);
}

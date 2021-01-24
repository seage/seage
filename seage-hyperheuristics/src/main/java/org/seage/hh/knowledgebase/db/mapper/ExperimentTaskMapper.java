package org.seage.hh.knowledgebase.db.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;
import org.seage.hh.experimenter.ExperimentTask;

public interface ExperimentTaskMapper {

  class ExperimentTaskSqlProvider implements ProviderMethodResolver {
    
    public static String insertExperimentTask(ExperimentTask experimentTask) {
      String result = new SQL() {{
          INSERT_INTO("seage.experiment_tasks");
          VALUES("experiment_task_id", "#{experimentTaskID}::uuid");
          VALUES("experiment_id", "#{experimentID}::uuid");          
          VALUES("job_id", "#{jobID}");
          VALUES("stage_id", "#{stageID}");
          VALUES("problem_id", "#{problemID}");
          VALUES("instance_id", "#{instanceID}");
          VALUES("algorithm_id", "#{algorithmID}");
          VALUES("config_id", "#{configID}");
          VALUES("start_date", "#{startDate}");
          VALUES("end_date", "#{endDate}");
          VALUES("score", "#{score}");
          VALUES("config", "#{config}");
          VALUES("statistics", "#{statistics}");
        }
      }.toString();
      return result;
    }
  }
  
  @InsertProvider(type = ExperimentTaskSqlProvider.class, method = "insertExperimentTask")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int insertExperimentTask(ExperimentTask experimentTask);

  @Select("SELECT * FROM seage.experiment_tasks WHERE id = #{id}")
  @Results(id = "experimentResult", value = {
      @Result(property = "id", column = "id", id = true),
      @Result(property = "experimentType", column = "experiment_task_id"),
      @Result(property = "experimentID", column = "experiment_id"),
      @Result(property = "jobID", column = "job_id"),
      @Result(property = "stageID", column = "stage_id"),
      @Result(property = "problemID", column = "problem_id"),
      @Result(property = "instanceID", column = "instance_id"),
      @Result(property = "algorithmID", column = "algorithm_id"),
      @Result(property = "configID", column = "config_id"),
      @Result(property = "startDate", column = "start_date"),
      @Result(property = "endDate", column = "end_date"),
      @Result(property = "score", column = "score"),
      // @Result(property = "config", column = "config"),
      // @Result(property = "statistics", column = "statistics")
  })
  ExperimentTask getExperimentTask(int id);
}

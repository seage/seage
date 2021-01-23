package org.seage.hh.knowledgebase.db.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
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
    public static String insertExperiment(Experiment experiment) {
      String result = new SQL() {{
          INSERT_INTO("seage.experiments");
          VALUES("experiment_type", "#{experimentType}");
          VALUES("experiment_id", "#{experimentID}");          
          VALUES("problem_id", "#{problemID}");
          VALUES("instance_id", "#{instanceID}");
          VALUES("algorithm_id", "#{algorithmID}");
          VALUES("config", "#{config}");
          VALUES("start_date", "#{startDate}");
          VALUES("end_date", "#{endDate}");
          VALUES("hostname", "#{hostname}");
          VALUES("score", "#{score}");
        }
      }.toString();
      return result;
    }

    // public static String getUsersByName(final String name) {
    //   return new SQL() {{
    //       SELECT("*");
    //       FROM("users");
    //       if (name != null) {
    //         WHERE("name like #{value} || '%'");
    //       }
    //       ORDER_BY("id");
    //     }
    //   }.toString();
    // }
  }

  @InsertProvider(type = ExperimentSqlProvider.class, method = "insertExperiment")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int insertExperiment(Experiment experiment);


  @Select("SELECT * FROM seage.experiments WHERE id = #{id}")
  @Results(id = "experimentResult", value = {
      @Result(property = "id", column = "id", id = true),
      @Result(property = "experimentType", column = "experiment_type"),
      @Result(property = "experimentID", column = "experiment_id"),
      @Result(property = "problemID", column = "problem_id"),
      @Result(property = "instanceID", column = "instance_id"),
      @Result(property = "algorithmID", column = "algorithm_id"),
      @Result(property = "config", column = "config"),
      @Result(property = "startDate", column = "start_date"),
      @Result(property = "endDate", column = "end_date"),
      @Result(property = "hostname", column = "hostname"),
      @Result(property = "score", column = "score")
  })
  Experiment getExperiment(int id);

  @Select("SELECT * FROM seage.experiments")
  @ResultMap("experimentResult")
  List<Experiment> getExperiments();

  @Select("SELECT count(*) FROM seage.experiments")
  int getExperimentCount();

  @Update("UPDATE seage.experiments SET end_date = #{endDate} WHERE experiment_id = #{experimentId}")
  void updateEndDate(@Param("experimentId") String experimentId, @Param("endDate") Date endDate);

  @Update("UPDATE seage.experiments SET score = #{score} WHERE experiment_id = #{experimentId}")
  void updateScore(@Param("experimentId") String experimentId, @Param("score") double score);
}

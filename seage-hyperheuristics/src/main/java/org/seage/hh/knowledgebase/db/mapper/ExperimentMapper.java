package org.seage.hh.knowledgebase.db.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;
import org.seage.hh.knowledgebase.db.Experiment;

public interface ExperimentMapper {
  class ExperimentSqlProvider implements ProviderMethodResolver {
    public static String insertExperiment(Experiment experiment) {
      return new SQL() {{
          INSERT_INTO("seage.experiments");
          VALUES("experiment_id", "#{experimentID}");
          VALUES("experiment_name", "#{experimentName}");
          VALUES("problem_id", "#{problemID}");
          VALUES("instance_id", "#{instanceID}");
          VALUES("algorithm_id", "#{algorithmID}");
          VALUES("config", "#{config}");
          VALUES("start_date", "#{startDate}");
          VALUES("duration", "#{duration}");
          VALUES("hostname", "#{hostname}");
        }
      }.toString();
    }

    public static String getUsersByName(final String name) {
      return new SQL() {{
          SELECT("*");
          FROM("users");
          if (name != null) {
            WHERE("name like #{value} || '%'");
          }
          ORDER_BY("id");
        }
      }.toString();
    }
  }

  // @Insert("INSERT INTO seage.experiments(start_date, duration, experiment_type, hostname, config) VALUES(#{duration}, #{startDate}, #{experimentType}, #{hostname}, #{config})")
  @InsertProvider(type = ExperimentSqlProvider.class, method = "insertExperiment")
  @Options(useGeneratedKeys=true, keyProperty="id")
  // @SelectKey(statement="call identity()", keyProperty="id", before=false, resultType=int.class)
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

package com.aiyuns.tinkerplay.Config;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: aiYunS @Date: 2022-11-16 15:09 @Description: flyway配置类
 */
@Configuration
public class FlywayConfig {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Resource(name = "YmlDataSourceConfig")
  private YmlDataSourceConfig ymlDataSourceConfig;

  @Value("${spring.flyway.dataSourceArr}")
  private String dataSourceArr;

  @PostConstruct
  public void migrate() {
    DynamicDataSourceProperties properties = ymlDataSourceConfig.getProperties();
    Map<String, DataSourceProperty> dataSourceMap = properties.getDatasource();
    String[] environment = dataSourceArr.split(",");
    for (String key : dataSourceMap.keySet()) {
      for (String env : environment) {
        if ("master".equals(key)) {
          execute(getDataSource(dataSourceMap.get(key)));
          break;
        } else if (key.equals(env)) {
          execute(getDataSource(dataSourceMap.get(key)));
        }
      }
    }
  }

  private void execute(DataSource dataSource) {
    // 高版本的Flywayjar包的写法一
    Flyway flyway =
        Flyway.configure()
            .dataSource(dataSource)
            // sql文件名称规则："V20210625.17.30__V1.0.sql"
            .locations("db/mysql")
            .baselineOnMigrate(true)
            // 对于开发环境, 可能是多人协作开发, 很可能先 apply 了自己本地的最新 SQL 代码, 然后发现其他同事早先时候提交的 SQL 代码还没有 apply,
            // 所以 开发环境应该设置 spring.flyway.outOfOrder=true, 这样 flyway 将能加载漏掉的老版本 SQL 文件; 而生产环境应该设置
            // spring.flyway.outOfOrder=false
            .outOfOrder(true)
            .encoding("UTF-8")
            .load();

    // 高版本的Flywayjar包的写法二, ClassicConfiguration类继承了Configure类
    //        ClassicConfiguration classicConfiguration  = new ClassicConfiguration();
    //        classicConfiguration.setDataSource(dataSource);
    //        classicConfiguration.setLocationsAsStrings("db/mysql");
    //        classicConfiguration.setEncodingAsString("UTF-8");
    //        classicConfiguration.setOutOfOrder(true);
    //        Flyway flyway = new Flyway(classicConfiguration);

    // 低版本的Flywayjar包的写法
    //        flyway.setDataSource(dataSource);
    //        //
    // 设置flyway扫描sql升级脚本、java升级脚本的目录路径或包路径（表示是src/main/resources/flyway下面，前缀默认为src/main/resources，因为这个路径默认在classpath下面）
    //        flyway.setLocations("db/migration");
    //        // 设置sql脚本文件的编码
    //        flyway.setEncoding("UTF-8");
    //        flyway.setOutOfOrder(true);

    try {
      flyway.migrate();
    } catch (FlywayException e) {

      logger.error("Flyway配置第一次加载出错", e);

      try {

        flyway.repair();
        logger.info("Flyway配置修复成功");
        flyway.migrate();
        logger.info("Flyway配置重新加载成功");
      } catch (Exception e1) {
        logger.error("Flyway配置第二次加载出错", e1);
        throw e1;
      }
    }
  }

  private DataSource getDataSource(DataSourceProperty dataSourceProperty) {
    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.driverClassName(dataSourceProperty.getDriverClassName());
    dataSourceBuilder.url(dataSourceProperty.getUrl());
    dataSourceBuilder.username(dataSourceProperty.getUsername());
    dataSourceBuilder.password(dataSourceProperty.getPassword());
    return dataSourceBuilder.build();
  }
}

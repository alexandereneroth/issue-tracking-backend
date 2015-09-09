package nu.jixa.its.web.integration;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import javax.sql.DataSource;
import nu.jixa.its.web.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Application.class)
public class IntegrationTestContext {

  @Autowired
  DataSource dataSource;

  @Bean
  public DatabaseConfigBean dbUnitDatabaseConfig() {
    DatabaseConfigBean dbConfig = new com.github.springtestdbunit.bean.DatabaseConfigBean();
    dbConfig.setDatatypeFactory(new org.dbunit.ext.mysql.MySqlDataTypeFactory());
    return dbConfig;
  }

  @Bean
  public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() {
    DatabaseDataSourceConnectionFactoryBean dbConnection =
        new com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean(dataSource);
    dbConnection.setDatabaseConfig(dbUnitDatabaseConfig());
    return dbConnection;
  }
}

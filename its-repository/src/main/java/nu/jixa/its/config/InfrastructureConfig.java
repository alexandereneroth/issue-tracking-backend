package nu.jixa.its.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("nu.jixa.its.repository")
@EnableTransactionManagement
public class InfrastructureConfig
{
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory()
  {
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

    factory.setDataSource(dataSource());
    factory.setJpaVendorAdapter(jpaVendorAdapter());
    factory.setPackagesToScan("nu.jixa.its.model");
    factory.setJpaProperties(additionalProperties());

    return factory;
  }


  @Bean
  public DataSource dataSource()
  {
    HikariConfig config = new HikariConfig();

    config.setDriverClassName("com.mysql.jdbc.Driver");
    config.setJdbcUrl("jdbc:mysql://localhost:3306/its-backend");
    config.setUsername("root");
    config.setPassword("");

    return new HikariDataSource(config);
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter()
  {
    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setDatabase(Database.MYSQL);
    adapter.setGenerateDdl(true);

    return adapter;
  }

  @Bean
  JpaTransactionManager transactionManager(EntityManagerFactory factory)
  {
    return new JpaTransactionManager(factory);
  }

  Properties additionalProperties()
  {
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", "create");
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
    return properties;
  }
}

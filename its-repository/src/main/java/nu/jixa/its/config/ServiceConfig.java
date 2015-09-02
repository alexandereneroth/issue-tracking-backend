package nu.jixa.its.config;

import nu.jixa.its.service.ITSRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

  @Bean ITSRepositoryImpl serviceImpl(){
    return new ITSRepositoryImpl();
  }
}

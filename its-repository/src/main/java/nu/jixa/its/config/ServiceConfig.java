package nu.jixa.its.config;

import nu.jixa.its.service.ServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

  @Bean ServiceImpl serviceImpl(){
    return new ServiceImpl();
  }
}

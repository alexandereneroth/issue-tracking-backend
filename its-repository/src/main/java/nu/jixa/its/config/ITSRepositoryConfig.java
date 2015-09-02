package nu.jixa.its.config;

import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.ITSRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ITSRepositoryConfig {

  @Bean ITSRepository ITSRepository(){
    return new ITSRepositoryImpl();
  }
}

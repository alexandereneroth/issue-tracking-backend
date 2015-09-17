package nu.jixa.its.config;

import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.IssueITSRepository;
import nu.jixa.its.service.TeamITSRepository;
import nu.jixa.its.service.UserITSRepository;
import nu.jixa.its.service.WorkItemITSRepository;
import nu.jixa.its.service.impl.ITSRepositoryImpl;
import nu.jixa.its.service.impl.IssueITSRepositoryImpl;
import nu.jixa.its.service.impl.TeamITSRepositoryImpl;
import nu.jixa.its.service.impl.UserITSRepositoryImpl;
import nu.jixa.its.service.impl.WorkItemITSRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ITSRepositoryConfig {

  @Bean ITSRepository ITSRepository() {
    return new ITSRepositoryImpl();
  }

  @Bean UserITSRepository UserITSRepository() {
    return new UserITSRepositoryImpl();
  }

  @Bean WorkItemITSRepository WorkItemITSRepository() {
    return new WorkItemITSRepositoryImpl();
  }

  @Bean IssueITSRepository IssueITSRepository() {
    return new IssueITSRepositoryImpl();
  }

  @Bean TeamITSRepository TeamITSRepository() {
    return new TeamITSRepositoryImpl();
  }
}

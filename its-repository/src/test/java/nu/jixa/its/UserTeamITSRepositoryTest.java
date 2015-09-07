package nu.jixa.its;

import nu.jixa.its.config.ITSRepositoryConfig;
import nu.jixa.its.config.InfrastructureConfig;
import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;
import nu.jixa.its.service.ITSRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/* FUNKTIONSKRAV
  Hämta alla User som ingår i ett visst team
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class,
    ITSRepositoryConfig.class }, loader = AnnotationConfigContextLoader.class)
public class UserTeamITSRepositoryTest {

  @Autowired
  ITSRepository repository;

  private final Long userNumber = 5L;
  private final Long teamNumber = 6L;

  @Test
  public void canGetUserByTeam() {
    User user = HelperMethods.generateSimpleUser(userNumber);
    Team team = new Team(teamNumber);

    user.setTeam(team);

    repository.addTeam(team);
    repository.addUser(user);

    User userInRepoAfterAdd = repository.getUser(userNumber);

    assertNotNull(user);
    assertEquals(user, userInRepoAfterAdd);

    User userInTeam = repository.getUserByTeam(teamNumber);

    repository.deleteUser(userNumber);
  }
}


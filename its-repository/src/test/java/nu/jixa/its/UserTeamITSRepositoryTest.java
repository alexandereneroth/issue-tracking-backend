package nu.jixa.its;

import java.util.Set;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class,
    ITSRepositoryConfig.class }, loader = AnnotationConfigContextLoader.class)
public class UserTeamITSRepositoryTest {

  @Autowired
  ITSRepository repository;

  private final Long userNumber = 8L;
  private final Long userNumber2 = 7L;
  private final Long teamNumber = 6L;

  @Test
  public void canAddUserToTeam() {
    User user = HelperMethods.generateSimpleUser(userNumber);
    Team team = new Team(teamNumber);

    repository.addTeam(team);

    user.setTeam(team);

    repository.addUser(user);

    User userInRepoAfterAdd = repository.getUser(userNumber);

    assertNotNull(user);
    assertEquals(user, userInRepoAfterAdd);

    repository.deleteUser(userNumber);
    repository.deleteTeam(teamNumber);
  }

  @Test
  public void canGetUserByTeam(){
    User user = HelperMethods.generateSimpleUser(userNumber);
    User user2 = HelperMethods.generateSimpleUser(userNumber2);
    Team team = new Team(teamNumber);

    repository.addTeam(team);

    user.setTeam(team);
    user2.setTeam(team);

    repository.addUser(user);
    repository.addUser(user2);

    User userInRepoAfterAdd = repository.getUser(userNumber);

    assertNotNull(user);
    assertEquals(user, userInRepoAfterAdd);

    Iterable<User> iterableUsersInTeamInRepo = repository.getUsersByTeam(teamNumber);

    Set<User> usersInTeamInRepo = HelperMethods.toHashSet(iterableUsersInTeamInRepo);
    Set<User> usersInTeam = HelperMethods.toHashSet(new User[] { user, user2 });

    assert(HelperMethods.isEqualSet(usersInTeam, usersInTeamInRepo));

    repository.deleteUser(userNumber);
    repository.deleteUser(userNumber2);
    repository.deleteTeam(teamNumber);
  }
}


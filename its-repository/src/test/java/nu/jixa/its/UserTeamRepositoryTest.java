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
public class UserTeamRepositoryTest {

  @Autowired
  ITSRepository repository;

  private final Long userNumber = 8L;
  private final Long userNumber2 = 7L;
  private final Long teamNumber = 6L;

  @Test
  public void canAddUserToTeam() {
    User user = Util.newSimpleUser(userNumber);
    Team team = new Team(teamNumber);

    repository.addTeam(team);

    user.joinTeam(team);

    repository.addUser(user);

    User userInRepoAfterAdd = repository.getUser(userNumber);

    assertNotNull(user);
    assertEquals(user, userInRepoAfterAdd);

    repository.deleteUser(userNumber);
    repository.deleteTeam(teamNumber);
  }

  @Test
  public void testAddUserToTeamWithNumber() {
    User user = Util.newSimpleUser(userNumber);
    Team team = new Team(teamNumber);

    repository.addUser(user);
    repository.addTeam(team);

    repository.addUserToTeamWithNumber(userNumber, teamNumber);

    User userInRepoAfterAdd = repository.getUser(userNumber);

    assertNotNull(user);
    assertEquals(team.getNumber(), userInRepoAfterAdd.getTeam().getNumber());

    repository.deleteUser(userNumber);
    repository.deleteTeam(teamNumber);
  }

  @Test
  public void canGetUserByTeam() {
    User user = Util.newSimpleUser(userNumber);
    User user2 = Util.newSimpleUser(userNumber2);
    Team team = new Team(teamNumber);

    repository.addUser(user);
    repository.addUser(user2);

    repository.addTeam(team);

    user.joinTeam(team);
    user2.joinTeam(team);

    repository.updateUser(user);
    repository.updateUser(user2);

    User userInRepoAfterAdd = repository.getUser(userNumber);

    assertNotNull(user);
    assertEquals(user, userInRepoAfterAdd);

    Iterable<User> iterableUsersInTeamInRepo = repository.getUsersByTeam(teamNumber);

    Set<User> usersInTeamInRepo = Util.toHashSet(iterableUsersInTeamInRepo);
    Set<User> usersInTeam = Util.toHashSet(new User[] { user, user2 });

    assert (Util.isEqualSet(usersInTeam, usersInTeamInRepo));

    repository.deleteUser(userNumber);
    repository.deleteUser(userNumber2);
    repository.deleteTeam(teamNumber);
  }
}


package nu.jixa.its;

import java.util.Collection;
import java.util.Set;
import nu.jixa.its.config.ITSRepositoryConfig;
import nu.jixa.its.config.InfrastructureConfig;
import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.ITSRepositoryException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class,
    ITSRepositoryConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TeamRepositoryTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  ITSRepository repository;

  static final Long testTeamNumber = 7L;
  static Team testTeam;

  static final Long number1 = 42L;
  static final String username1 = "alladin";
  static final String firstname1 = "alladin2";
  static final String lastname1 = "alladin3";
  static final Long number2 = 4422L;
  static final String username2 = "arvid";
  static final String firstname2 = "arvid2";
  static final String lastname2 = "arvid3";
  static final Long number3 = 423L;
  static final String username3 = "karl";
  static final String firstname3 = "karl2";
  static final String lastname3 = "karl3";
  static User user1;
  static User user2;
  static User user3;

  @Before
  public void before() {

    // Gotcha: Must be created each time, so they contain no id. Otherwise spring thinks they have
    // already been added.
    testTeam = new Team(testTeamNumber);
    user1 = new User(number1, username1, firstname1, lastname1);
    user2 = new User(number2, username2, firstname2, lastname2);
    user3 = new User(number3, username3, firstname3, lastname3);

    repository.addTeam(testTeam);
    repository.addUser(user1);
    repository.addUser(user2);
    repository.addUser(user3);
    user1.joinTeam(testTeam);
    user2.joinTeam(testTeam);
    user3.joinTeam(testTeam);
    repository.updateUser(user1);
    repository.updateUser(user2);
    repository.updateUser(user3);

    Team teamInRepoAfterAdd = repository.getTeam(testTeamNumber);
    assertNotNull(teamInRepoAfterAdd);
    assertEquals(testTeam, teamInRepoAfterAdd);
    assert (teamInRepoAfterAdd.getUsers().contains(user1));
    assert (teamInRepoAfterAdd.getUsers().contains(user2));
    assert (teamInRepoAfterAdd.getUsers().contains(user3));
  }

  @After
  public void after() {
    repository.deleteTeam(testTeam);
    repository.deleteUser(user1.getNumber());
    repository.deleteUser(user2.getNumber());
    repository.deleteUser(user3.getNumber());
  }

  @Test
  public void canSaveGetAndDelete() {
    Long teamNumber = 1L;
    Team team = new Team(teamNumber);

    repository.addTeam(team);
    Team teamInRepoAfterAdd = repository.getTeam(teamNumber);

    assertNotNull(team);
    assertEquals(team, teamInRepoAfterAdd);

    repository.deleteTeam(teamNumber);

    expectedException.expect(ITSRepositoryException.class);
    expectedException.expectMessage("Could not find Team");
    repository.getTeam(teamNumber);
  }

  @Test
  public void canUpdate() {

    Collection<User> usersBefore = repository.getTeam(testTeamNumber).getUsers();

    assertThat(usersBefore.size(), is(3));
    assertTrue(usersBefore.contains(user1));
    assertTrue(usersBefore.contains(user2));
    assertTrue(usersBefore.contains(user3));

    Team teamToUpdate = new Team(testTeamNumber);
    user1.joinTeam(teamToUpdate);

    repository.updateTeam(teamToUpdate);

    Team teamInRepoAfterUpdate = repository.getTeam(testTeamNumber);

    assertThat(teamInRepoAfterUpdate.getUsers().size(), is(1));
    assertTrue(teamInRepoAfterUpdate.getUsers().contains(user1));

    // reset, so after() can do its job
    teamToUpdate.setNumber(testTeamNumber);
    repository.updateTeam(teamToUpdate);
  }

  @Test
  public void canGetAllTeams() {
    Team tmpTeam1 = new Team(88L);
    Team tmpTeam2 = new Team(99L);
    Team tmpTeam3 = new Team(111L);

    repository.addTeam(tmpTeam1);
    repository.addTeam(tmpTeam2);
    repository.addTeam(tmpTeam3);

    Set<Team> allTeamsInRepo = HelperMethods.toHashSet(repository.getAllTeams());
    Set<Team> allTeams =
        HelperMethods.toHashSet(new Team[] { testTeam, tmpTeam1, tmpTeam2, tmpTeam3 });

    assert (HelperMethods.isEqualSet(allTeams, allTeamsInRepo));
  }
}


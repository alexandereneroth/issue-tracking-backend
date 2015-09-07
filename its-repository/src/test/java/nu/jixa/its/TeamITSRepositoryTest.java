package nu.jixa.its;

import java.util.HashSet;
import java.util.Set;
import nu.jixa.its.config.ITSRepositoryConfig;
import nu.jixa.its.config.InfrastructureConfig;
import nu.jixa.its.model.Team;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class,
    ITSRepositoryConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TeamITSRepositoryTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  ITSRepository repository;

  final Long testTeamNumber = 7L;
  Team testTeam;

  @Before
  public void before() {
    testTeam = new Team(testTeamNumber);

    repository.addTeam(testTeam);
    Team teamInRepoAfterAdd = repository.getTeam(testTeamNumber);
    assertNotNull(testTeam);
    assertEquals(testTeam, teamInRepoAfterAdd);
  }

  @After
  public void after() {
    repository.deleteTeam(testTeam);
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

    Team teamToUpdate = repository.getTeam(testTeamNumber);
    teamToUpdate.setNumber(773L);

    repository.updateTeam(teamToUpdate);

    expectedException.expect(ITSRepositoryException.class);
    expectedException.expectMessage("Could not find Team");

    repository.getTeam(testTeamNumber);

    // reset, so after() can do its job
    teamToUpdate.setNumber(testTeamNumber);
    repository.updateTeam(teamToUpdate);
  }

  //| Hämta alla team
  @Test
  public void canGetAllTeams() {
    Team tmpTeam1 = new Team(88L);
    Team tmpTeam2 = new Team(99L);
    Team tmpTeam3 = new Team(111L);

    repository.addTeam(tmpTeam1);
    repository.addTeam(tmpTeam2);
    repository.addTeam(tmpTeam3);

    Set<Team> allTeamsInRepo = HelperMethods.toHashSet(repository.getAllTeams());

    Set<Team> allTeams = new HashSet<>();
    allTeams.add(testTeam);
    allTeams.add(tmpTeam1);
    allTeams.add(tmpTeam2);
    allTeams.add(tmpTeam3);


    assert(HelperMethods.isEqualSet(allTeams,allTeamsInRepo));
  }
  //| Lägga till en User till ett team
}


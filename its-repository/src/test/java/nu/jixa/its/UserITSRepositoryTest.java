package nu.jixa.its;

import com.sun.istack.internal.NotNull;
import java.util.Collection;
import nu.jixa.its.config.ITSRepositoryConfig;
import nu.jixa.its.config.InfrastructureConfig;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class,
    ITSRepositoryConfig.class }, loader = AnnotationConfigContextLoader.class)
//@DatabaseSetup("userData.xml")TODO try to use this when the other stuff works
public class UserITSRepositoryTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  ITSRepository repository;

  final Long testUserNumber = 10L;
  final String testUserUsername = "Username";
  final String testUserFirstname = "John";
  final String testUserLastname = "Doe";
  User testUser;

  final Long testTeamNumber = 256L;
  Team testTeam;

  final Long testWorkItemNumber = 1024L;
  final Status testWorkItemStatus = Status.DONE;
  WorkItem testWorkItem;


  @Before
  public void before() {
    testUser = new User(testUserNumber,testUserUsername, testUserFirstname,testUserLastname);
    testTeam = new Team(testTeamNumber);
    testWorkItem = new WorkItem(testWorkItemNumber, testWorkItemStatus);

    repository.addTeam(testTeam);
    repository.addUser(testUser);
    repository.addWorkItem(testWorkItem);

    testUser.joinTeam(testTeam);

    repository.updateUser(testUser);

    testUser.addWorkItem(testWorkItem);

    repository.updateUser(testUser);

    User userInRepoAfterAdd = repository.getUser(testUserNumber);
    assertNotNull(testUser);
    assertEquals(testUser, userInRepoAfterAdd);
  }

  @After
  public void after() {
    repository.deleteTeam(testTeam);
    repository.removeWorkItem(testWorkItemNumber);
    repository.deleteUser(testUserNumber);
  }

  @Test
  public void canSaveGetAndDelete() {
    Long userNumber = 1L;
    User user = new User(userNumber, "Jixa", "Andromeda", "Wasilewskji");

    repository.addUser(user);
    User userInRepoAfterAdd = repository.getUser(userNumber);

    assertNotNull(user);
    assertEquals(user, userInRepoAfterAdd);

    repository.deleteUser(userNumber);

    expectedException.expect(ITSRepositoryException.class);
    expectedException.expectMessage("Could not get User");
    repository.getUser(userNumber);
  }

  @Test
  public void canUpdate() {

    //update names
    User userToUpdate = new User(testUserNumber,"wa","ta","shi");
    repository.updateUser(userToUpdate);

    User testUserAfterUpdate = repository.getUser(testUserNumber);
    assertEquals(testUserAfterUpdate.getUsername(), "wa");
    assertEquals(testUserAfterUpdate.getFirstname(), "ta");
    assertEquals(testUserAfterUpdate.getLastname(), "shi");

    //update team
    User userWithNewTeam = new User(testUserNumber, "wa", "ta", "shi");
    Long newTeamNumber = 52L;
    Team newTeam = new Team(newTeamNumber);
    repository.addTeam(newTeam);

    userWithNewTeam.joinTeam(newTeam);
    repository.updateUser(userWithNewTeam);

    User userInRepoWithNewTeam = repository.getUser(testUserNumber);
    Team teamInRepoWithNewUser = repository.getTeam(newTeamNumber);
    Team teamInRepoWithRemovedUser = repository.getTeam(testTeamNumber);

    assertThat(userInRepoWithNewTeam.getTeam(), is(equalTo(newTeam)));

    assertThat(teamInRepoWithNewUser.getUsers().size(),is(1));
    assertTrue(teamInRepoWithNewUser.getUsers().contains(userWithNewTeam));

    assertThat(teamInRepoWithRemovedUser.getUsers().size(), is(0));
    assertFalse(teamInRepoWithRemovedUser.getUsers().contains(userWithNewTeam));

    repository.deleteTeam(newTeam);

    //update workItems
    User userWithNewWorkItem = new User(testUserNumber, "wa", "ta", "shi");
    Long newWorkItemNumber = 42L;

    WorkItem newWorkItem = new WorkItem(newWorkItemNumber, Status.DONE);

    repository.addWorkItem(newWorkItem);

    userWithNewWorkItem.addWorkItem(newWorkItem);

    repository.updateUser(userWithNewWorkItem);

    User userInRepoWithNewWorkItem = repository.getUser(testUserNumber);
    WorkItem workItemInRepoWithNewUser = repository.getWorkItem(newWorkItemNumber);
    WorkItem workItemInRepoWithRemovedUser = repository.getWorkItem(testWorkItemNumber);

    assertThat(userInRepoWithNewWorkItem.getWorkItems().size(), is(1));
    assertTrue(userInRepoWithNewWorkItem.getWorkItems().contains(newWorkItem));

    assertThat(workItemInRepoWithNewUser.getUsers().size(), is(1));
    assertTrue(workItemInRepoWithNewUser.getUsers().contains(userWithNewWorkItem));

    assertThat(workItemInRepoWithRemovedUser.getUsers().size(), is(0));
    assertFalse(workItemInRepoWithRemovedUser.getUsers().contains(userWithNewWorkItem));

    repository.removeWorkItem(newWorkItemNumber);
  }

  @Test
  public void canGetByNameLike() {
    Collection<User> usersWithName = repository.getUsersByNameLike(testUserUsername);

    assertThat(usersWithName.size(), is(1));
    for(User user : usersWithName)
    {
      assertEquals(user, testUser);
    }

    usersWithName = repository.getUsersByNameLike(testUserFirstname);

    assertThat(usersWithName.size(), is(1));
    for(User user : usersWithName)
    {
      assertEquals(user, testUser);
    }

    usersWithName = repository.getUsersByNameLike(testUserLastname);

    assertThat(usersWithName.size(), is(1));
    for(User user : usersWithName)
    {
      assertEquals(user, testUser);
    }
  }

  @Test
  public void addShouldThrowExceptionOnExistingNumber()
  {
    User userWithExistingNumber = new User(testUserNumber,"hej","pa","dig");

    expectedException.expect(ITSRepositoryException.class);
    expectedException.expectMessage("Could not add user");
    repository.addUser(userWithExistingNumber);
  }

  private User generateSimpleUser(@NotNull final Long number) {
    return new User(number, "account" + number, "firstname" + number, "lastname" + number);
  }
}
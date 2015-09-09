package nu.jixa.its;

import com.sun.istack.internal.NotNull;
import java.util.Collection;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class,
    ITSRepositoryConfig.class }, loader = AnnotationConfigContextLoader.class)
//@DatabaseSetup("userData.xml")TODO try to use this when the other stuff works
public class UserITSRepositoryTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  ITSRepository repository;

  final Long testUserId = 10L;
  final String testUserUsername = "Username";
  final String testUserFirstname = "John";
  final String testUserLastname = "Doe";
  User testUser;
  final Long testTeamId = 256L;
  Team testTeam;


  @Before
  public void before() {
    testUser = new User(testUserId,testUserUsername, testUserFirstname,testUserLastname);
    testTeam = new Team(testTeamId);


    repository.addUser(testUser);
    User userInRepoAfterAdd = repository.getUser(testUserId);
    assertNotNull(testUser);
    assertEquals(testUser, userInRepoAfterAdd);
  }

  @After
  public void after() {
    repository.deleteUser(testUserId);
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

    User userToUpdate = new User(testUserId,"wa","ta","shi");
    repository.updateUser(userToUpdate);

    User testUserAfterUpdate = repository.getUser(testUserId);
    assertEquals(testUserAfterUpdate.getUsername(), "wa");
    assertEquals(testUserAfterUpdate.getFirstname(), "ta");
    assertEquals(testUserAfterUpdate.getLastname(), "shi");


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
    User userWithExistingNumber = new User(testUserId,"hej","pa","dig");

    expectedException.expect(ITSRepositoryException.class);
    expectedException.expectMessage("Could not add user");
    repository.addUser(userWithExistingNumber);
  }

  private User generateSimpleUser(@NotNull final Long number) {
    return new User(number, "account" + number, "firstname" + number, "lastname" + number);
  }
}
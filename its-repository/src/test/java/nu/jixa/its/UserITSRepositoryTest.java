package nu.jixa.its;

import com.sun.istack.internal.NotNull;
import nu.jixa.its.config.ITSRepositoryConfig;
import nu.jixa.its.config.InfrastructureConfig;
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

  @Before
  public void before() {
    User user = new User(testUserId,testUserUsername, testUserFirstname,testUserLastname);

    repository.addUser(user);
    User userInRepoAfterAdd = repository.getUser(testUserId);
    assertNotNull(user);
    assertEquals(user, userInRepoAfterAdd);
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

    User userToUpdate = repository.getUser(testUserId);
    final String updatedFirstname = "UPDATED_FIRSTNAME";
    userToUpdate.setFirstname(updatedFirstname);

    repository.updateUser(userToUpdate);

    User testUserAfterUpdate = repository.getUser(testUserId);
    assertEquals(testUserAfterUpdate.getFirstname(), updatedFirstname);
  }

  @Test
  public void canGetByNameLike() {

  }

  /*@Test
  public void canAddWorkItemTo() {
    //TODO implement
  }*/

  private User generateSimpleUser(@NotNull final Long number) {
    return new User(number, "account" + number, "firstname" + number, "lastname" + number);
  }
}

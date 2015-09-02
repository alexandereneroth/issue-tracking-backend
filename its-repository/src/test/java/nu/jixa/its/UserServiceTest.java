package nu.jixa.its;

import com.sun.istack.internal.NotNull;
import nu.jixa.its.config.InfrastructureConfig;
import nu.jixa.its.config.ServiceConfig;
import nu.jixa.its.model.User;
import nu.jixa.its.service.Service;
import nu.jixa.its.service.ServiceException;
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
    ServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
//@DatabaseSetup("userData.xml")TODO try to use this when the other stuff works
public class UserServiceTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  Service service;

  final Long testUserId = 10L;

  @Before
  public void before() {
    User user = generateSimpleUser(testUserId);

    service.addUser(user);
    User userInRepoAfterAdd = service.getUser(testUserId);
    assertNotNull(user);
    assertEquals(user, userInRepoAfterAdd);
  }

  @After
  public void after() {
    service.deleteUser(testUserId);
  }

  @Test
  public void canSaveGetAndDelete() {
    Long userNumber = 1L;
    User user = new User(userNumber, "Jixa", "Andromeda", "Wasilewskji");

    service.addUser(user);
    User userInRepoAfterAdd = service.getUser(userNumber);

    assertNotNull(user);
    assertEquals(user, userInRepoAfterAdd);

    service.deleteUser(userNumber);

    expectedException.expect(ServiceException.class);
    expectedException.expectMessage("Could not get User");
    service.getUser(userNumber);
  }

  @Test
  public void canUpdate() {

    User userToUpdate = service.getUser(testUserId);
    final String updatedFirstname = "UPDATED_FIRSTNAME";
    userToUpdate.setFirstname(updatedFirstname);

    service.updateUser(userToUpdate);

    User testUserAfterUpdate = service.getUser(testUserId);
    assertEquals(testUserAfterUpdate.getFirstname(), updatedFirstname);
  }

  @Test
  public void canGetById() {
    //TODO implement
  }

  @Test
  public void canGetByTeam() {
    //TODO implement
  }

  @Test
  public void canGetByNameLike() {
    //TODO implement
  }

  @Test
  public void canAddWorkItemTo() {
    //TODO implement
  }

  private User generateSimpleUser(@NotNull final Long number) {
    return new User(number, "account" + number, "firstname" + number, "lastname" + number);
  }
}

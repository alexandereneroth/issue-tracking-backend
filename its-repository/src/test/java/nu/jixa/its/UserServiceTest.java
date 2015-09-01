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
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class,
    ServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
//@DatabaseSetup("userData.xml")TODO try to use this when the other stuff works
public class UserServiceTest {

  @Rule
  ExpectedException expectedException = ExpectedException.none();

  @Autowired
  Service service;

  Long testUserId = 10L;

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
  public void canGet() {
    User testUser = service.getUser(testUserId);
    assertEquals(testUser.getId(), testUserId);
  }

  @Test
  public void canSaveAndRemove() {
    Long userId = 1L;
    User user = new User(userId, "Jixa", "Andromeda", "Wasilewskji");

    service.addUser(user);
    User userInRepoAfterAdd = service.getUser(userId);
    assertNotNull(user);
    assertEquals(user, userInRepoAfterAdd);

    service.deleteUser(userId);

    expectedException.expect(ServiceException.class);
    service.getUser(userId);
  }

  @Test
  public void canUpdate() {
    final String updatedFirstname = "UPDATED_FIRSTNAME";

    User testUserMod = generateSimpleUser(testUserId);
    testUserMod.setFirstname(updatedFirstname);

    service.updateUser(testUserMod);
    User testUserAfterUpdate = service.getUser(testUserId);
    assertEquals(testUserAfterUpdate.getFirstname(), updatedFirstname);
  }

  @Test
  public void canGetById() {
    fail();
  }

  @Test
  public void canGetByTeam() {
    fail();
  }

  @Test
  public void canGetByNameLike() {
    fail();
  }

  @Test
  public void canAddWorkItemTo() {
    fail();
  }

  private User generateSimpleUser(@NotNull final Long id) {
    return new User(id, "account" + id, "firstname" + id, "lastname" + id);
  }
}

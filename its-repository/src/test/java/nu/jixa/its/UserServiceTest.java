package nu.jixa.its;

import nu.jixa.its.config.InfrastructureConfig;
import nu.jixa.its.config.ServiceConfig;
import nu.jixa.its.service.Service;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class,
    ServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
public class UserServiceTest {

  @Autowired
  Service service;

  @Before
  public void before() {

    fail();
  }

  @After
  public void after() {
    fail();
  }

  @Test
  public void canSaveAndRemove() {

  }

  @Test
  public void canUpdate(){
    fail();
  }

  @Test
  public void canGet(){
    fail();
  }

  @Test
  public void canGetById(){
    fail();
  }

  @Test
  public void canGetByTeam(){
    fail();
  }

  @Test
  public void canGetByNameLike(){
    fail();
  }

  @Test
  public void canAddWorkItemTo(){
    fail();
  }
}

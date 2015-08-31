package nu.jixa.its;

import nu.jixa.its.config.InfrastructureConfig;
import nu.jixa.its.config.ServiceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class,
    ServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
public class UserRepositoryTest {

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
    fail();
  }
}

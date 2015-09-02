package nu.jixa.its;

import java.util.HashMap;
import nu.jixa.its.config.InfrastructureConfig;
import nu.jixa.its.config.ITSRepositoryConfig;
import nu.jixa.its.model.ModelUtil;
import nu.jixa.its.model.RepositoryModelException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class,
    ITSRepositoryConfig.class }, loader = AnnotationConfigContextLoader.class)
public class ModelUtilTest {
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void throwExceptionIfArgIsNull_ThrowsException() {

    expectedException.expect(RepositoryModelException.class);
    expectedException.expectMessage("Null value not allowed");
    expectedException.expectMessage("number");
    ModelUtil.throwExceptionIfArgIsNull(null, "number");


    expectedException.expect(RepositoryModelException.class);
    expectedException.expectMessage("Null value not allowed");
    expectedException.expectMessage("username");

    HashMap<Object, String> argsWithNames = new HashMap<>();
    argsWithNames.put(234L, "number");
    argsWithNames.put(null, "username");
    argsWithNames.put("hans", "firstname");
    argsWithNames.put("olov", "lastname");

    ModelUtil.throwExceptionIfArgIsNull(argsWithNames);
  }
}

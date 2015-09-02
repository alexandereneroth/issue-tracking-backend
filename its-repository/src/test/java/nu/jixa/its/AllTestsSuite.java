package nu.jixa.its;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ModelUtilTest.class,
    UserITSRepositoryTest.class
})
public class AllTestsSuite {
}

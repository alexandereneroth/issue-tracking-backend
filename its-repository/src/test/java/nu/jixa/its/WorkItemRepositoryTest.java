package nu.jixa.its;

import com.sun.istack.internal.NotNull;
import nu.jixa.its.config.ITSRepositoryConfig;
import nu.jixa.its.config.InfrastructureConfig;
import nu.jixa.its.model.Issue;
import nu.jixa.its.model.Status;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class,
    ITSRepositoryConfig.class }, loader = AnnotationConfigContextLoader.class)
public class WorkItemRepositoryTest {

  final Long testWorkitemNr = 10L;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  ITSRepository repository;

  @Before
  public void before() {

    WorkItem item = generateSimpleWorkItem(testWorkitemNr);
    repository.addWorkItem(item);
    WorkItem workItemInRepoAfterAdd = repository.findByNumber(item.getNumber());
    assertNotNull(item);
    assertEquals(item, workItemInRepoAfterAdd);
  }

  @After
  public void after() {
    WorkItem workItemInRepoAfterAdd = repository.findByNumber(testWorkitemNr);
    assertNotNull(workItemInRepoAfterAdd);
    repository.removeWorkItem(testWorkitemNr);
  }

  @Test
  public void testSaveAndDelete() {
    Long id = 999L;
    WorkItem item = generateSimpleWorkItem(id);
    repository.addWorkItem(item);
    WorkItem workItemInRepoAfterAdd = repository.findByNumber(item.getNumber());

    assertNotNull(item);
    assertEquals(item, workItemInRepoAfterAdd);

    repository.removeWorkItem(workItemInRepoAfterAdd.getNumber());

    expectedException.expect(ITSRepositoryException.class);
    expectedException.expectMessage("Could not find workItem");
    repository.getWorkItem(workItemInRepoAfterAdd.getId());
  }

  private WorkItem generateSimpleWorkItem(@NotNull final Long number) {
    Status status = Status.ON_BACKLOG;
    return new WorkItem(number, status);
  }

}

package nu.jixa.its;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import nu.jixa.its.config.ITSRepositoryConfig;
import nu.jixa.its.config.InfrastructureConfig;
import nu.jixa.its.model.Issue;
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

    WorkItem item = HelperMethods.generateSimpleWorkItem(testWorkitemNr);
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
    WorkItem item = HelperMethods.generateSimpleWorkItem(id);
    repository.addWorkItem(item);
    WorkItem workItemInRepoAfterAdd = repository.findByNumber(item.getNumber());

    assertNotNull(item);
    assertEquals(item, workItemInRepoAfterAdd);

    repository.removeWorkItem(workItemInRepoAfterAdd.getNumber());

    expectedException.expect(ITSRepositoryException.class);
    expectedException.expectMessage("Could not find workItem");
    repository.getWorkItemById(workItemInRepoAfterAdd.getId());
  }

  @Test
  public void testCanUpdateWorkItem(){

    //WorkItem item = repository.findByNumber();
    Issue issue = new Issue(14L);
    Collection<User> users = new ArrayList();

    users.add(new User(8L, "Simba", "Ba", "Sim"));
    WorkItem itemInRepository = repository.findByNumber(testWorkitemNr);

    itemInRepository.setStatus(Status.IN_PROGRESS);
    itemInRepository.setDescription("updated item");
    itemInRepository.setIssue(issue);
    itemInRepository.setUsers(users);

    WorkItem updatedItem = repository.updateWorkItem(itemInRepository);
    Set<User>usersInRepoSet = HelperMethods.toHashSet(itemInRepository.getUsers());
    Set<User>usersAfterUpdateSet = HelperMethods.toHashSet(updatedItem.getUsers());

    assertNotNull(updatedItem);
    assert(HelperMethods.isEqualSet(usersInRepoSet, usersAfterUpdateSet));
    assertEquals(itemInRepository, updatedItem);
  }

  @Test
  public void testCanFindByStatus(){
    ArrayList<WorkItem> items = (ArrayList)HelperMethods.generateComplexWorkItems();
    addWorkItemsToRepository(items);

    ArrayList<WorkItem> itemsFromRepository = (ArrayList)repository.getWorkItemsByStatus(Status.DONE);
    Set<WorkItem>itemsByStatusSet = HelperMethods.toHashSet(items);
    Set<WorkItem>itemsFromRepositorySet = HelperMethods.toHashSet(itemsFromRepository);

    assert(HelperMethods.isEqualSet(itemsByStatusSet, itemsFromRepositorySet));
  }

  @Test
  public void testCanSetWorkItemStatus(){
    Status status = Status.ON_BACKLOG;
    repository.setWorkItemStatus(testWorkitemNr, status);
    WorkItem workItemFromRepository = repository.getWorkItemById(testWorkitemNr);

    assertNotNull(workItemFromRepository);
    assertEquals(workItemFromRepository.getStatus(), status);
  }
  @Test
  public void testCanFindByUser(){

    ArrayList<WorkItem> items = (ArrayList)HelperMethods.generateComplexWorkItems();
    addWorkItemsToRepository(items);

    //repository.getWorkItemsByUser();
  }
  @Test
  public void testCanFindByIssue(){
    //repository.getWorkItemsByTeam();
  }
  @Test
  public void testCanFindByTeam(){
    //repository.getWorkItemsByTeam();
  }
  private void addWorkItemsToRepository(ArrayList<WorkItem> workItems){
    for(WorkItem item: workItems){
      repository.addWorkItem(item);
      assertNotNull(repository.findByNumber(item.getNumber()));
    }
  }
}

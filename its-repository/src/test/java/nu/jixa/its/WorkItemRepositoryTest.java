package nu.jixa.its;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import nu.jixa.its.config.ITSRepositoryConfig;
import nu.jixa.its.config.InfrastructureConfig;
import nu.jixa.its.model.Issue;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.exception.ITSRepositoryException;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class,
    ITSRepositoryConfig.class }, loader = AnnotationConfigContextLoader.class)
public class WorkItemRepositoryTest {

  final Long testWorkitemNr = 100L;
  ArrayList<WorkItem> items;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  ITSRepository repository;

  @Before
  public void before() {
    addUsersToRepository((ArrayList) HelperMethods.generate3Users());
    WorkItem item = HelperMethods.generateSimpleWorkItem(testWorkitemNr);
    repository.addWorkItem(item);
    WorkItem workItemInRepoAfterAdd = repository.getWorkItem(item.getNumber());
    assertNotNull(item);
    assertEquals(item, workItemInRepoAfterAdd);
    items = (ArrayList) HelperMethods.generateComplexWorkItems();
    addWorkItemsToRepository(items);
  }

  @After
  public void after() {
    WorkItem workItemInRepoAfterAdd = repository.getWorkItem(testWorkitemNr);
    assertNotNull(workItemInRepoAfterAdd);
    repository.removeWorkItem(testWorkitemNr);
    deleteWorkItemsFromRepository(items);
    deleteUsersFromRepository((ArrayList) HelperMethods.generate3Users());
  }

  @Test
  public void testSaveAndDelete() {
    Long id = 999L;
    WorkItem item = HelperMethods.generateSimpleWorkItem(id);
    repository.addWorkItem(item);
    WorkItem workItemInRepoAfterAdd = repository.getWorkItem(item.getNumber());

    assertNotNull(item);
    assertEquals(item, workItemInRepoAfterAdd);

    repository.removeWorkItem(workItemInRepoAfterAdd.getNumber());

    expectedException.expect(ITSRepositoryException.class);
    expectedException.expectMessage("Could not find WorkItem");
    repository.getWorkItem(workItemInRepoAfterAdd.getNumber());
  }

  @Test
  public void testCanUpdateWorkItem() {

    Issue issue = new Issue(14L);
    Collection<User> users = new ArrayList();

    User userFromRepository = repository.getUser(HelperMethods.USER_NUMBER);
    users.add(userFromRepository);
    WorkItem itemInRepository = repository.getWorkItem(testWorkitemNr);

    itemInRepository.setStatus(Status.IN_PROGRESS);
    itemInRepository.setDescription("updated item");
    itemInRepository.setIssue(issue);
    itemInRepository.setUsers(users);
    WorkItem updatedItem = repository.updateWorkItem(itemInRepository);
    Set<User> usersInRepoSet = HelperMethods.toHashSet(itemInRepository.getUsers());
    Set<User> usersAfterUpdateSet = HelperMethods.toHashSet(updatedItem.getUsers());

    assertNotNull(updatedItem);
    assert (HelperMethods.isEqualSet(usersInRepoSet, usersAfterUpdateSet));
    assertEquals(itemInRepository, updatedItem);
  }

  @Test
  public void testCanFindByStatus() {
    ArrayList<WorkItem> itemsFromRepository =
        (ArrayList) repository.getWorkItemsByStatus(Status.DONE);
    Set<WorkItem> itemsByStatusSet = HelperMethods.toHashSet(items);
    Set<WorkItem> itemsFromRepositorySet = HelperMethods.toHashSet(itemsFromRepository);

    assert (HelperMethods.isEqualSet(itemsByStatusSet, itemsFromRepositorySet));
  }

  @Test
  public void testCanSetWorkItemStatus() {
    Status status = Status.ON_BACKLOG;
    repository.setWorkItemStatus(testWorkitemNr, status);
    WorkItem workItemFromRepository = repository.getWorkItem(testWorkitemNr);

    assertNotNull(workItemFromRepository);
    assertEquals(workItemFromRepository.getStatus(), status);
  }

  @Test
  public void testCanFindByUser() {

    User user = repository.getUser(HelperMethods.USER_NUMBER);
    ArrayList<WorkItem> workItemList = HelperMethods.workItems3List;
    for (WorkItem workItem : workItemList) {
      repository.addWorkItemToUser(user.getNumber(), workItem.getNumber());
    }
    ArrayList<WorkItem> workItemsByUser =
        (ArrayList) repository.getWorkItemsByUser(HelperMethods.USER_NUMBER);

    assertNotNull(workItemsByUser);
    assertEquals(workItemsByUser.size(), workItemList.size());
  }

  @Test
  public void testCanFindWithIssue() {

    Collection<WorkItem> workItem = repository.getWorkItemsWithIssue();
    assertThat(workItem.size(), is(3));
    //TODO test more
  }

  @Test
  public void testCanFindByTeam() {

    User user = repository.getUser(HelperMethods.USER_NUMBER);
    Team team = new Team(222L);
    repository.addTeam(team);
    try {
      user.joinTeam(team);
      repository.updateUser(user);
      assertNotNull(repository.getTeam(team.getNumber()));
      WorkItem workItem = repository.getWorkItem(HelperMethods.WORKITEMNUMBER);

      ArrayList<User> users = (ArrayList) repository.getUsersByTeam(222L);
      assertNotNull(users);
      assertEquals(users.size(), 1);

      repository.addWorkItemToUser(user.getNumber(), workItem.getNumber());
      ArrayList<Team> teamsFromRepository =
          (ArrayList) repository.getWorkItemsByTeam(team.getNumber());

      assertNotNull(teamsFromRepository);
      assertEquals(teamsFromRepository.get(0), workItem);
    } finally {
      repository.deleteTeam(team);
    }
  }

  @Test
  public void canUpdateIssue() {

    WorkItem workItemFromRepo = repository.getWorkItem(HelperMethods.WORKITEMNUMBER);
    Long issueNumber = 2005L;
    String issueStringBeforeUpdate = "BEFORE UPDATE";
    String issueStringAfterUpdate = "AFTER UPDATE";

    Issue issue = new Issue(issueNumber);
    issue.setString(issueStringBeforeUpdate);
    Issue updatedIssue = new Issue(issueNumber);
    updatedIssue.setString(issueStringAfterUpdate);

    workItemFromRepo.setIssue(issue);
    WorkItem workItemWithBeforeIssue = repository.updateWorkItem(workItemFromRepo);

    assertNotNull(workItemWithBeforeIssue);
    assertEquals(workItemWithBeforeIssue.getIssue().getString(), issueStringBeforeUpdate);

    issue.setString(issueStringAfterUpdate);
    workItemFromRepo.setIssue(issue);
    WorkItem workItemWithIssueAfterUpdate = repository.updateWorkItem(workItemFromRepo);

    assertNotNull(workItemWithIssueAfterUpdate);
    assertThat(workItemWithIssueAfterUpdate.getIssue().getString(),
        is(equalTo(issueStringAfterUpdate)));
  }

  private void deleteWorkItemsFromRepository(ArrayList<WorkItem> workItems) {
    for (WorkItem item : workItems) {
      repository.removeWorkItem(item.getNumber());
    }
  }

  private void addWorkItemsToRepository(ArrayList<WorkItem> workItems) {
    for (WorkItem item : workItems) {
      repository.addWorkItem(item);
      assertNotNull(repository.getWorkItem(item.getNumber()));
    }
  }

  private void addUsersToRepository(ArrayList<User> users) {
    for (User user : users) {
      repository.addUser(user);
      //assertNotNull(userRepository.getWorkItem(user.getNumber()));
    }
  }

  private void deleteUsersFromRepository(ArrayList<User> users) {
    for (User user : users) {
      repository.deleteUser(user.getNumber());
    }
  }
}

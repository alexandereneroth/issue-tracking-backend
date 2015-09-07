package nu.jixa.its.service;

import java.util.Collection;
import nu.jixa.its.model.Issue;
import nu.jixa.its.model.ModelUtil;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.repository.IssueRepository;
import nu.jixa.its.repository.RepositoryUtil;
import nu.jixa.its.repository.TeamRepository;
import nu.jixa.its.repository.UserRepository;
import nu.jixa.its.repository.WorkItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gina on 2015-09-01.
 */
public class ITSRepositoryImpl implements ITSRepository {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private WorkItemRepository workItemRepository;

  @Autowired
  private IssueRepository issueRepository;

  @Autowired
  private TeamRepository teamRepository;

  @Override public WorkItem updateWorkItem(WorkItem updatedWorkItem) {
    return null;
  }
  @Transactional
  @Override public WorkItem addWorkItem(WorkItem workItem) {
    //issueRepository.save(workItem.getIssue());
    return workItemRepository.save(workItem);
  }

  @Transactional
  @Override public WorkItem removeWorkItem(Long workItemNr) {
    WorkItem deleteItem = findByNumber(workItemNr);
    workItemRepository.delete(deleteItem);
    return deleteItem;
  }
  @Transactional
  @Override public WorkItem getWorkItemById(Long workItemId) {
    WorkItem workItemInDB = workItemRepository.findByNumber(workItemId);
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(workItemInDB,
        "Could not find workItem: No item with nr " + workItemId);
    return workItemRepository.findByNumber(workItemId);
  }
  @Transactional
  @Override public void setWorkItemStatus(Long workItemId, Status status) {
    WorkItem item = workItemRepository.findByNumber(workItemId);
    item.setStatus(status);
    workItemRepository.save(item);
  }

  @Override public Collection<WorkItem> getWorkItemsByStatus(Status status) {
    return workItemRepository.findByStatus(status);
  }

  @Override public Collection<WorkItem> getWorkItemsByTeam(Long teamId) {

    //userRepository.selectUserByTeamId(teamId);
    //return workItemRepository.getWorkItemsByTeam(teamId);
    return null;
  }

  @Override public Collection<WorkItem> getWorkItemsByUser(Long userId) {
    return workItemRepository.findByUsersId(userId);
  }

  @Override public Collection<WorkItem> getWorkItemsByIssue(Long issueId) {
    return workItemRepository.findByIssueId(issueId);
  }

  @Override public Collection<WorkItem> getWorkItemByDescriptionLike(String descriptionLike) {
    return workItemRepository.findWorkItemByDescLike(descriptionLike);
  }

  @Override public WorkItem addIssueToWorkItem(Long workItemId, Long IssueId) {
    return null;
  }

  @Transactional
  @Override public User addUser(User user) {
    workItemRepository.save(user.getWorkItems());
    return userRepository.save(user);
  }

  @Transactional
  @Override public User updateUser(User user) {
    workItemRepository.save(user.getWorkItems());
    return userRepository.save(user);
  }

  @Override public User deleteUser(Long userId) {
    User deletedUser = userRepository.findByNumber(userId);
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(deletedUser,"Could not delete User: No user with id " + userId );
    userRepository.delete(deletedUser);
    return deletedUser;
  }

  @Override public User getUser(Long userId) {
    User gotUser = userRepository.findByNumber(userId);
    if (gotUser == null) {
      throw new ITSRepositoryException("Could not get User: No user with id " + userId);
    }
    return gotUser;
  }

  @Override public User getUserByTeam(Long teamId) {
    return null;
  }

  @Override public Collection<User> getUsersByNameLike(String nameLike) {
    return null;
  }

  @Override public void addWorkItemToUser(Long userId, WorkItem workItem) {

  }

  @Override public Team addTeam(Team team) {
    return null;
  }

  @Override public Team updateTeam(Team team) {
    return null;
  }

  @Override public Team removeTeamWithId(Long teamId) {
    return null;
  }

  @Override public Collection<Team> getAllTeams() {
    return null;
  }

  @Override public Team addUserToTeamWithId(Long teamId, Long userId) {
    return null;
  }

  @Override public Issue addIssue(Issue issue) {
    return null;
  }

  @Override public Issue removeIssue(Long issueId) {
    return null;
  }

  @Override public Issue updateIssue(Issue issue) {
    return null;
  }
  @Override public WorkItem findByNumber(Long workItemNr) {
    WorkItem item = workItemRepository.findByNumber(workItemNr);
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(item,"Could not find User: No user with id" + workItemNr );
    return item;
  }
}

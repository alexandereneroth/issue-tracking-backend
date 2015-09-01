package nu.jixa.its.service;

import java.util.Collection;
import nu.jixa.its.model.Issue;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gina on 2015-09-01.
 */
public class ServiceImpl implements Service {

  @Autowired
  private UserRepository userRepository;

  @Override public WorkItem addWorkItem(WorkItem workItem) {
    return null;
  }

  @Override public WorkItem removeWorkItem(Long workItemId) {
    return null;
  }

  @Override public void setWorkItemStatus(Long workItemId, Status status) {

  }

  @Override public Collection<WorkItem> getWorkItemsByStatus(Status status) {
    return null;
  }

  @Override public Collection<WorkItem> getWorkItemsByTeam(Long teamId) {
    return null;
  }

  @Override public Collection<WorkItem> getWorkItemsByUser(Long userId) {
    return null;
  }

  @Override public Collection<WorkItem> getWorkItemsByIssue(Long issueId) {
    return null;
  }

  @Override public Collection<WorkItem> getWorkItemByDescriptionLike(String descriptionLike) {
    return null;
  }

  @Override public WorkItem addIssueToWorkItem(Long workItemId, Long IssueId) {
    return null;
  }

  @Transactional
  @Override public User addUser(User user) {
    return userRepository.save(user);
  }

  @Override public User updateUser(User user) {
    return null;
  }

  @Override public User deleteUser(Long userId) {
    return null;
  }

  @Override public User getUser(Long userId) {
    return null;
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
}

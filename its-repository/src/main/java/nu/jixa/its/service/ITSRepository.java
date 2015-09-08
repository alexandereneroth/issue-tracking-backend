package nu.jixa.its.service;

import java.util.Collection;
import nu.jixa.its.model.Issue;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;

public interface ITSRepository {

  // WorkItem
  WorkItem addWorkItem(WorkItem workItem);

  WorkItem removeWorkItem(Long workItemId);

  void setWorkItemStatus(Long workItemId, Status status);

  Collection<WorkItem> getWorkItemsByStatus(Status status);

  Collection<WorkItem> getWorkItemsByTeam(Long teamId);

  Collection<WorkItem> getWorkItemsByUser(Long userId);

  Collection<WorkItem> getWorkItemsByIssue(Long issueId);

  Collection<WorkItem> getWorkItemByDescriptionLike(String descriptionLike);

  WorkItem addIssueToWorkItem(Long workItemId, Long IssueId);

  WorkItem getWorkItemById(Long workItemId);

  // User
  User addUser(User user);

  User updateUser(User user);

  User deleteUser(Long userId);

  User getUser(Long userId);

  Iterable<User> getUsersByTeam(Long teamId);

  /**
   * Get all users that has
   * @param nameLike
   * @return
   */
  Collection<User> getUsersByNameLike(String nameLike);

  void addWorkItemToUser(Long userId, WorkItem workItem);

  // Team
  Team addTeam(Team team);

  Team updateTeam(Team team);

  Team deleteTeam(Long teamNumber);

  Team deleteTeam(Team testTeam);

  Team getTeam(Long teamNumber);

  Team removeTeamWithId(Long teamId);

  Iterable<Team> getAllTeams();

  Team addUserToTeamWithId(Long teamId, Long userId);

  // Issue
  Issue addIssue(Issue issue);

  Issue removeIssue(Long issueId);

  Issue updateIssue(Issue issue);

  WorkItem findByNumber(Long id);

  WorkItem updateWorkItem(WorkItem updatedWorkItem);
}

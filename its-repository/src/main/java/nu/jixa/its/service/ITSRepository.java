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

  WorkItem removeWorkItem(Long workItemNumber);

  void setWorkItemStatus(Long workItemNumber, Status status);

  Collection<WorkItem> getWorkItemsByStatus(Status status);

  Collection<WorkItem> getWorkItemsByTeam(Long teamNumber);

  Collection<WorkItem> getWorkItemsByUser(Long userNumber);

  Collection<WorkItem> getWorkItemsWithIssue();

  Collection<WorkItem> getWorkItemsWithDescriptionLike(String descriptionLike);

  WorkItem getWorkItem(Long workItemNumber);

  // User
  User addUser(User user);

  User updateUser(User user);

  User deleteUser(Long userNumber);

  User getUser(Long userNumber);

  Iterable<User> getUsersByTeam(Long teamNumber);

  /**
   * Get all users that has
   */
  Collection<User> getUsersByNameLike(String nameLike);

  void addWorkItemToUser(Long userNumber, Long workItemNumber);

  // Team
  Team addTeam(Team team);

  //TODO REMOVE, nothing can be updated through team. membership is updated through user
  void updateTeam(Team team);

  Team deleteTeam(Long teamNumber);

  Team deleteTeam(Team testTeam);

  Team getTeam(Long teamNumber);

  Iterable<Team> getAllTeams();

  User addUserToTeamWithNumber(Long userNumber, Long teamNumber);

  // Issue

  Issue addIssue(Issue issue);

  Issue getIssue(Long issueNumber);

  WorkItem addIssueToWorkItem(Long workItemNumber, Long IssueNumber);

  void saveIssue(Issue issue);

  Issue updateIssue(Issue issue);

  boolean issueExists(Issue issue);

  WorkItem findByNumber(Long id);

  WorkItem updateWorkItem(WorkItem updatedWorkItem);
}

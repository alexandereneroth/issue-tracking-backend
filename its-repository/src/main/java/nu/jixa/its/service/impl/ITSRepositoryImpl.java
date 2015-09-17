package nu.jixa.its.service.impl;

import java.time.LocalDateTime;
import java.util.Collection;

import java.util.Date;
import nu.jixa.its.model.Issue;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.IssueITSRepository;
import nu.jixa.its.service.TeamITSRepository;
import nu.jixa.its.service.UserITSRepository;
import nu.jixa.its.service.WorkItemITSRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class just redirects all method calls to the more specific repository implementations.
 */
public class ITSRepositoryImpl implements ITSRepository {

  @Autowired
  UserITSRepository userITSRepository;

  @Autowired
  TeamITSRepository teamITSRepository;

  @Autowired
  WorkItemITSRepository workItemITSRepository;

  @Autowired
  IssueITSRepository issueITSRepository;

  /*
  **    WorkItem
   */

  @Override public WorkItem addWorkItem(WorkItem workItem) {
    return workItemITSRepository.addWorkItem(workItem);
  }

  @Override public WorkItem updateWorkItem(WorkItem updatedWorkItem) {
    return workItemITSRepository.updateWorkItem(updatedWorkItem);
  }

  @Override public WorkItem removeWorkItem(Long workItemNumber) {
    return workItemITSRepository.removeWorkItem(workItemNumber);
  }

  @Override public void setWorkItemStatus(Long workItemNumber, Status status) {
    workItemITSRepository.setWorkItemStatus(workItemNumber, status);
  }

  @Override public Collection<WorkItem> getWorkItemsByStatus(Status status) {
    return workItemITSRepository.getWorkItemsByStatus(status);
  }

  @Override public Collection<WorkItem> getWorkItemsByTeam(Long teamNumber) {
    return workItemITSRepository.getWorkItemsByTeam(teamNumber);
  }

  @Override public Collection<WorkItem> getWorkItemsByUser(Long userNumber) {
    return workItemITSRepository.getWorkItemsByUser(userNumber);
  }

  @Override public Collection<WorkItem> getWorkItemsWithIssue() {
    return workItemITSRepository.getWorkItemsWithIssue();
  }

  @Override public Collection<WorkItem> getWorkItemsWithDescriptionLike(String descriptionLike) {
    return workItemITSRepository.getWorkItemsWithDescriptionLike(descriptionLike);
  }

  @Override
  public Collection<WorkItem> getWorkItemsCompletedBetween(Date from, Date to) {
    return workItemITSRepository.getWorkItemsCompletedBetween(from, to);
  }

  @Override public Collection<WorkItem> getWorkItems(int pageIndex, int pageSize) {
    return workItemITSRepository.getWorkItemsPage(pageIndex, pageSize);
  }

  @Override public WorkItem getWorkItem(Long workItemNumber) {
    return workItemITSRepository.getWorkItem(workItemNumber);
  }

  /*
  **    User
   */

  @Override public User addUser(User user) {
    return userITSRepository.addUser(user);
  }

  @Override public User updateUser(User user) {
    return userITSRepository.updateUser(user);
  }

  @Override public User deleteUser(Long userNumber) {
    return userITSRepository.deleteUser(userNumber);
  }

  @Override public User getUser(Long userNumber) {
    return userITSRepository.getUser(userNumber);
  }

  @Override public Iterable<User> getUsersByTeam(Long teamNumber) {
    return userITSRepository.getUsersByTeam(teamNumber);
  }

  @Override public Collection<User> getUsersByNameLike(String nameLike) {
    return userITSRepository.getUsersByNameLike(nameLike);
  }

  @Override public Collection<User> getUsers(int pageIndex, int pageSize) {
    return userITSRepository.getUsersPage(pageIndex, pageSize);
  }

  @Override public void addWorkItemToUser(Long userNumber, Long workItemNumber) {
    userITSRepository.addWorkItemToUser(userNumber, workItemNumber);
  }

  /*
  **    Team
   */

  @Override public Team addTeam(Team team) {
    return teamITSRepository.addTeam(team);
  }

  @Override public void updateTeam(Team team) {
    teamITSRepository.updateTeam(team);
  }

  @Override public Team deleteTeam(Long teamNumber) {
    return teamITSRepository.deleteTeam(teamNumber);
  }

  @Override public Team deleteTeam(Team testTeam) {
    return teamITSRepository.deleteTeam(testTeam);
  }

  @Override public Team getTeam(Long teamNumber) {
    return teamITSRepository.getTeam(teamNumber);
  }

  @Override public Iterable<Team> getAllTeams() {
    return teamITSRepository.getAllTeams();
  }

  @Override public User addUserToTeamWithNumber(Long userNumber, Long teamNumber) {
    return teamITSRepository.addUserToTeamWithNumber(userNumber, teamNumber);
  }

  /*
  **    Issue
   */

  @Override public Issue addIssue(Issue issue) {
    return issueITSRepository.addIssue(issue);
  }

  @Override public Issue getIssue(Long issueNumber) {
    return issueITSRepository.getIssue(issueNumber);
  }

  @Override public Collection<Issue> getIssues(int page, int pageSize) {
    return issueITSRepository.getIssuesPage(page, pageSize);
  }

  @Override public WorkItem addIssueToWorkItem(Long workItemNumber, Long issueNumber) {
    return issueITSRepository.addIssueToWorkItem(workItemNumber, issueNumber);
  }

  @Override public void saveIssue(Issue issue) {
    issueITSRepository.saveIssue(issue);
  }

  @Override public Issue updateIssue(Issue issue) {
    return issueITSRepository.updateIssue(issue);
  }

  @Override public boolean issueExists(Issue issue) {
    return issueITSRepository.issueExists(issue);
  }
}

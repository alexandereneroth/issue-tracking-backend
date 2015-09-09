package nu.jixa.its.service;

import java.util.Collection;
import nu.jixa.its.model.Issue;
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
    WorkItem workItem = getWorkItemById(updatedWorkItem.getNumber());
    workItemRepository.save(workItem);
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

    //userRepository.selectUserByTeamId(teamNumber);
    //return workItemRepository.getWorkItemsByTeam(teamNumber);
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
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(deletedUser,"Could not delete User: No user with number " + userId );
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

  @Override public Iterable<User> getUsersByTeam(Long teamNumber) {
    return userRepository.findByTeamNumber(teamNumber);
  }

  @Override public Collection<User> getUsersByNameLike(String nameLike) {
    return userRepository.selectByNameLike(nameLike);
  }

  @Override public void addWorkItemToUser(Long userId, WorkItem workItem) {

  }

  @Override public Team addTeam(Team team) {
    return teamRepository.save(team);
  }

  @Override public Team updateTeam(Team team) {
    return teamRepository.save(team);
  }

  @Override public Team deleteTeam(Long teamNumber) {
    Team deletedTeam = teamRepository.findByNumber(teamNumber);
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(deletedTeam,"Could not delete Team: No team with number " + teamNumber );
    teamRepository.delete(deletedTeam);
    return deletedTeam;
  }

  @Override public Team deleteTeam(Team team) {
    Team deletedTeam = teamRepository.findOne(team.getId());
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(deletedTeam,
        "Could not delete Team: Team not in repository");

    teamRepository.delete(deletedTeam);
    return team;
  }

  @Override public Team getTeam(Long teamNumber) {
    Team team = teamRepository.findByNumber(teamNumber);
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(team,
        "Could not find Team: No team with number " + teamNumber);
    return team;
  }

  @Override public Team removeTeamWithId(Long teamId) {
    return null;
  }

  @Override public Iterable<Team> getAllTeams() {
    return teamRepository.findAll();
  }

  @Override public User addUserToTeamWithNumber(Long userNumber, Long teamNumber) {

    User user = getUser(userNumber);
    Team team = getTeam(teamNumber);
    user.setTeam(team);

    userRepository.save(user);
    return user;
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

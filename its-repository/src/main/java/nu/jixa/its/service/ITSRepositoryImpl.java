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
  private TeamRepository teamRepository;

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
  @Override public WorkItem getWorkItem(Long workItemNumber) {
    WorkItem workItemInDB = workItemRepository.findByNumber(workItemNumber);
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(workItemInDB,
        "Could not find workItem: No item with nr " + workItemNumber);
    return workItemRepository.findByNumber(workItemNumber);
  }
  @Transactional
  @Override public void setWorkItemStatus(Long workItemNumber, Status status) {
    WorkItem item = workItemRepository.findByNumber(workItemNumber);
    item.setStatus(status);
    workItemRepository.save(item);
  }

  @Override public Collection<WorkItem> getWorkItemsByStatus(Status status) {
    return workItemRepository.findByStatus(status);
  }

  @Override public Collection<WorkItem> getWorkItemsByTeam(Long teamNumber) {

    //userRepository.selectUserByTeamId(teamNumber);
    //return workItemRepository.getWorkItemsByTeam(teamNumber);
    return null;
  }

  @Override public Collection<WorkItem> getWorkItemsByUser(Long userNumber) {
    return workItemRepository.findByUsersNumber(userNumber);
  }

  @Override public Collection<WorkItem> getWorkItemsByIssue(Long issueNumber) {
    return workItemRepository.findByIssueNumber(issueNumber);
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

  @Override public User deleteUser(Long userNumber) {
    User deletedUser = userRepository.findByNumber(userNumber);
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(deletedUser,"Could not delete User: No user with number " + userNumber );
    userRepository.delete(deletedUser);
    return deletedUser;
  }

  @Override public User getUser(Long userNumber) {
    User gotUser = userRepository.findByNumber(userNumber);
    if (gotUser == null) {
      throw new ITSRepositoryException("Could not get User: No user with id " + userNumber);
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
  @Override public WorkItem findByNumber(Long workItemNr) {
    WorkItem item = workItemRepository.findByNumber(workItemNr);
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(item,"Could not find User: No user with id" + workItemNr );
    return item;
  }
}

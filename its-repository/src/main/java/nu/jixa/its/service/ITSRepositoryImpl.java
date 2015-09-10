package nu.jixa.its.service;

import java.util.Collection;
import java.util.Iterator;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.repository.RepositoryUtil;
import nu.jixa.its.repository.TeamRepository;
import nu.jixa.its.repository.UserRepository;
import nu.jixa.its.repository.WorkItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

public class ITSRepositoryImpl implements ITSRepository {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private WorkItemRepository workItemRepository;

  @Autowired
  private TeamRepository teamRepository;

  @Override public WorkItem updateWorkItem(WorkItem updatedWorkItem) {

    WorkItem workItemFromRepository = getWorkItem(updatedWorkItem.getNumber());
    workItemFromRepository.copyFields(updatedWorkItem);
    return workItemRepository.save(workItemFromRepository);

  }
  @Transactional
  @Override public WorkItem addWorkItem(WorkItem workItem) {
    //issueRepository.save(workItem.getIssue());
    return workItemRepository.save(workItem);
  }

  @Transactional
  @Override public WorkItem removeWorkItem(Long workItemNumber) {
    WorkItem deleteItem = findByNumber(workItemNumber);

    if(deleteItem.getUsers().size() > 0)
    {
      removeWorkItemFromItsUsers(deleteItem);
    }
    workItemRepository.delete(deleteItem);
    return deleteItem;
  }

  private void removeWorkItemFromItsUsers(WorkItem workItem) {
    Iterator<User> userIterator = workItem.getUsers().iterator();
    while(userIterator.hasNext())
    {
      User userToRemoveWorkItemFrom = userIterator.next();
      userToRemoveWorkItemFrom.getWorkItems().remove(workItem);
      userRepository.save(userToRemoveWorkItemFrom);
      userIterator.remove();
    }
    workItemRepository.save(workItem);
  }

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
    try {
      workItemRepository.save(user.getWorkItems());
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not add user", e);
    }
    return user;
  }

  @Transactional
  @Override public User updateUser(User user) {
    User userInRepo = getUser(user.getNumber());
    userInRepo.copyFields(user);

    return userRepository.save(userInRepo);
  }

  @Override public User deleteUser(Long userNumber) {
    User deletedUser = userRepository.findByNumber(userNumber);
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(deletedUser,
        "Could not delete User: No user with number " + userNumber);
    if (deletedUser.getTeam() != null) {
      // Gotcha: References from other objects need to be cleared and saved to the database
      // before removal
      removeUserFromItsTeam(deletedUser);
    }
    if(deletedUser.getWorkItems().size() > 0)
    {
      removeUserFromItsWorkItems(deletedUser);
    }
    userRepository.delete(deletedUser);
    return deletedUser;
  }

  private void removeUserFromItsTeam(User leavingUser) {
    Team leavingUsersTeam = leavingUser.getTeam();
    leavingUser.leaveTeam();
    teamRepository.save(leavingUsersTeam);
    userRepository.save(leavingUser);
  }

  private void removeUserFromItsWorkItems(User userToRemove) {
    Iterator<WorkItem> workItemIterator = userToRemove.getWorkItems().iterator();
    while(workItemIterator.hasNext())
    {
      WorkItem workItemToRemoveUserFrom = workItemIterator.next();
      workItemToRemoveUserFrom.getUsers().remove(userToRemove);
      workItemRepository.save(workItemToRemoveUserFrom);
      workItemIterator.remove();
    }
    userRepository.save(userToRemove);
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

  @Override public void addWorkItemToUser(Long userId, Long workItemId) {

    WorkItem item = getWorkItem(workItemId);
    User user = getUser(userId);
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(item,"Could not find workItem: No workItem with number " + workItemId );
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(user,"Could not find user: No user with number " + userId );

    //item.addUser(getUser(userId));
    //workItemRepository.save(item);
    user.addWorkItem(item);
    userRepository.save(user);
  }

  @Override public Team addTeam(Team team) {
    return teamRepository.save(team);
  }

  @Override public void updateTeam(Team team) {
    Team teamInRepo = teamRepository.findByNumber(team.getNumber());

    Collection<User> usersBeforeUpdate = teamInRepo.getUsers();
    Collection<User> usersAfterUpdate = team.getUsers();

    removeAllUsersThatExistedInTeamBeforeUpdateButNotAfter(team, usersBeforeUpdate,usersAfterUpdate);
    for(User user : usersAfterUpdate)
    {
      user.joinTeam(teamInRepo);
      updateUser(user);
    }
  }

  public void removeAllUsersThatExistedInTeamBeforeUpdateButNotAfter(Team team, Collection<User> usersBeforeUpdate, Collection<User> usersAfterUpdate){
    Iterator<User> usersBeforeUpdateIterator = usersBeforeUpdate.iterator();
    while(usersBeforeUpdateIterator.hasNext())
    {
      User userBeforeUpdate = usersBeforeUpdateIterator.next();
      if(!usersAfterUpdate.contains(userBeforeUpdate))
      {
        usersBeforeUpdateIterator.remove();
        if(userBeforeUpdate.getTeam().equals(team)) {
          userBeforeUpdate.leaveTeam();
        }
        updateUser(userBeforeUpdate);
      }
    }
  }

  @Override public Team deleteTeam(Team team) {
    return deleteTeam(team.getNumber());
  }

  @Override public Team deleteTeam(Long teamNumber) {
    Team teamToDelete = teamRepository.findByNumber(teamNumber);
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(teamToDelete,
        "Could not delete Team: No team with number " + teamNumber);

    Iterator<User> usersIterator = teamToDelete.getUsers().iterator();
    while (usersIterator.hasNext()) {
      User userToRemoveFromTeam = usersIterator.next();
      usersIterator.remove();
      userToRemoveFromTeam.leaveTeam();
      userRepository.save(userToRemoveFromTeam);
    }
    teamRepository.delete(teamToDelete);
    return teamToDelete;
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
    user.joinTeam(team);

    userRepository.save(user);
    return user;
  }

  @Override public WorkItem findByNumber(Long workItemNr) {
    WorkItem item = workItemRepository.findByNumber(workItemNr);
    RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(item,
        "Could not find User: No user with id" + workItemNr);
    return item;
  }
}

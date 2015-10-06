package nu.jixa.its.service.impl;

import java.util.Collection;
import java.util.Iterator;
import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.model.exception.RepositoryModelException;
import nu.jixa.its.repository.TeamRepository;
import nu.jixa.its.repository.UserRepository;
import nu.jixa.its.repository.WorkItemRepository;
import nu.jixa.its.service.UserITSRepository;
import nu.jixa.its.service.WorkItemITSRepository;
import nu.jixa.its.service.exception.ITSRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

public class UserITSRepositoryImpl implements UserITSRepository {

  @Autowired
  UserRepository userRepository;

  @Autowired
  WorkItemRepository workItemRepository;

  @Autowired
  TeamRepository teamRepository;

  @Autowired
  WorkItemITSRepository workItemITSRepository;

  @Transactional
  @Override
  public User addUser(User user) {
    try {
      workItemRepository.save(user.getWorkItems());
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not add user", e);
    }
    return user;
  }

  @Transactional
  @Override
  public User updateUser(User user) {
    User userInRepo = getUser(user.getNumber());
    userInRepo.copyFields(user);
    try {
      return userRepository.save(userInRepo);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not add user", e);
    }
  }

  @Override
  public User deleteUser(Long userNumber) {
    User deletedUser = userRepository.findByNumber(userNumber);
    Util.throwExceptionIfNull(deletedUser,
        "Could not delete User: No user with number " + userNumber);
    if (deletedUser.getTeam() != null) {
      // Gotcha: References from other objects need to be cleared and saved to the database
      // before removal
      removeUserFromItsTeam(deletedUser);
    }
    if (deletedUser.getWorkItems().size() > 0) {
      removeUserFromItsWorkItems(deletedUser);
    }
    try {
      userRepository.delete(deletedUser);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not add user", e);
    }
    return deletedUser;
  }

  private void removeUserFromItsTeam(User leavingUser) {
    Team leavingUsersTeam = leavingUser.getTeam();
    leavingUser.leaveTeam();
    try {
      teamRepository.save(leavingUsersTeam);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not update team", e);
    }
    try {
      userRepository.save(leavingUser);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not update user", e);
    }
  }

  private void removeUserFromItsWorkItems(User userToRemove) {
    Iterator<WorkItem> workItemIterator = userToRemove.getWorkItems().iterator();
    while (workItemIterator.hasNext()) {
      WorkItem workItemToRemoveUserFrom = workItemIterator.next();
      workItemToRemoveUserFrom.getUsers().remove(userToRemove);
      try {
        workItemRepository.save(workItemToRemoveUserFrom);
      } catch (DataIntegrityViolationException e) {
        throw new ITSRepositoryException("Could not update workItem", e);
      }
      workItemIterator.remove();
    }
    try {
      userRepository.save(userToRemove);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not update user", e);
    }
  }

  @Override
  public User getUser(Long userNumber) {
    User gotUser = userRepository.findByNumber(userNumber);
    if (gotUser == null) {
      throw new ITSRepositoryException("Could not get User: No user with id " + userNumber);
    }
    return gotUser;
  }

  @Override
  public Iterable<User> getUsersByTeam(Long teamNumber) {
    return userRepository.selectByTeamNumber(teamNumber);
  }

  @Override
  public Collection<User> getUsersByNameLike(String nameLike) {
    return userRepository.selectByNameLike(nameLike);
  }

  @Override public Collection<User> getUsersPage(int pageIndex, int pageSize) {
    if (pageIndex < 0 || pageSize < 1) {
      throw new ITSRepositoryException("Could not get Users: invalid page or pageSize");
    }
    Page<User> userPage = userRepository.findAll(new PageRequest(pageIndex, pageSize));
    return Util.iterableToArrayList(userPage);
  }

  @Override
  public void addWorkItemToUser(Long userId, Long workItemId) {
    WorkItem item = workItemITSRepository.getWorkItem(workItemId);
    User user = getUser(userId);
    Util.throwExceptionIfNull(item,
        "Could not find workItem: No workItem with number " + workItemId);
    Util.throwExceptionIfNull(user,
        "Could not find user: No user with number " + userId);
    user.addWorkItem(item);
    try {
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not save user", e);
    }
  }

  @Override public void removeWorkItemFromUser(Long userNumber, Long workItemNumber) {
    WorkItem item = workItemITSRepository.getWorkItem(workItemNumber);
    User user = getUser(userNumber);
    Util.throwExceptionIfNull(item,
        "Could not find workItem: No workItem with number " + workItemNumber);
    Util.throwExceptionIfNull(user,
        "Could not find user: No user with number " + userNumber);
    try {
      user.removeWorkItem(item);
    } catch (RepositoryModelException e) {
      throw new ITSRepositoryException(e.getMessage());
    }
    try {
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not save user", e);
    }
  }
}

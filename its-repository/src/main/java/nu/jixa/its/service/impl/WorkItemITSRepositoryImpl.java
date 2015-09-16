package nu.jixa.its.service.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import nu.jixa.its.model.Issue;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.repository.RepositoryUtil;
import nu.jixa.its.repository.UserRepository;
import nu.jixa.its.repository.WorkItemRepository;
import nu.jixa.its.service.exception.ITSRepositoryException;
import nu.jixa.its.service.IssueITSRepository;
import nu.jixa.its.service.WorkItemITSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

public class WorkItemITSRepositoryImpl implements WorkItemITSRepository {

  @Autowired
  WorkItemRepository workItemRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  IssueITSRepository issueITSRepository;

  @Override
  public WorkItem updateWorkItem(WorkItem updatedWorkItem) {
    WorkItem workItemInRepository = getWorkItem(updatedWorkItem.getNumber());
    RepositoryUtil.throwExceptionIfNull(workItemInRepository,
        "Could not update workItem: workItem with number "
            + updatedWorkItem.getNumber()
            + " doesn't exist");
    Issue updatedWorkItemsIssue = updatedWorkItem.getIssue();
    if (updatedWorkItemsIssue != null) {
      issueITSRepository.saveIssue(updatedWorkItemsIssue);
    }
    workItemInRepository.copyFields(updatedWorkItem);
    try {
      return workItemRepository.save(workItemInRepository);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not save user", e);
    }
  }

  @Transactional
  @Override
  public WorkItem addWorkItem(WorkItem workItem) {
    try {
      return workItemRepository.save(workItem);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not add workItem", e);
    }
  }

  @Transactional
  @Override
  public WorkItem removeWorkItem(Long workItemNumber) {
    WorkItem deleteItem = getWorkItem(workItemNumber);
    RepositoryUtil.throwExceptionIfNull(deleteItem,
        "Could not remove workItem: workItem with number "
            + deleteItem.getNumber()
            + " doesn't exist");
    if (deleteItem.getUsers().size() > 0) {
      removeWorkItemFromItsUsers(deleteItem);
    }
    try {
      workItemRepository.delete(deleteItem);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not delete workItem", e);
    }
    return deleteItem;
  }

  @Transactional
  private void removeWorkItemFromItsUsers(WorkItem workItem) {
    WorkItem workItemFromDB = workItemRepository.findByNumber(workItem.getNumber());
    RepositoryUtil.throwExceptionIfNull(workItemFromDB,
        "Could not remove work item from its users: workItem with number "
            + workItemFromDB.getNumber()
            + " doesn't exist in repository");
    Iterator<User> userIterator = workItem.getUsers().iterator();
    while (userIterator.hasNext()) {
      User userToRemoveWorkItemFrom = userIterator.next();
      userToRemoveWorkItemFrom.getWorkItems().remove(workItem);
      try {
        userRepository.save(userToRemoveWorkItemFrom);
      } catch (DataIntegrityViolationException e) {
        throw new ITSRepositoryException("Could not save user", e);
      }
      userIterator.remove();
    }
    workItemRepository.save(workItem);
  }

  @Override
  public WorkItem getWorkItem(Long workItemNumber) {
    WorkItem workItemInRepository = workItemRepository.findByNumber(workItemNumber);
    RepositoryUtil.throwExceptionIfNull(workItemInRepository,
        "Could not find workItem: No item with number " + workItemNumber);
    return workItemInRepository;
  }

  @Transactional
  @Override
  public void setWorkItemStatus(Long workItemNumber, Status status) {
    WorkItem item = workItemRepository.findByNumber(workItemNumber);
    RepositoryUtil.throwExceptionIfNull(item,
        "Could not find workItem: No item with number " + workItemNumber);
    item.setStatus(status);
    try {
      workItemRepository.save(item);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not save user", e);
    }
  }

  @Override
  public Collection<WorkItem> getWorkItemsByStatus(Status status) {
    return workItemRepository.findByStatus(status);
  }

  @Override
  public Collection<WorkItem> getWorkItemsByTeam(Long teamNumber) {
    return workItemRepository.findByUsersTeamNumber(teamNumber);
  }

  @Override
  public Collection<WorkItem> getWorkItemsByUser(Long userNumber) {
    return workItemRepository.findByUsersNumber(userNumber);
  }

  @Override
  public Collection<WorkItem> getWorkItemsWithIssue() {
    return workItemRepository.findAllWorkItemsWithIssue();
  }

  @Override
  public Collection<WorkItem> getWorkItemsWithDescriptionLike(String descriptionLike) {
    return workItemRepository.findWorkItemsWithDescriptionLike(descriptionLike);
  }

  @Override
  public Collection<WorkItem> getWorkItemsCompletedBetween(LocalDateTime from, LocalDateTime to) {
    return workItemRepository.findByCompletedDateBetween(from,to);
  }
}

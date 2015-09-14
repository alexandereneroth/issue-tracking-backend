package nu.jixa.its.service;

import java.util.Collection;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.WorkItem;

public interface WorkItemITSRepository {

  WorkItem updateWorkItem(WorkItem updatedWorkItem);

  WorkItem addWorkItem(WorkItem workItem);

  WorkItem removeWorkItem(Long workItemNumber);

  void setWorkItemStatus(Long workItemNumber, Status status);

  Collection<WorkItem> getWorkItemsByStatus(Status status);

  Collection<WorkItem> getWorkItemsByTeam(Long teamNumber);

  Collection<WorkItem> getWorkItemsByUser(Long userNumber);

  Collection<WorkItem> getWorkItemsWithIssue();

  Collection<WorkItem> getWorkItemsWithDescriptionLike(String descriptionLike);

  WorkItem getWorkItem(Long workItemNumber);
}

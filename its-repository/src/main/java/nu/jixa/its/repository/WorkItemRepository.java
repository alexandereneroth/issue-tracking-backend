package nu.jixa.its.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface WorkItemRepository extends CrudRepository<WorkItem, Long> {

  WorkItem findByNumber(Long id);

  Collection<WorkItem> findByStatus(Status status);

  Collection<WorkItem> findByUsersNumber(Long userNumber);

  @Query(value = "select w from WorkItem w join fetch w.users u join fetch u.team t where t.number =?1")
  Collection<WorkItem> findByUsersTeamNumber(Long teamNumber);

  @Query("select w from WorkItem w where w.description like %?1%")
  Collection<WorkItem> findWorkItemsWithDescriptionLike(String descriptionLike);

  @Query("select w from WorkItem w where w.issue is not null")
  Collection<WorkItem> findAllWorkItemsWithIssue();

  Collection<WorkItem> findByCompletedDateBetween(LocalDateTime from, LocalDateTime to);
}

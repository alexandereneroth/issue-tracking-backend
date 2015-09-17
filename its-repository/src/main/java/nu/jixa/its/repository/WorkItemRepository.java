package nu.jixa.its.repository;

import java.util.Collection;
import java.util.Date;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.WorkItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WorkItemRepository extends PagingAndSortingRepository<WorkItem, Long> {

  WorkItem findByNumber(Long id);

  Collection<WorkItem> findByStatus(Status status);

  Collection<WorkItem> findByUsersNumber(Long userNumber);

  @Query(value = "select w from WorkItem w join fetch w.users u join fetch u.team t where t.number =?1")
  Collection<WorkItem> findByUsersTeamNumber(Long teamNumber);

  @Query("select w from WorkItem w where w.description like %?1%")
  Collection<WorkItem> findWorkItemsWithDescriptionLike(String descriptionLike);

  @Query("select w from WorkItem w where w.issue is not null")
  Collection<WorkItem> findAllWorkItemsWithIssue();

  Collection<WorkItem> findByCompletedDateBetween(Date from, Date to);
}

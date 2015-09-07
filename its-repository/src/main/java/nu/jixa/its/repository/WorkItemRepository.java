package nu.jixa.its.repository;

import java.util.Collection;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface WorkItemRepository extends CrudRepository<WorkItem, Long> {

  WorkItem findByNumber(Long id);

  Collection<WorkItem> findByStatus(Status status);

  //TODO confusing method names?
  WorkItem findByUsers(User user);
  Collection<WorkItem> findByUsersId(Long userId);

  Collection<WorkItem> findByIssueId(Long issueId);

  @Query("select w from WorkItem w where w.description like %?1")
  Collection<WorkItem> findWorkItemByDescLike(String descriptionLike);
}

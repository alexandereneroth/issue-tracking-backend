package nu.jixa.its.repository;

import java.util.Collection;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.WorkItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface WorkItemRepository extends CrudRepository<WorkItem, Long> {

  WorkItem findById(Long id);

  @Transactional
  WorkItem deleteById(Long Id);

  Collection<WorkItem> findByStatus(Status status);

}

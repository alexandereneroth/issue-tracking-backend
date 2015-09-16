package nu.jixa.its.repository;

import java.util.Collection;
import nu.jixa.its.model.Issue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IssueRepository extends PagingAndSortingRepository<Issue, Long> {

  Issue findByNumber(Long number);

}

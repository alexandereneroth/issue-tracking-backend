package nu.jixa.its.repository;

import nu.jixa.its.model.Issue;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IssueRepository extends PagingAndSortingRepository<Issue, Long> {

  Issue findByNumber(Long number);
}

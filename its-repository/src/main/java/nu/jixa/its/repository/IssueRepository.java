package nu.jixa.its.repository;

import nu.jixa.its.model.Issue;
import org.springframework.data.repository.CrudRepository;

public interface IssueRepository extends CrudRepository<Issue, Long> {

  Issue findByNumber(Long number);
}

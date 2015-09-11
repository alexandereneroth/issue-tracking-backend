package nu.jixa.its.repository;

import nu.jixa.its.model.Issue;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by gina on 2015-09-01.
 */
public interface IssueRepository extends CrudRepository<Issue, Long> {

  Issue findByNumber(Long number);
}

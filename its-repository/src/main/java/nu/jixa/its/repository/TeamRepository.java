package nu.jixa.its.repository;

import nu.jixa.its.model.Team;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by gina on 2015-09-01.
 */
public interface TeamRepository extends CrudRepository<Team, Long> {

  Team findByNumber(Long teamNumber);
}



package nu.jixa.its.repository;

import nu.jixa.its.model.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {

  Team findByNumber(Long teamNumber);
}



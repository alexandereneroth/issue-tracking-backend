package nu.jixa.its.repository;

import java.util.Collection;
import nu.jixa.its.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

  @Query("select u from User u join u.team t where t.id = ?1")
  Collection<User> selectUserByTeamId(Long id);

  User findByNumber(Long number);

}

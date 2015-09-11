package nu.jixa.its.repository;

import java.util.Collection;
import nu.jixa.its.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {

  @Query("select u from User u join fetch u.team t where t.id = ?1")
  Collection<User> selectByTeamId(Long teamId);

  @Query("select u from User u where (u.username like %:name%) or (u.firstname like %:name%) or (u.lastname like %:name%)")
  Collection<User> selectByNameLike(@Param("name") String name);
  
  User findByNumber(Long number);

  Iterable<User> findByTeamNumber(Long number);
}

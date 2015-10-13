package nu.jixa.its.repository;

import java.util.Collection;
import nu.jixa.its.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

  @Query("select u from User u join fetch u.team t where t.number = ?1")
  Iterable<User> selectByTeamNumber(
      Long teamNumber);

  @Query("select u from User u where (u.username like %:name%) or (u.firstname like %:name%) or (u.lastname like %:name%)")
  Collection<User> selectByNameLike(@Param("name") String name);

  User findByNumber(Long number);

  User findByUsername(String username);
}

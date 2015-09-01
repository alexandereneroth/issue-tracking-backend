package nu.jixa.its.repository;

import nu.jixa.its.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}

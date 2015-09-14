package nu.jixa.its.service;

import java.util.Collection;
import nu.jixa.its.model.User;

public interface UserITSRepository {

  User addUser(User user);

  User updateUser(User user);

  User deleteUser(Long userNumber);

  User getUser(Long userNumber);

  Iterable<User> getUsersByTeam(Long teamNumber);

  Collection<User> getUsersByNameLike(String nameLike);

  void addWorkItemToUser(Long userNumber, Long workItemNumber);
}

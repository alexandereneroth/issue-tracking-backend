package nu.jixa.its.service;

import java.util.Collection;
import nu.jixa.its.model.User;

public interface UserITSRepository {

  User addUser(User user);

  User updateUser(User user);

  User deleteUser(Long userNumber);

  User getUser(Long userNumber);

  User getUser(String username);

  Iterable<User> getUsersByTeam(Long teamNumber);

  Collection<User> getUsersByNameLike(String nameLike);

  Collection<User> getUsersPage(int pageIndex, int pageSize);

  void addWorkItemToUser(Long userNumber, Long workItemNumber);

  void removeWorkItemFromUser(Long userNumber, Long workItemNumber);
}

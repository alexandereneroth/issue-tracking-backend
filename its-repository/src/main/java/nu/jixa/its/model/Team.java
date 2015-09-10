package nu.jixa.its.model;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tblTeam")
public class Team extends AbstractEntity<Team> {

  @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
  Collection<User> users = new ArrayList<>();

  protected Team() {
  }

  @Override public void copyFields(Team other) {
    this.users = other.users;
  }

  public Team(@NotNull final Long number) {
    ModelUtil.throwExceptionIfArgIsNull(number, "number");
    this.number = number;
  }

  // Users team membership is controlled from methods in the user class.
  protected void addUser(User user) {
    ModelUtil.throwExceptionIfArgIsNull(user, "user");
    users.add(user);
  }

  protected void removeUser(User user) {
    ModelUtil.throwExceptionIfArgIsNull(user, "user");
    users.remove(user);
  }

  public Collection<User> getUsers() {
    return users;//TODO return copy
  }


  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Team team = (Team) o;

    return getNumber().equals(team.getNumber());
  }

  @Override public int hashCode() {
    return getNumber().hashCode();
  }

}

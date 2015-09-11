package nu.jixa.its.model;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tblTeam")
public class Team extends AbstractEntity<Team> {

  @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
  Collection<User> users = new ArrayList<>();

  @Column(name = "name")
  String name;

  protected Team() {
  }

  @Override public void copyFields(Team other) {
    this.name = other.name;
    //    this.users = other.users;
  }

  public Team(@NotNull final Long number) {
    ModelUtil.throwExceptionIfArgIsNull(number, "number");
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

    if (!getNumber().equals(team.getNumber())) return false;

    return !(getName() != null ? !getName().equals(team.getName())
        : team.getName() != null);
  }

  @Override public int hashCode() {
    int result = getNumber().hashCode();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    return result;
  }
}

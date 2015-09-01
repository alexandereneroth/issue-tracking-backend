package nu.jixa.its.model;

import com.sun.istack.internal.NotNull;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tblTeam")
public class Team extends AbstractEntity {

  @Column(name = "number", unique = true, nullable = false)
  private Long number;

  @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
  Collection<User> users;

  protected  Team(){}
  public Team(@NotNull final Long number) {
    if (number == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
    this.number = number;
  }

  public Long getNumber() {
    return number;
  }

  public void setNumber(@NotNull final Long number) {
    if (number == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
    this.number = number;
  }

  public Collection<User> getUsers() {
    return users;
  }

  public void addUser(@NotNull final User user) {
    if (user == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
    users.add(user);
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

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

  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
  Collection<User> users;

  public Team(@NotNull final Long id) {
    if (id == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(@NotNull final Long id) {
    if (id == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
    this.id = id;
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
}

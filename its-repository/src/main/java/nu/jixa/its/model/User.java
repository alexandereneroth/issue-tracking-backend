package nu.jixa.its.model;

import com.sun.istack.internal.NotNull;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tblUser")
public class User extends AbstractEntity {

  @Column(name = "number", unique = true, nullable = false)
  private Long number;

  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @Column(name = "firstname", nullable = false)
  private String firstname;

  @Column(name = "lastname", nullable = false)
  private String lastname;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "team")
  private Team team;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "tblUser_tblWorkItem",
      joinColumns = @JoinColumn( name = "userId", referencedColumnName="id"),
      inverseJoinColumns = @JoinColumn(name ="workItemId", referencedColumnName="id")
  )
  private Collection<WorkItem> workItems;

  protected User() {
  }

  public User(Long number, String username, String firstname, String lastname) {
    if (number == null) {
      throw new RepositoryModelException("Null argument not allowed: number");
    }
    if (username == null) {
      throw new RepositoryModelException("Null argument not allowed: username");
    }
    if (firstname == null) {
      throw new RepositoryModelException("Null argument not allowed: firstname");
    }
    if (lastname == null) {
      throw new RepositoryModelException("Null argument not allowed: lastname");
    }
    this.number = number;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(@NotNull final String username) {
    if (username == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
    this.username = username;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(@NotNull final String firstname) {
    if (firstname == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(@NotNull final String lastname) {
    if (lastname == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
    this.lastname = lastname;
  }

  public Team getTeam(Team team) {
    return team;
  }


  public void setTeam(@NotNull final Team team) {
    if (team == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
    this.team = team;
  }

  public Collection<WorkItem> getWorkItems() {
    return workItems;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (!getNumber().equals(user.getNumber())) return false;
    if (!getUsername().equals(user.getUsername())) return false;
    if (!getFirstname().equals(user.getFirstname())) return false;
    return getLastname().equals(user.getLastname());
  }

  @Override public int hashCode() {
    int result = getNumber().hashCode();
    result = 31 * result + getUsername().hashCode();
    result = 31 * result + getFirstname().hashCode();
    result = 31 * result + getLastname().hashCode();
    return result;
  }
}

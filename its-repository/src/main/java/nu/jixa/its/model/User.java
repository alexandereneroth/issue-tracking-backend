package nu.jixa.its.model;

import com.sun.istack.internal.NotNull;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tblUser")
public class User extends AbstractEntity{

  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @Column(name = "firstname", nullable = false)
  private String firstname;

  @Column(name = "lastname", nullable = false)
  private String lastname;

  @ManyToMany
  @JoinTable(name = "user_work-items")
  private Collection<WorkItem> workItems;

  public User(Long id, String username, String firstname, String lastname) {
    if (id == null) {
      throw new RepositoryModelException("Null argument not allowed: id");
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
    this.id = id;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
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
}

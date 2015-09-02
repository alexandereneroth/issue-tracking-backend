package nu.jixa.its.web;

import com.sun.istack.internal.NotNull;

public class User {

  private Long id;

  private String username;

  private String firstname;

  private String lastname;

  public User(Long id, String username, String firstname, String lastname) {
    this.id = id;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
  }

  public Long getId() {
    return id;
  }

  public void setId(@NotNull final Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(@NotNull final String username) {
    this.username = username;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(@NotNull final String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(@NotNull final String lastname) {
    this.lastname = lastname;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (!id.equals(user.id)) return false;
    if (!username.equals(user.username)) return false;
    if (!firstname.equals(user.firstname)) return false;
    return lastname.equals(user.lastname);
  }

  @Override public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + username.hashCode();
    result = 31 * result + firstname.hashCode();
    result = 31 * result + lastname.hashCode();
    return result;
  }
}

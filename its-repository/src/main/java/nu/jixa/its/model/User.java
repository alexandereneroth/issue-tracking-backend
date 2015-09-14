package nu.jixa.its.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import nu.jixa.its.serializer.UserJsonSerializer;

@Entity
@Table(name = "tblUser")
@JsonSerialize(using = UserJsonSerializer.class)
public class User extends AbstractEntity<User> {

  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @Column(name = "firstname", nullable = false)
  private String firstname;

  @Column(name = "lastname", nullable = false)
  private String lastname;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "team_id")
  private Team team;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "tblUser_tblWorkItem",
      joinColumns = @JoinColumn(name = "user_Id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "workItem_Id", referencedColumnName = "id")
  )
  private Collection<WorkItem> workItems = new ArrayList<>();

  protected User() {
  }

  @Override public void copyFields(User user) {
    this.username = user.username;
    this.firstname = user.firstname;
    this.lastname = user.lastname;
    this.team = user.team;
    this.workItems = user.workItems;
  }

  public User(@NotNull Long number, @NotNull String username, @NotNull String firstname,
      @NotNull String lastname) {
    HashMap<Object, String> argsWithNames = new HashMap<>();
    argsWithNames.put(number, "number");
    argsWithNames.put(username, "username");
    argsWithNames.put(firstname, "firstname");
    argsWithNames.put(lastname, "lastname");

    ModelUtil.throwExceptionIfArgIsNull(argsWithNames);

    this.number = number;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(@NotNull final String username) {
    ModelUtil.throwExceptionIfArgIsNull(username, "username");
    this.username = username;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(@NotNull final String firstname) {
    ModelUtil.throwExceptionIfArgIsNull(firstname, "firstname");
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(@NotNull final String lastname) {
    ModelUtil.throwExceptionIfArgIsNull(lastname, "lastname");
    this.lastname = lastname;
  }

  public void addWorkItem(WorkItem item) {
    workItems.add(item);
  }

  public Collection<WorkItem> getWorkItems() {
    return workItems;
  }

  public Team getTeam() {
    return team;
  }

  public void joinTeam(@NotNull final Team team) {
    ModelUtil.throwExceptionIfArgIsNull(team, "team");
    this.team = team;
    team.addUser(this);
  }

  public void leaveTeam() {
    team.removeUser(this);
    team = null;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (!getNumber().equals(user.getNumber())) return false;
    if (!getUsername().equals(user.getUsername())) return false;
    if (!getFirstname().equals(user.getFirstname())) return false;
    if (!getLastname().equals(user.getLastname())) return false;
    return !(team != null ? !team.equals(user.team) : user.team != null);
  }

  @Override public int hashCode() {
    int result = getNumber().hashCode();
    result = 31 * result + getUsername().hashCode();
    result = 31 * result + getFirstname().hashCode();
    result = 31 * result + getLastname().hashCode();
    result = 31 * result + (team != null ? team.hashCode() : 0);
    return result;
  }
}

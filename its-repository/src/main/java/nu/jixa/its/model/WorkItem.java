package nu.jixa.its.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.internal.NotNull;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import nu.jixa.its.model.exception.RepositoryModelException;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "tblWorkItem")
public class WorkItem extends AbstractEntity<WorkItem> {

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  @Column(name = "completedDate")
  private Date completedDate;

  @Column(name = "description")
  private String description;

  @OneToOne(cascade = { CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE })
  @JoinColumn
  private Issue issue;

  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "workItems")
  private Collection<User> users;

  protected WorkItem() {
  }

  @Override public void copyFields(WorkItem other) {
    if (other.status != null) {
      setStatus(other.status);
    }
    this.description = other.description;
    this.issue = other.issue;
    this.users = other.users;
  }

  public WorkItem(Long number, Status status) {
    this.number = number;
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(@NotNull final Status status) {
    Util.throwExceptionIfArgIsNull(status, "status");
    this.status = status;
    if (status == Status.DONE) {
      completedDate = new Date(); // Gets initalized with the current date/time.
    }
  }

  public Date getCompletedDate() {
    return completedDate;
  }

  public Issue getIssue() {
    return issue;
  }

  public void setIssue(Issue issue) {
    this.issue = issue;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    Util.throwExceptionIfArgIsNull(description, "description");
    this.description = description;
  }

  public User addUser(User user) {
    if (users.contains(user)) {
      throw new RepositoryModelException("the user already exist");
    }
    return user;
  }

  public void setUsers(Collection<User> users) {
    this.users = users;
  }

  public Collection<User> getUsers() {
    return users;
  }

  public boolean hasUserWithTeam(Team team) {
    for(User user : users)
    {
      if(user.getTeam().getNumber() == team.getNumber())
      {
        return true;
      }
    }
    return false;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    WorkItem workItem = (WorkItem) o;

    if (!getNumber().equals(workItem.getNumber())) return false;
    if (getStatus() != workItem.getStatus()) return false;
    return !(getIssue() != null ? !getIssue().equals(workItem.getIssue())
        : workItem.getIssue() != null);
  }

  @Override public int hashCode() {
    int result = getNumber().hashCode();
    result = 31 * result + getStatus().hashCode();
    result = 31 * result + (getIssue() != null ? getIssue().hashCode() : 0);
    return result;
  }

}

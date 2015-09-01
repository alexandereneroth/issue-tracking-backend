package nu.jixa.its.model;

import com.sun.istack.internal.NotNull;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tblWorkItem")
public class WorkItem extends AbstractEntity {

  @Column(name = "number", unique = true, nullable = false)
  private Long number;

  @Enumerated // TODO add name after we know how it gets created in db
  @Column(name = "status", nullable = false)
  private Status status;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn
  private Issue issue;

  @ManyToMany(mappedBy = "workItems") // FIXME is this the right mapping?
      Collection<User> users;

  protected WorkItem() {
  }

  public WorkItem(Long number, Status status) {
    this.number = number;
    this.status = status;
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

  public Status getStatus() {
    return status;
  }

  public void setStatus(@NotNull final Status status) {
    if (number == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
    this.status = status;
  }

  public Issue getIssue() {
    return issue;
  }

  public void setIssue(Issue issue) {
    this.issue = issue;
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

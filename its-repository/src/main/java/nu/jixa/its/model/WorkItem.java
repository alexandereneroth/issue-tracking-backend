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
import nu.jixa.its.model.exception.RepositoryModelException;

@Entity
@Table(name = "tblWorkItem")
public class WorkItem extends AbstractEntity{

  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Enumerated // TODO add name after we know how it gets created in db
  private Status status;

  @Column(name = "description", unique = true, nullable = false)
  private String description;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn
  private Issue issue;

  @ManyToMany(mappedBy = "workItems") // FIXME is this the right mapping?
  Collection<User> users;

  public WorkItem(Long id, Status status) {
    this.id = id;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(@NotNull final Long id) {
    throwExceptionIfArgIsNull(id);
    this.id = id;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(@NotNull final Status status) {
    throwExceptionIfArgIsNull(status);
    this.status = status;
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
    throwExceptionIfArgIsNull(description);
    this.description = description;
  }

  private <T>void throwExceptionIfArgIsNull(T arg){
    if (arg == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
  }
}

package nu.jixa.its.model;

import com.sun.istack.internal.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tblIssue")
public class Issue extends AbstractEntity{
  @Column(name = "id", unique = true, nullable = false)
  private Long id;
  @Column(name = "string")
  private String string;

  public Issue(@NotNull final Long id) {

    if (id == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
    this.id = id;
  }

  public Issue(@NotNull final Long id, final String string) {
    if (id == null) {
      throw new RepositoryModelException("Null argument not allowed");
    }
    this.id = id;
    this.string = string;
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

  public String getString() {
    return string;
  }

  public void setString(final String string) {
    this.string = string;
  }
}

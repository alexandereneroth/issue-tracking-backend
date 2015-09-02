package nu.jixa.its.model;

import com.sun.istack.internal.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tblIssue")
public class Issue extends AbstractEntity {
  @Column(name = "number", unique = true, nullable = false)
  private Long number;
  @Column(name = "string")
  private String string;

  protected Issue() {
  }

  public Issue(@NotNull final Long number) {
    ModelUtil.throwExceptionIfArgIsNull(number, "number");
    this.number = number;
  }

  public Issue(@NotNull final Long number, final String string) {
    ModelUtil.throwExceptionIfArgIsNull(number, "number");
    this.number = number;
    this.string = string;
  }

  public Long getNumber() {
    return number;
  }

  public void setNumber(@NotNull final Long number) {
    ModelUtil.throwExceptionIfArgIsNull(number, "number");
    this.number = number;
  }

  public String getString() {
    return string;
  }

  public void setString(final String string) {
    this.string = string;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Issue issue = (Issue) o;

    return getNumber().equals(issue.getNumber());
  }

  @Override public int hashCode() {
    return getNumber().hashCode();
  }
}

package nu.jixa.its.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.internal.NotNull;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="number")
public abstract class AbstractEntity<T> {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "number", unique = true, nullable = false)
  protected Long number;

  protected AbstractEntity() {
  }

  public final Long getNumber() {
    return number;
  }

  public void setNumber(@NotNull final Long number) {
    ModelUtil.throwExceptionIfArgIsNull(number, "number");
    this.number = number;
  }

  public abstract void copyFields(T other);
}

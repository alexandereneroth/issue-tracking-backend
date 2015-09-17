package nu.jixa.its.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.internal.NotNull;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "number")
public abstract class AbstractEntity<T> {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "number", unique = true, nullable = false)
  protected Long number;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  @CreatedDate
  @Column(name = "createdDate")
  private Date createdDate;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  @LastModifiedDate
  @Column(name = "modifiedDate")
  private Date modifiedDate;

  protected AbstractEntity() {
  }

  public final Long getNumber() {
    return number;
  }

  public void setNumber(@NotNull final Long number) {
    Util.throwExceptionIfArgIsNull(number, "number");
    this.number = number;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public Date getModifiedDate() {
    return modifiedDate;
  }

  public abstract void copyFields(T other);
}

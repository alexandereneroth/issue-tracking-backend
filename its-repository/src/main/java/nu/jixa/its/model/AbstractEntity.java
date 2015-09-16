package nu.jixa.its.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.internal.NotNull;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "number")
public abstract class AbstractEntity<T> {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "number", unique = true, nullable = false)
  protected Long number;

/*  @JsonFormat(pattern = "yyyy-MM-dd HH:mm a")
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)*/
  @CreatedDate
  //@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
  @Column(name = "createdDate")
  private LocalDateTime createdDate;

/*  @JsonFormat(pattern = "yyyy-MM-dd HH:mm a")
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)*/
  @LastModifiedDate
  //@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
  @Column(name = "modifiedDate")
  private LocalDateTime modifiedDate;

  protected AbstractEntity() {
  }

  public final Long getNumber() {
    return number;
  }

  public void setNumber(@NotNull final Long number) {
    ModelUtil.throwExceptionIfArgIsNull(number, "number");
    this.number = number;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public LocalDateTime getModifiedDate() {
    return modifiedDate;
  }

  public abstract void copyFields(T other);
}

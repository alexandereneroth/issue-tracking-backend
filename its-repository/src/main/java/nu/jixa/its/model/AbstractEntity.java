package nu.jixa.its.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity {

  @Id
  @GeneratedValue
  private Long id;

  protected AbstractEntity() {
  }

  public final Long getId() {
    return id;
  }
}

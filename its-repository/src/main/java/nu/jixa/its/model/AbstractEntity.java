package nu.jixa.its.model;

import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.Id;

@MappedSuperclass
public class AbstractEntity {

  @Id
  @GeneratedValue
  private int dbId;

  protected AbstractEntity() {
  }

  public int getDbId() {
    return dbId;
  }
}

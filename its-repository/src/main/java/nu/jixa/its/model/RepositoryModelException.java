package nu.jixa.its.model;

public class RepositoryModelException extends RuntimeException {

  private static final long serialVersionUID = 3459677325772556212L;

  public RepositoryModelException(String message) {
    super(message);
  }

  public RepositoryModelException(String message, Throwable cause) {
    super(message, cause);
  }
}

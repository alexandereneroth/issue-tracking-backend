package nu.jixa.its.service;

public class ITSRepositoryException extends RuntimeException {

  private static final long serialVersionUID = 6644385975445649056L;

  public ITSRepositoryException(String message) {
    super(message);
  }

  public ITSRepositoryException(String message, Throwable cause) {
    super(message, cause);
  }
}

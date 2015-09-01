package nu.jixa.its.service;

public class ServiceException extends RuntimeException{
  private static final long serialVersionUID = 6644385975445649056L;

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}

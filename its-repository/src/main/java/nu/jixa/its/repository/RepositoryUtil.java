package nu.jixa.its.repository;

import nu.jixa.its.service.exception.ITSRepositoryException;

public final class RepositoryUtil {

  public static <T extends Object> void throwExceptionIfNull(T arg,
      String message) {
    if (arg == null) {
      throw new ITSRepositoryException(message);
    }
  }
}

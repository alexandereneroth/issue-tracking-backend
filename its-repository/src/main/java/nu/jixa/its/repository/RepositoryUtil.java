package nu.jixa.its.repository;

import nu.jixa.its.service.ITSRepositoryException;

/**
 * Created by gina on 2015-09-05.
 */
public final class RepositoryUtil {

  public static <T extends Object> void throwExceptionIfArgIsNullCustomMessage(T arg,
      String message) {
    if (arg == null) {
      throw new ITSRepositoryException(message);
    }
  }
}

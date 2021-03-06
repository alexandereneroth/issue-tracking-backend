package nu.jixa.its.model;

import java.util.Map;
import nu.jixa.its.model.exception.RepositoryModelException;

final class Util {

  public static <T extends Object> void throwExceptionIfArgIsNull(T arg, String name) {
    if (arg == null) {
      throw new RepositoryModelException("Null value not allowed on argument: " + name);
    }
  }

  public static void throwExceptionIfArgIsNull(Map<Object, String> argumentsWithNames) {
    for (Map.Entry<Object, String> entry : argumentsWithNames.entrySet()) {
      throwExceptionIfArgIsNull(entry.getKey(), entry.getValue());
    }
  }
}

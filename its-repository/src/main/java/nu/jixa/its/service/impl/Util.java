package nu.jixa.its.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import nu.jixa.its.service.exception.ITSRepositoryException;

final class Util {

  public static <T extends Object> void throwExceptionIfNull(T arg,
      String message) {
    if (arg == null) {
      throw new ITSRepositoryException(message);
    }
  }

  public static <T> ArrayList<T> iterableToArrayList(Iterable<T> iterable){

    ArrayList<T> arrayList = new ArrayList<>();

    for(T item : iterable)
    {
      arrayList.add(item);
    }

    return arrayList;
  }
}

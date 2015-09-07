package nu.jixa.its;

import com.sun.istack.internal.NotNull;
import java.util.Set;
import nu.jixa.its.model.User;

public final class HelperMethods {

  public static User generateSimpleUser(@NotNull final Long number) {
    return new User(number, "account" + number, "firstname" + number, "lastname" + number);
  }

  /**
   * Returns true if all elements in set1 is present in set2 and, vice versa.
   */
  public <T> boolean isEqualSet(Set<T> set1, Set<T> set2) {
    int matchesInColl1 = 0;
    for (T item : set1) {
      if (set2.contains(item)) {
        matchesInColl1 += 1;
      }
    }
    if (matchesInColl1 == set2.size()) {
      return true;
    }
    return false;
  }
}

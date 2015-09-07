package nu.jixa.its;

import com.sun.istack.internal.NotNull;
import java.util.HashSet;
import java.util.Set;
import nu.jixa.its.model.User;

public final class HelperMethods {

  public static User generateSimpleUser(@NotNull final Long number) {
    return new User(number, "account" + number, "firstname" + number, "lastname" + number);
  }

  /**
   * Returns true if all elements in set1 is present in set2 and, vice versa.
   */
  public static <T> boolean isEqualSet(Set<T> set1, Set<T> set2) {
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

  /**
   * Converts an iterable to a HashSet, if it only contains unique elements.
   * Otherwise throws IllegalArgumentException.
   * @param iterable
   * @param <T>
   * @return
   */
  public static <T> HashSet<T> toHashSet(Iterable<T> iterable) {
    HashSet<T> newSet = new HashSet<T>();
    for (T item : iterable) {
      if (newSet.add(item) == false) {
        throw new IllegalArgumentException(
            "Cannot convert iterable to set: iterable contains duplicate elements");
      }
    }
    return newSet;
  }

  public static <T> HashSet<T> newHashSetContaining(T[] items)
  {
    HashSet<T> hashSet = new HashSet<>();

    for(int i = 0; i < items.length; i++)
    {
      hashSet.add(items[i]);
    }
    return hashSet;
  }
}

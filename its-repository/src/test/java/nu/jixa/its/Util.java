package nu.jixa.its;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nu.jixa.its.model.Issue;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;

final class Util {

  public static User newSimpleUser(@NotNull final Long number) {
    return new User(number, "account" + number, "firstname" + number, "lastname" + number);
  }

  public static final Long USER_NUMBER = 100L;
  public static final Long WORKITEMNUMBER = 2012L;
  public static ArrayList<WorkItem> workItems3List;

  /**
   * Returns true if all elements in set1 is present in set2 and, vice versa.
   */
  public static <T> boolean isEqualSet(Set<T> set1, Set<T> set2) {
    int matchesInSet1 = 0;
    for (T item : set1) {
      if (set2.contains(item)) {
        matchesInSet1 += 1;
      }
    }
    return matchesInSet1 == set2.size() && set2.size() == set1.size();
  }

  /**
   * Converts an iterable to a HashSet, if it only contains unique elements. Otherwise throws
   * IllegalArgumentException.
   */
  public static <T> HashSet<T> toHashSet(Iterable<T> iterable) {
    HashSet<T> newSet = new HashSet<>();
    for (T item : iterable) {
      if (newSet.add(item) == false) {
        throw new IllegalArgumentException(
            "Cannot convert iterable to set: iterable contains duplicate elements");
      }
    }
    return newSet;
  }

  /**
   * Converts an array to a HashSet, if it only contains unique elements. Otherwise throws
   * IllegalArgumentException.
   */
  public static <T> HashSet<T> toHashSet(T[] items) {
    HashSet<T> newSet = new HashSet<>();
    for (T item : items) {
      if (newSet.add(item) == false) {
        throw new IllegalArgumentException(
            "Cannot convert array to set: array contains duplicate elements");
      }
    }
    return newSet;
  }

  /**
   * ------- WorkItem methods ------
   */

  public static List<WorkItem> generateComplexWorkItems() {
    ArrayList<WorkItem> list = new ArrayList<>();
    WorkItem workItem1 = generateSimpleWorkItem(WORKITEMNUMBER);
    workItem1.setDescription("item 1");
    workItem1.setIssue(new Issue(20L));
    workItem1.setStatus(Status.IN_PROGRESS);

    WorkItem workItem2 = generateSimpleWorkItem(14L);
    workItem2.setDescription("item 2");
    workItem2.setIssue(new Issue(22L));
    workItem2.setStatus(Status.IN_PROGRESS);

    WorkItem workItem3 = generateSimpleWorkItem(244L);
    workItem3.setDescription("item 4");
    workItem3.setIssue(new Issue(290L));
    workItem3.setStatus(Status.IN_PROGRESS);

    list.add(workItem1);
    list.add(workItem2);
    list.add(workItem3);
    workItems3List = list;
    return list;
  }

  public static WorkItem generateSimpleWorkItem(@NotNull final Long number) {
    Status status = Status.ON_BACKLOG;
    return new WorkItem(number, status);
  }

  public static Collection<User> generate3Users() {

    Collection<User> users = new ArrayList<>();
    users.add(newSimpleUser(USER_NUMBER));
    users.add(newSimpleUser(12L));
    users.add(newSimpleUser(14L));
    return users;
  }

  public static Date newDate(int year, int month, int day, int hour, int minute) {
    Calendar calendar2014 = new GregorianCalendar();
    calendar2014.set(year, month, day, hour, minute);
    return calendar2014.getTime();
  }
}

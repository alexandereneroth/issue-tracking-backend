package nu.jixa.its;

import com.sun.istack.internal.NotNull;
import nu.jixa.its.model.User;

public final class HelperMethods {

  public static User generateSimpleUser(@NotNull final Long number) {
    return new User(number, "account" + number, "firstname" + number, "lastname" + number);
  }
}

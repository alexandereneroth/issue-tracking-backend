package nu.jixa.its.web;

import java.util.HashMap;
import javax.security.auth.login.LoginException;
import nu.jixa.its.service.ITSRepository;
import org.springframework.beans.factory.annotation.Autowired;

public final class JixaAuthenticator {

  static {
    // TODO add security manager check
  }

  @Autowired
  private ITSRepository itsRepository;

  // No service key because its easier that way
  private HashMap<String, String> userNumberToAuthToken;

  //public void AuthToken login(String username, String password) throws LoginException {
  //
  //}
}

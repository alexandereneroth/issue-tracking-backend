package nu.jixa.its.web;

import java.util.HashMap;
import javax.security.auth.login.LoginException;
import nu.jixa.its.service.ITSRepository;
import org.springframework.beans.factory.annotation.Autowired;

public final class JixaAuthenticator {

  static {
    // TODO add security manager check
  }

  private static JixaAuthenticator INSTANCE;

  @Autowired
  private ITSRepository itsRepository;

  public static JixaAuthenticator getInstance(){
    if(INSTANCE == null){
      INSTANCE = new JixaAuthenticator();
    }
    return INSTANCE;
  }

  // No service key because its easier that way
  private HashMap<String, String> userNumberToAuthToken;

  public String login(String username, String password) throws LoginException {
    return ""; //TODO
  }

  public void logout(String authToken) {

  }

}

package nu.jixa.its.web;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.security.auth.login.LoginException;
import nu.jixa.its.model.User;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.exception.ITSRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class JixaAuthenticator {

  static {
    // TODO add security manager check
  }

  private static JixaAuthenticator INSTANCE;

  @Autowired
  private ITSRepository itsRepository;

  // An authentication token storage which stores <auth_token, username>.
  private final Map<String, String> authTokenStorage = new HashMap<>();

  public static JixaAuthenticator getInstance(){
    if(INSTANCE == null){
      INSTANCE = new JixaAuthenticator();
    }
    return INSTANCE;
  }

  public String login(String username, String password) throws LoginException {
      User userToLogin;

    try {
      userToLogin = itsRepository.getUser(username);
    } catch (ITSRepositoryException e) {
      throw new LoginException("Invalid username or password");
    }

    try {
      if (PasswordHash.validatePassword(password, userToLogin.getPassword())) {
        String authToken = UUID.randomUUID().toString();
        authTokenStorage.put( authToken, username );

        return authToken;
      }
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new LoginException("Error while validating password");
    }

    throw new LoginException("Invalid username or password");
  }


  public void logout(String authToken) {
    authTokenStorage.remove(authToken);
  }

  public boolean isAuthTokenValid(String authToken) {
    return authTokenStorage.containsValue(authToken);
  }
}

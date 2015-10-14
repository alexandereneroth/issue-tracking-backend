package nu.jixa.its.web;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.security.auth.login.LoginException;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.exception.ITSRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class JixaAuthenticator {

  @Autowired
  ITSRepository itsRepository;

  // An authentication token storage which stores <auth_token, username>.
  private final Map<String, String> authTokenStorage = new HashMap<>();

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
        authTokenStorage.put(authToken, username);

        return authToken;
      }
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new LoginException("Error while validating password");
    }

    throw new LoginException("Invalid username or password");
  }

  public void logout(String authToken) throws GeneralSecurityException {
    if (authTokenStorage.containsKey(authToken)) {
      authTokenStorage.remove(authToken);
    } else {
      throw new GeneralSecurityException("Invalid authorization token, logout not possible");
    }
  }

  public boolean isAuthTokenValid(String authToken) {
    return authTokenStorage.containsKey(authToken);
  }

  public boolean userIsLoggedIn(String username) {
    return authTokenStorage.containsValue(username);
  }

  public String getUserWithToken(String authToken) throws GeneralSecurityException {
    if (authTokenStorage.containsKey(authToken)) {
      return authTokenStorage.get(authToken);
    }
    throw new GeneralSecurityException("No user with this token logged in");
  }

  public boolean isAuthorizedToAccessWorkItem(final String authToken, final long workItemNumber) {
    User loggedInUser = getUserObjWithToken(authToken);
    if(loggedInUser.getTeam() == null){
      return false;
    }
    WorkItem workItem = itsRepository.getWorkItem(workItemNumber);
    return workItem.hasUserWithTeam(loggedInUser.getTeam());
  }

  public boolean isAuthorizedToAccessTeam(final String authToken, final long teamNumber) {
    User loggedInUser = getUserObjWithToken(authToken);
    if(loggedInUser.getTeam() == null){
      return false;
    }
    return loggedInUser.getTeam().getNumber() == teamNumber;
  }

  public boolean isAuthorizedToEditUser(final String authToken, final long userNumber) {
    User loggedInUser = getUserObjWithToken(authToken);
    return loggedInUser.getNumber() == userNumber;
  }

  public boolean isAuthorizedToViewUser(final String authToken, final long userNumber) {
    User loggedInUser = getUserObjWithToken(authToken);
    User checkedUser = itsRepository.getUser(userNumber);
    if(loggedInUser.getTeam() == null || checkedUser.getTeam() == null){
      return false;
    }

    return loggedInUser.getTeam().getNumber() == checkedUser.getTeam().getNumber();
  }

  private User getUserObjWithToken(final String authToken){
    String username =  authTokenStorage.get(authToken);
    return itsRepository.getUser(username);
  }

  public void setLoggedInUsersUsername(final String authToken, String newUsername)
      throws GeneralSecurityException {
    if(!authTokenStorage.containsKey(authToken)){
      throw new GeneralSecurityException("User not logged in");
    }
     authTokenStorage.put(authToken, newUsername);
  }
}

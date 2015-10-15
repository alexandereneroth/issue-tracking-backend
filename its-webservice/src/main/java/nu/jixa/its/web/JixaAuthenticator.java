package nu.jixa.its.web;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.security.auth.login.LoginException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
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
    if (loggedInUser.getTeam() == null) {
      return false;
    }
    WorkItem workItem = itsRepository.getWorkItem(workItemNumber);
    return workItem.hasUserWithTeam(loggedInUser.getTeam());
  }

  public boolean isAuthorizedToAccessTeam(final String authToken, final long teamNumber) {
    User loggedInUser = getUserObjWithToken(authToken);
    if (loggedInUser.getTeam() == null) {
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
    if (loggedInUser.getTeam() == null || checkedUser.getTeam() == null) {
      return false;
    }

    return loggedInUser.getTeam().getNumber() == checkedUser.getTeam().getNumber();
  }

  private User getUserObjWithToken(final String authToken) {
    String username = authTokenStorage.get(authToken);
    return itsRepository.getUser(username);
  }

  public void setLoggedInUsersUsername(final String authToken, String newUsername)
      throws GeneralSecurityException {
    if (!authTokenStorage.containsKey(authToken)) {
      throw new GeneralSecurityException("User not logged in");
    }
    authTokenStorage.put(authToken, newUsername);
  }

  /**
   * Makes sure the logged in user is authorized to access a team. Otherwise throws
   * <code>NotAuthorizedException</code>.
   *
   * @param httpHeaders The http headers containing the authorization token.
   * @param teamNumber The team number to check authorization against
   * @throws NotAuthorizedException If the user with the auth token is not authorized to access the
   * team
   */
  public void enforceTeamAccessRights(HttpHeaders httpHeaders, long teamNumber)
      throws NotAuthorizedException {
    String authToken = extractAuthorizationToken(httpHeaders);
    if (!isAuthorizedToAccessTeam(authToken, teamNumber)) {
      throw new NotAuthorizedException(Values.MSG_UNAUTHORIZED_RESPONSE);
    }
  }

  /**
   * @return The Authorization Token
   * @throws NotAuthorizedException If the header doesn't start with substr "Bearer "
   */
  private String extractAuthorizationToken(HttpHeaders httpHeaders) throws NotAuthorizedException {
    String authorizationHeader = httpHeaders.getHeaderString(Values.HEADER_NAME_AUTH_TOKEN);

    // Check if the HTTP Authorization header is present and formatted correctly
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      throw new NotAuthorizedException("Authorization header must be provided");
    }

    return authorizationHeader.substring("Bearer".length()).trim();
  }
}

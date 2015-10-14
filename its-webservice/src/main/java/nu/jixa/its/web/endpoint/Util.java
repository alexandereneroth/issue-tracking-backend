package nu.jixa.its.web.endpoint;

import javax.ws.rs.core.Response;

/**
 * Common utility class for REST endpoints.
 */
final class Util {

  public static final String BAD_REQUEST_NULL_OR_INVALID_MESSAGE =
      "Null or Invalid JSON Data in Request Body";
  public static final String MSG_INVALID_LOGIN_RESPONSE = "Invalid username and/or password";
  public static final String MSG_UNAUTHORIZED_RESPONSE = "You are not authorized to access this resource";
  public static final String MSG_UNAUTHORIZED = "Invalid username and/or password";
  public static final String MSG_ALREADY_LOGGED_IN = "Already logged in";

  /**
   * Returns false if the argument is null or an empty String, otherwise returns true.
   */
  public static <T> boolean queryEntered(T query) {
    if (query == null) {
      return false;
    }
    if (query instanceof String) {
      return !((String) query).isEmpty();
    }
    return true;
  }


  public static Response badRequestResponse() {
    return badRequestResponse(BAD_REQUEST_NULL_OR_INVALID_MESSAGE);
  }

  public static Response badRequestResponse(Exception e) {
    return badRequestResponse(e.getMessage());
  }

  public static Response badRequestResponse(String message) {
    return response(Response.Status.BAD_REQUEST, message);
  }

  public static Response response(Response.Status status, String message) {
    return Response.status(status).entity(message).build();
  }
}

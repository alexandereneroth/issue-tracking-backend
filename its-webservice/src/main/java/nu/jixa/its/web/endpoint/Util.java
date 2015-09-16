package nu.jixa.its.web.endpoint;

import javax.ws.rs.core.Response;

/**
 * Common utility class for REST endpoints.
 */
class Util {

  public static final String BAD_REQUEST_NULL_OR_INVALID =
      "Null or Invalid JSON Data in Request Body";

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
    return badRequestResponse(BAD_REQUEST_NULL_OR_INVALID);
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

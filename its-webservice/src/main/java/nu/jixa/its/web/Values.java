package nu.jixa.its.web;

public interface Values {

  String HEADER_NAME_AUTH_TOKEN = "Authorization";

  String BAD_REQUEST_NULL_OR_INVALID_MESSAGE =
      "Null or Invalid JSON Data in Request Body";
  String MSG_INVALID_LOGIN_RESPONSE = "Invalid username and/or password";
  String MSG_UNAUTHORIZED_RESPONSE = "You are not authorized to access this resource";
  String MSG_UNAUTHORIZED = "Invalid username and/or password";
  String MSG_ALREADY_LOGGED_IN = "Already logged in";
}

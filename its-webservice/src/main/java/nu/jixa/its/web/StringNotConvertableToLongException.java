package nu.jixa.its.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class StringNotConvertableToLongException extends WebApplicationException {
  private static final long serialVersionUID = 3899197009093505203L;

  public StringNotConvertableToLongException(String message) {
    super(Response.status(Status.BAD_REQUEST)
        .entity("String not convertable to long: " + message)
        .build());
  }
}


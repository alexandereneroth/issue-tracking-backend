package nu.jixa.its.web.endpoint;

import java.security.GeneralSecurityException;
import javax.security.auth.login.LoginException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.web.JixaAuthenticator;
import nu.jixa.its.web.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class RootEndpoint {

  @Autowired
  ITSRepository itsRepository;

  @Autowired
  JixaAuthenticator jixaAuthenticator;

  @POST
  @Path("login")
  public Response login(
      @Context HttpHeaders httpHeaders,
      @FormParam("username") final String username,
      @FormParam("password") final String password) {

    if (jixaAuthenticator.userIsLoggedIn(username)) {
      return Response.status(Status.BAD_REQUEST).entity(Util.MSG_ALREADY_LOGGED_IN_MESSAGE).build();
    }

    try {

      final String authToken = jixaAuthenticator.login(username, password);

      return Response.ok("{auth_token:" + authToken + "}").build();
    } catch (LoginException e) {
      return Response.status(Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_MESSAGE)
          .build();
    }
  }

  @POST
  @Path("logout")
  public Response logout(@Context HttpHeaders httpHeaders) {

    final String authToken = httpHeaders.getHeaderString(Values.HEADER_NAME_AUTH_TOKEN);

    try {
      jixaAuthenticator.logout(authToken);
    } catch (GeneralSecurityException e) {
      return Response.status(Status.UNAUTHORIZED)
          .entity(e.getMessage())
          .build();
    }

    return Response.noContent().build();
  }
}

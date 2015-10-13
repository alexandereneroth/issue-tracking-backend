package nu.jixa.its.web.endpoint;

import javax.security.auth.login.LoginException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.web.JixaAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RootEndpoint {

  @Autowired
  ITSRepository itsRepository;

  @POST
  @Path("/login")
  public Response login(final String username, final String password) {

    final JixaAuthenticator authenticator = JixaAuthenticator.getInstance();

    try {
      final String authToken = authenticator.login(username, password);

      return Response.ok("{auth_token:" + authToken + "}").build();
    } catch (LoginException e) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_RESPONSE)
          .build();
    }
  }

  @POST
  @Path("/logout")
  public Response logout(@Context HttpHeaders httpHeaders) {

    final JixaAuthenticator authenticator = JixaAuthenticator.getInstance();

    final String authToken = httpHeaders.getHeaderString(Util.HEADER_NAME_AUTH_TOKEN);
    authenticator.logout(authToken);

    return Response.noContent().build();
  }
}

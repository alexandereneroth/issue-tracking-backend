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
@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginEndpoint {

  @Autowired
  ITSRepository itsRepository;

  @POST // (login)
  public Response getAuthToken(@Context final HttpHeaders httpHeaders, final String username,
      final String password) {

    JixaAuthenticator authenticator = new JixaAuthenticator();

    try {
      String authToken = authenticator.login(username, password);

    }catch(LoginException e){
      Response 
    }
  }
}

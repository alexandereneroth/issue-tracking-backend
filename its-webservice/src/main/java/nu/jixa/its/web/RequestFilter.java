package nu.jixa.its.web;

import java.io.IOException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.springframework.beans.factory.annotation.Autowired;

@Provider
@PreMatching
public class RequestFilter implements ContainerRequestFilter {

  @Autowired
  JixaAuthenticator jixaAuthenticator;

  @Override
  public void filter( ContainerRequestContext requestContext )
      throws IOException {

    //Answer pre-flight tests before authorization check
    if (requestContext.getRequest().getMethod().equals("OPTIONS")) {
      requestContext.abortWith(Response.ok().build());
      return;
    }

  String requestPath = requestContext.getUriInfo().getPath();

    //Require authToken authorization on all paths except '/login'
    if(!requestPath.startsWith("login")){

      String authorizationHeader =
          requestContext.getHeaderString(Values.HEADER_NAME_AUTH_TOKEN);

      // Check if the HTTP Authorization header is present and formatted correctly
      if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        throw new NotAuthorizedException("Authorization header must be provided");
      }

      // Extract the token from the HTTP Authorization header
      String authToken = authorizationHeader.substring("Bearer".length()).trim();


      if(!jixaAuthenticator.isAuthTokenValid(authToken)){
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
      }

    }
  }

}
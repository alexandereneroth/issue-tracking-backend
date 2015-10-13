package nu.jixa.its.web;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

@PreMatching
public class RequestFilter implements ContainerRequestFilter {

  @Autowired
  JixaAuthenticator jixaAuthenticator;

  @Override
  public void filter( ContainerRequestContext requestContext ) throws IOException {

    //Answer pre-flight tests before authorization check
    if (requestContext.getRequest().getMethod().equals("OPTIONS")) {
      requestContext.abortWith(Response.ok().build());
      return;
    }

    String requestPath = requestContext.getUriInfo().getPath();

    //Require authToken authorization on all paths except '/login'
    if(!requestPath.startsWith("login")){

      String authToken = requestContext.getHeaderString(Values.HEADER_NAME_AUTH_TOKEN);

      if(!jixaAuthenticator.isAuthTokenValid(authToken)){
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
      }
    }
  }
}
package nu.jixa.its.web;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class CORSResponseFilter
    implements ContainerResponseFilter {

  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext)
      throws IOException {

    MultivaluedMap<String, Object> headers = responseContext.getHeaders();

    headers.add("Access-Control-Allow-Origin", "*");
    headers.add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
    headers.add("Access-Control-Allow-Credentials", "true");
    headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    headers.add("Access-Control-Max-Age", "1209600");
    headers.add("Cache-Control", "private, no-cache, no-store, must-revalidate");
    headers.add("Expires", "-1");
    headers.add("Pragma", "no-cache");

    //Answer pre-flight tests before authorization check
    if (requestContext.getRequest().getMethod().equals("OPTIONS")) {
      requestContext.abortWith(Response.ok().build());
      return;
    }

    String requestPath = requestContext.getUriInfo().getPath();

    //Require authToken authorization on all paths except '/login'
    if(!requestPath.startsWith("/login")){

      String authToken = requestContext.getHeaderString(Values.HEADER_NAME_AUTH_TOKEN);

      if(JixaAuthenticator.getInstance().isAuthTokenValid(authToken)){
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
      }
    }
  }
}
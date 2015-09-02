package nu.jixa.its.web.endpoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.jixa.its.web.MockService;
import nu.jixa.its.web.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersEndpoint {

  private MockService mockService;

  @Autowired
  public UsersEndpoint(MockService mockService) {
    this.mockService = mockService;
  }

  @GET
  @Path("{userId}")
  public Response getCustomer(@PathParam("userId") final int userId)
  {
    User user = mockService.getUserWithId(userId);
    return Response.ok(user).build();
  }
}

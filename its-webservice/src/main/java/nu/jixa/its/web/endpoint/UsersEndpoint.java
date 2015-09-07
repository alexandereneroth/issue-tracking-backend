package nu.jixa.its.web.endpoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.jixa.its.model.User;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.web.MockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersEndpoint {

  @Autowired
  private MockService mockService;

  @Autowired
  private ITSRepository itsRepository;

  @GET
  @Path("{userNumber}")
  public Response getCustomer(@PathParam("userNumber") final long userNumber)
  {
    User user = mockService.getUserWithId(userNumber);
    itsRepository.addUser(user);

    User repoUser = itsRepository.getUser(user.getNumber());
    return Response.ok(repoUser).build();
  }
}

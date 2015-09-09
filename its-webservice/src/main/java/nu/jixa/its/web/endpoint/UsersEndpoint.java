package nu.jixa.its.web.endpoint;

import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import nu.jixa.its.model.User;
import nu.jixa.its.service.ITSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersEndpoint {

  @Autowired
  private ITSRepository itsRepository;

  @Context
  private UriInfo uriInfo;

  //USER
  //✓User       | Uppdatera en User

  //✓User       | Söka efter en User baserat på förnamn eller efternamn eller användarnamn
  //✓UserTeam   | Hämta alla User som ingår i ett visst team


  //✓User       | Skapa en User
  @POST
  public Response createUser(final User user) {
    itsRepository.addUser(user);

    final URI location = uriInfo.getAbsolutePathBuilder().path(user.getNumber().toString()).build();
    return Response.created(location).build();
  }

  //✓User       | Hämta en User baserat på user number (inte entity id)
  @GET
  @Path("{userNumber}")
  public Response getUser(@PathParam("userNumber") final long userNumber)
  {
    User user = itsRepository.getUser(userNumber);
    return Response.ok(user).build();
  }

  //✓User       | Ta bort* en User
  @DELETE
  @Path("{userNumber}")
  public Response deleteUser(@PathParam("userNumber") final long userNumber) {
    itsRepository.deleteUser(userNumber);
    return Response.noContent().build();
  }



}

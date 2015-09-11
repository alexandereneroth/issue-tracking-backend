package nu.jixa.its.web.endpoint;

import java.net.URI;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.ITSRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersEndpoint {

  @Autowired
  private ITSRepository itsRepository;

  @Context
  private UriInfo uriInfo;

  private static final String NO_USER_WITH_USERNUMBER = "No user with Usernumber: ";
  private static final String BAD_REQUEST_NULL_OR_INVALID =
      "Null or Invalid JSON Data in Request Body";
  private static final String BAD_REQUEST_MISMATCH_BETWEEN_PATH_AND_USER =
      "Usernumber mismatch between path and new user info";

  //USER

  //✓UserTeam   | Hämta alla User som ingår i ett visst team

  /**
   * Defaults to empty searchString which will return all Users if no queryParam is entered.
   */
  @GET
  public Response getUsersByName(
      @QueryParam("filterByName") @DefaultValue("") final String searchString) {
    Collection<User> usersByName = itsRepository.getUsersByNameLike(searchString);
    if (usersByName.isEmpty()) {
      return Response.noContent().build();
    } else {
      return Response.ok(usersByName).build();
    }
  }

  @POST
  public Response createUser(final User user) throws IllegalAccessException {
    if (user == null) {
      return Response.status(Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    try {
      itsRepository.addUser(user);
    } catch (ITSRepositoryException e) {
      return Response.status(Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    final URI location = uriInfo.getAbsolutePathBuilder().path(user.getNumber().toString()).build();
    return Response.created(location).build();
  }

  @GET
  @Path("{userNumber}")
  public Response getUser(@PathParam("userNumber") final long userNumber) {
    try {
      User user = itsRepository.getUser(userNumber);
      return Response.ok(user).build();
    } catch (ITSRepositoryException e) {
      return Response.status(Status.NOT_FOUND)
          .entity(NO_USER_WITH_USERNUMBER + userNumber).build();
    }
  }

  @GET
  @Path("{userNumber}/work-items")
  public Response getWorkItemsForUser(
      @PathParam("userNumber") final long userNumber) {
    if (userNumber == 0) {
      return Response.status(Status.BAD_REQUEST).entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    try {
      Iterable<WorkItem> workItemsByUser = itsRepository.getWorkItemsByUser(userNumber);

      if (workItemsByUser.iterator().hasNext()) {
        return Response.ok(workItemsByUser).build();
      } else {
        return Response.noContent().build();
      }
    } catch (ITSRepositoryException e) {
      return Response.status(Status.BAD_REQUEST).build();
    }
  }

  @DELETE
  @Path("{userNumber}")
  public Response deleteUser(@PathParam("userNumber") final long userNumber) {
    try {
      itsRepository.deleteUser(userNumber);
    } catch (ITSRepositoryException e) {
      return Response.status(Status.NOT_FOUND)
          .entity(NO_USER_WITH_USERNUMBER + userNumber).build();
    }
    return Response.noContent().build();
  }

  @PUT
  @Path("{userNumber}")
  public Response updateUser(@PathParam("userNumber") final long userNumber,
      final User updatedUser) {
    if (updatedUser == null || updatedUser.getNumber() == null) {
      return Response.status(Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    try {
      if (userNumber == updatedUser.getNumber()) {
        itsRepository.updateUser(updatedUser);
        return Response.status(Status.NO_CONTENT).build();
      } else {
        return Response.status(Status.BAD_REQUEST)
            .entity(BAD_REQUEST_MISMATCH_BETWEEN_PATH_AND_USER).build();
      }
    } catch (ITSRepositoryException e) {
      return Response.status(Status.NOT_FOUND)
          .entity(NO_USER_WITH_USERNUMBER + userNumber).build();
    }
  }

  @PUT
  @Path("{userNumber}/work-items")
  public Response addWorkItemToUser(@PathParam("userNumber") final Long userNumber,
      final WorkItem workItemToAdd) {
    if (workItemToAdd == null) {
      return Response.status(Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    try {
      itsRepository.addWorkItemToUser(userNumber, workItemToAdd.getNumber());
      final URI location =
          uriInfo.getAbsolutePathBuilder().path(String.valueOf(userNumber)).build();
      return Response.created(location).build();
    } catch (ITSRepositoryException e) {
      return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
    }
  }
}

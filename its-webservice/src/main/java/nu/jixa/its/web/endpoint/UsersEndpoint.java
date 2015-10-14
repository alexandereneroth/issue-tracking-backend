package nu.jixa.its.web.endpoint;

import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.exception.ITSRepositoryException;
import nu.jixa.its.web.JixaAuthenticator;
import nu.jixa.its.web.PasswordHash;
import nu.jixa.its.web.StringNotConvertableToNumberWebApplicationException;
import nu.jixa.its.web.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersEndpoint {

  @Autowired
  private ITSRepository itsRepository;

  @Autowired
  private JixaAuthenticator authenticator;

  @Context
  private UriInfo uriInfo;

  private static final String NO_USER_WITH_USERNUMBER = "No user with Usernumber: ";
  private static final String BAD_REQUEST_NULL_OR_INVALID =
      "Null or Invalid JSON Data in Request Body";
  private static final String BAD_REQUEST_MISMATCH_BETWEEN_PATH_AND_USER =
      "Usernumber mismatch between path and new user info";

  /**
   * Defaults to empty searchString which will return all Users if no queryParam is entered.
   */
  @GET
  public Response getUsers(
      @QueryParam("username") @DefaultValue("") final String userName,
      @QueryParam("name_substring") @DefaultValue("") final String nameSubstringQuery,
      @QueryParam("page") @DefaultValue("") final String pageIndexQuery,
      @QueryParam("page_size") @DefaultValue("") final String pageSizeQuery) {
    if (Util.queryEntered(userName)) {
      try {
        User userResult = itsRepository.getUser(userName);
        return Response.ok(userResult).build();
      } catch (ITSRepositoryException e) {
        return Util.badRequestResponse(e);
      }
    }
    if (Util.queryEntered(nameSubstringQuery)) {
      Collection<User> usersByName = itsRepository.getUsersByNameLike(nameSubstringQuery);
      return Response.ok(usersByName).build();
    }
    if (Util.queryEntered(pageIndexQuery) && Util.queryEntered(pageSizeQuery)) {
      try {
        int pageIndexInt = Integer.parseInt(pageIndexQuery);
        int pageSizeInt = Integer.parseInt(pageSizeQuery);
        Collection<User> users = itsRepository.getUsers(pageIndexInt, pageSizeInt);
        return Response.ok(users).build();
      } catch (NumberFormatException e) {
        throw new StringNotConvertableToNumberWebApplicationException(
            pageIndexQuery + " or " + pageSizeQuery + " not convertable to number");
      } catch (ITSRepositoryException e) {
        return Util.badRequestResponse(e);
      }
    }
    return Response.status(Status.NOT_IMPLEMENTED).build();
  }

  @POST
  public Response createUser(@Context HttpHeaders httpHeaders, final User user)
      throws IllegalAccessException, InvalidKeySpecException, NoSuchAlgorithmException {
    if (user == null) {
      return Util.badRequestResponse(Util.BAD_REQUEST_NULL_OR_INVALID_MESSAGE);
    }
    if(user.getTeam() != null){
      String authToken = httpHeaders.getHeaderString(Values.HEADER_NAME_AUTH_TOKEN);
      if(!authenticator.isAuthorizedToAccessTeam(authToken, user.getTeam().getNumber())) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_RESPONSE).build();
      }
    }

    try {
      String unhashedPassword = user.getPassword();
      user.setPassword(PasswordHash.createHash(unhashedPassword));
      itsRepository.addUser(user);
    } catch (ITSRepositoryException e) {
      return Util.badRequestResponse(e);
    }
    final URI location = uriInfo.getAbsolutePathBuilder().path(user.getNumber().toString()).build();
    return Response.created(location).build();
  }

  @GET
  @Path("{userNumber}")
  public Response getUser(@Context HttpHeaders httpHeaders,
      @PathParam("userNumber") final long userNumber) {
    String authToken = httpHeaders.getHeaderString(Values.HEADER_NAME_AUTH_TOKEN);
    if(!authenticator.isAuthorizedToViewUser(authToken, userNumber)) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_RESPONSE).build();
    }

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
  public Response getWorkItemsForUser(@Context HttpHeaders httpHeaders,
      @PathParam("userNumber") final long userNumber) {
    if (userNumber == 0) {
      return Response.status(Status.BAD_REQUEST).entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    String authToken = httpHeaders.getHeaderString(Values.HEADER_NAME_AUTH_TOKEN);
    if(!authenticator.isAuthorizedToViewUser(authToken, userNumber)) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_RESPONSE).build();
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
  public Response deleteUser(@Context HttpHeaders httpHeaders,
      @PathParam("userNumber") final long userNumber) {
    String authToken = httpHeaders.getHeaderString(Values.HEADER_NAME_AUTH_TOKEN);
    if(!authenticator.isAuthorizedToEditUser(authToken, userNumber)) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_RESPONSE).build();
    }
    try {
      authenticator.logout(authToken);
      itsRepository.deleteUser(userNumber);
    } catch (ITSRepositoryException e) {
      return Response.status(Status.NOT_FOUND)
          .entity(NO_USER_WITH_USERNUMBER + userNumber).build();
    } catch (GeneralSecurityException e) {
      return Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity(e.getMessage() + "(this should not be possible! probable race condition!)").build();
    }
    return Response.noContent().build();
  }

  @PUT
  @Path("{userNumber}")
  public Response updateUser(@Context HttpHeaders httpHeaders,
      @PathParam("userNumber") final long userNumber,
      final User updatedUser) {
    if (updatedUser == null || updatedUser.getNumber() == null) {
      return Response.status(Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    String authToken = httpHeaders.getHeaderString(Values.HEADER_NAME_AUTH_TOKEN);
    if(!authenticator.isAuthorizedToEditUser(authToken, userNumber)) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_RESPONSE).build();
    }
    try {
      if (userNumber == updatedUser.getNumber()) {
        itsRepository.updateUser(updatedUser);
        if(updatedUser.getUsername() != null)
        {
          authenticator.setLoggedInUsersUsername(authToken, updatedUser.getUsername());
        }

        return Response.status(Status.NO_CONTENT).build();
      } else {
        return Response.status(Status.BAD_REQUEST)
            .entity(BAD_REQUEST_MISMATCH_BETWEEN_PATH_AND_USER).build();
      }
    } catch (ITSRepositoryException e) {
      return Response.status(Status.NOT_FOUND)
          .entity(NO_USER_WITH_USERNUMBER + userNumber).build();
    } catch (GeneralSecurityException e) {
      return Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity(e.getMessage() + "(this should not be possible! probable race condition!)").build();
    }
  }

  @PUT
  @Path("{userNumber}/work-items")
  public Response addWorkItemToUser(@Context HttpHeaders httpHeaders,
      @PathParam("userNumber") final Long userNumber,
      final WorkItem workItemToAdd) {
    if (workItemToAdd == null) {
      return Response.status(Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    String authToken = httpHeaders.getHeaderString(Values.HEADER_NAME_AUTH_TOKEN);
    if(!authenticator.isAuthorizedToAccessWorkItem(authToken, workItemToAdd.getNumber())) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_RESPONSE).build();
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

  @DELETE
  @Path("{userNumber}/work-items/{workItemNumber}")
  public Response removeWorkItemFromUser(@Context HttpHeaders httpHeaders,
      @PathParam("userNumber") final Long userNumber,
      @PathParam("workItemNumber") final Long workItemNumber) {
    String authToken = httpHeaders.getHeaderString(Values.HEADER_NAME_AUTH_TOKEN);
    if(!authenticator.isAuthorizedToAccessWorkItem(authToken, workItemNumber)) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_RESPONSE).build();
    }

    try {
      itsRepository.removeWorkItemFromUser(userNumber, workItemNumber);
      return Response.noContent().build();
    } catch (ITSRepositoryException e) {
      return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
    }
  }
}

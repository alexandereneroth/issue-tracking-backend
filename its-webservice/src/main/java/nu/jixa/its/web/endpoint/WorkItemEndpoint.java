package nu.jixa.its.web.endpoint;

import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
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
import javax.ws.rs.core.UriInfo;
import nu.jixa.its.model.Issue;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.exception.ITSRepositoryException;
import nu.jixa.its.web.JixaAuthenticator;
import nu.jixa.its.web.StringNotConvertableToNumberWebApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/work-items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WorkItemEndpoint {

  private static final String NO_WORKITEM_WITH_NUMBER = "No workItem with number: ";
  private static final String NO_MATCHES_FOR_QUERY = "The query gave no matches in the repository.";
  private static final String BAD_REQUEST_NULL_OR_INVALID =
      "Null or Invalid JSON Data in Request Body";
  private static final String BAD_REQUEST_MISMATCH_BETWEEN_PATH_AND_USER =
      "Usernumber mismatch between path and new user info";

  @Autowired
  private ITSRepository itsRepository;

  @Autowired
  private JixaAuthenticator authenticator;

  @Context
  private UriInfo uriInfo;

  @GET
  @Path("{workItemNumber}")
  public Response getWorkItem(@Context HttpHeaders httpHeaders,
      @PathParam("workItemNumber") final long workItemNumber) {
    String authToken = Util.extractAuthorizationToken(httpHeaders);
    if(!authenticator.isAuthorizedToAccessWorkItem(authToken, workItemNumber)) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_RESPONSE).build();
    }

    try {
      WorkItem workItem = itsRepository.getWorkItem(workItemNumber);
      return Response.ok(workItem).build();
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(NO_WORKITEM_WITH_NUMBER + workItemNumber).build();
    }
  }

  @GET
  public Response getWorkItems(
      @QueryParam("description_contains") @DefaultValue("") final String descriptionContainsQuery,
      @QueryParam("has_issue") @DefaultValue("") final String hasIssueQuery,
      @QueryParam("status") @DefaultValue("") final String statusQuery,
      @QueryParam("completed_time_from") @DefaultValue("") final String completedTimeFromQuery,
      @QueryParam("completed_time_to") @DefaultValue("") final String completedTimeToQuery,
      @QueryParam("page") @DefaultValue("") final String pageIndexQuery,
      @QueryParam("page_size") @DefaultValue("") final String pageSizeQuery) {

    if (Util.queryEntered(descriptionContainsQuery)) {
      return getByDescriptionContains(descriptionContainsQuery);
    }
    if (hasIssueQuery.equals("true")) {
      return getByIssue();
    }
    if (Util.queryEntered(statusQuery)) {
      return getByStatus(statusQuery);
    }
    if (Util.queryEntered(completedTimeFromQuery)
        && Util.queryEntered(completedTimeToQuery)) {
      return getByCompletedBetween(completedTimeFromQuery, completedTimeToQuery);
    }
    if (Util.queryEntered(pageIndexQuery) && Util.queryEntered(pageSizeQuery)) {
      return getPage(pageIndexQuery, pageSizeQuery);
    }

    return getAll();
  }

  @POST
  public Response createWorkItem(final WorkItem workItem) throws IllegalAccessException {
    if (workItem == null) {
      return Util.badRequestResponse();
    }

    try {
      itsRepository.addWorkItem(workItem);
    } catch (ITSRepositoryException e) {
      return Util.badRequestResponse();
    }

    final URI location =
        uriInfo.getAbsolutePathBuilder().path(workItem.getNumber().toString()).build();
    return Response.created(location).build();
  }

  @POST
  @Path("{workItemNumber}")
  public Response addIssueToWorkItem(@Context HttpHeaders httpHeaders,
      @PathParam("workItemNumber") final long workItemNumber,
      final Issue issue) {
    String authToken = Util.extractAuthorizationToken(httpHeaders);
    if(!authenticator.isAuthorizedToAccessWorkItem(authToken, workItemNumber)) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_RESPONSE).build();
    }

    WorkItem workItem;
    try {
      workItem = itsRepository.getWorkItem(workItemNumber);
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(NO_WORKITEM_WITH_NUMBER + workItemNumber).build();
    }
    workItem.setIssue(issue);
    try {
      itsRepository.updateWorkItem(workItem);
    } catch (ITSRepositoryException e) {
      return Util.badRequestResponse();
    }
    final URI location =
        uriInfo.getAbsolutePathBuilder().path(workItem.getNumber().toString()).build();
    return Response.created(location).build();
  }

  @PUT
  @Path("{workItemNumber}/status")
  public Response updateWorkItemStatus(@Context HttpHeaders httpHeaders,
      @PathParam("workItemNumber") final long workItemNumber,
      final String statusString) {
    String authToken = Util.extractAuthorizationToken(httpHeaders);
    if(!authenticator.isAuthorizedToAccessWorkItem(authToken, workItemNumber)) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_RESPONSE).build();
    }

    Status status;
    switch (statusString.toLowerCase()) {
      case "on_backlog":
        status = Status.ON_BACKLOG;
        break;
      case "in_progress":
        status = Status.IN_PROGRESS;
        break;
      case "done":
        status = Status.DONE;
        break;
      default:
        return Util.badRequestResponse();
    }

    try {
      itsRepository.setWorkItemStatus(workItemNumber, status);
      return Response.status(Response.Status.NO_CONTENT).build();
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(e.getMessage()).build();
    }
  }

  @DELETE
  @Path("{workItemNumber}")
  public Response deleteWorkItem(@Context HttpHeaders httpHeaders,
      @PathParam("workItemNumber") final long workItemNumber) {
    String authToken = Util.extractAuthorizationToken(httpHeaders);
    if(!authenticator.isAuthorizedToAccessWorkItem(authToken, workItemNumber)) {
      return Response.status(Response.Status.UNAUTHORIZED).entity(Util.MSG_UNAUTHORIZED_RESPONSE).build();
    }

    try {
      itsRepository.removeWorkItem(workItemNumber);
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(NO_WORKITEM_WITH_NUMBER + workItemNumber).build();
    }
    return Response.noContent().build();
  }

  private Response getByStatus(final String statusString) {
    Status status = getStatusByString(statusString);

    if (statusString.isEmpty() || status == null) {
      return Util.badRequestResponse();
    }

    Collection<WorkItem> workItems = itsRepository.getWorkItemsByStatus(status);
    if (workItems.isEmpty()) {
      return Response.noContent().build();
    } else {
      return Response.ok(workItems).build();
    }
  }

  private Response getByIssue() {
    Collection<WorkItem> workItems = itsRepository.getWorkItemsWithIssue();
    if (workItems.isEmpty()) {
      return Response.noContent().build();
    } else {
      return Response.ok(workItems).build();
    }
  }

  private Response getAll() {
    return Response.ok(itsRepository.getWorkItems()).build();
  }

  private Response getByDescriptionContains(final String descriptionSubstring) {
    try {
      Collection<WorkItem> workItems =
          itsRepository.getWorkItemsWithDescriptionLike(descriptionSubstring);
      return Response.ok(workItems).build();
    } catch (ITSRepositoryException e) {
      return Util.badRequestResponse();
    }
  }

  private Response getByCompletedBetween(String completedTimeFromString,
      String completedTimeToString) {
    try {

      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
      Date completedTimeFrom = formatter.parse(completedTimeFromString);
      Date completedTimeTo = formatter.parse(completedTimeToString);

      Collection<WorkItem> workItems =
          itsRepository.getWorkItemsCompletedBetween(completedTimeFrom, completedTimeTo);

      return Response.ok(workItems).build();
    } catch (ITSRepositoryException | ParseException e) {
      return Util.badRequestResponse(e);
    }
  }

  private Response getPage(String pageIndexQuery, String pageSizeQuery) {
    try {
      int pageIndexInt = Integer.parseInt(pageIndexQuery);
      int pageSizeInt = Integer.parseInt(pageSizeQuery);
      Collection<WorkItem> workItems = itsRepository.getWorkItemsPage(pageIndexInt, pageSizeInt);
      return Response.ok(workItems).build();
    } catch (NumberFormatException e) {
      throw new StringNotConvertableToNumberWebApplicationException(
          pageIndexQuery + " or " + pageSizeQuery + " not convertable to number");
    } catch (ITSRepositoryException e) {
      return Util.badRequestResponse(e);
    }
  }

  private Status getStatusByString(String statusString) {
    switch (statusString) {
      case "in_progress":
        return Status.IN_PROGRESS;
      case "on_backlog":
        return Status.ON_BACKLOG;
      case "done":
        return Status.DONE;
    }
    return null;
  }
}

package nu.jixa.its.web.endpoint;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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
import javax.ws.rs.core.UriInfo;
import nu.jixa.its.model.Issue;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.exception.ITSRepositoryException;
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
  private static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
  private static final String STATUS_ON_BACKLOG = "ON_BACKLOG";
  private static final String STATUS_DONE = "DONE";

  @Autowired
  private ITSRepository itsRepository;

  @Context
  private UriInfo uriInfo;

  @GET
  @Path("{workItemNumber}")
  public Response getWorkItem(@PathParam("workItemNumber") final long workItemNumber) {
    try {
      WorkItem workItem = itsRepository.getWorkItem(workItemNumber);
      return Response.ok(workItem).build();
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(NO_WORKITEM_WITH_NUMBER + workItemNumber).build();
    }
  }

  @GET
  public Response getWorkItemsByQuery(
      @QueryParam("description_contains") @DefaultValue("") final String descriptionContainsQuery,
      @QueryParam("has_issue") @DefaultValue("") final String hasIssueQuery,
      @QueryParam("status") @DefaultValue("") final String statusQuery,
      @QueryParam("completed_time_from") @DefaultValue("") String completedTimeFromQuery,
      @QueryParam("completed_time_to") @DefaultValue("") String completedTimeToQuery) {

    if (queryEntered(descriptionContainsQuery)) {
      return getByDescriptionContains(descriptionContainsQuery);
    }
    if (hasIssueQuery.equals("true")) {
      return getByIssue();
    }
    if (queryEntered(statusQuery)) {
      return getByStatus(statusQuery);
    }
    if (queryEntered(completedTimeFromQuery) && queryEntered(completedTimeToQuery)) {
      return getByCompletedBetween(completedTimeFromQuery, completedTimeToQuery);
    }
    // If no queryParam is entered return all WorkItems
    return getByDescriptionContains("");
  }

  private <T> boolean queryEntered(T query) {
    if (query == null) {
      return false;
    }
    if (query instanceof String) {
      return !((String) query).isEmpty();
    }
    return true;
  }

  @POST
  public Response createWorkItem(final WorkItem workItem) throws IllegalAccessException {
    if (workItem == null) {
      return badRequestResponse();
    }

    try {
      itsRepository.addWorkItem(workItem);
    } catch (ITSRepositoryException e) {
      return badRequestResponse();
    }

    final URI location =
        uriInfo.getAbsolutePathBuilder().path(workItem.getNumber().toString()).build();
    return Response.created(location).build();
  }

  @POST
  @Path("{workItemNumber}")
  public Response addIssueToWorkItem(@PathParam("workItemNumber") final long workItemNumber,
      final Issue issue) {
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
      return badRequestResponse();
    }
    final URI location =
        uriInfo.getAbsolutePathBuilder().path(workItem.getNumber().toString()).build();
    return Response.created(location).build();
  }

  @PUT
  @Path("{workItemNumber}/status")
  public Response updateWorkItemStatus(@PathParam("workItemNumber") final long workItemNumber,
      final String statusString) {

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
        return badRequestResponse();
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
  public Response deleteWorkItem(@PathParam("workItemNumber") final long workItemNumber) {
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
      return badRequestResponse();
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

  private Response getByDescriptionContains(final String descriptionSubstring) {
    try {
      Collection<WorkItem> workItems =
          itsRepository.getWorkItemsWithDescriptionLike(descriptionSubstring);
      return Response.ok(workItems).build();
    } catch (ITSRepositoryException e) {
      return badRequestResponse();
    }
  }

  private Response getByCompletedBetween(String completedTimeFromString,
      String completedTimeToString) {
    try {
      LocalDateTime completedTimeFrom = LocalDateTime.parse(completedTimeFromString);
      LocalDateTime completedTimeTo = LocalDateTime.parse(completedTimeToString);

      Collection<WorkItem> workItems =
          itsRepository.getWorkItemsCompletedBetween(completedTimeFrom, completedTimeTo);

      return Response.ok(workItems).build();

    } catch (ITSRepositoryException | DateTimeParseException e) {
      return badRequestResponse(e);
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

  private Response badRequestResponse() {
    return badRequestResponse(BAD_REQUEST_NULL_OR_INVALID);
  }

  private Response badRequestResponse(Exception e) {
    return badRequestResponse(e.getMessage());
  }

  private Response badRequestResponse(String message) {
    return response(Response.Status.BAD_REQUEST, message);
  }

  private Response response(Response.Status status, String message) {
    return Response.status(status).entity(message).build();
  }
}

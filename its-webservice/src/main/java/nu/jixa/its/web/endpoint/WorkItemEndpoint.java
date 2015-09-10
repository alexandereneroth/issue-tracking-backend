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
import javax.ws.rs.core.UriInfo;
import nu.jixa.its.model.Issue;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.ITSRepositoryException;
import nu.jixa.its.web.StringNotConvertableToLongException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/work_tems")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WorkItemEndpoint {

  @Autowired
  private ITSRepository itsRepository;

  @Context
  private UriInfo uriInfo;

  private static final String NO_WORKITEM_WITH_NUMBER = "No workItem with number: ";
  private static final String BAD_REQUEST_NULL_OR_INVALID =
      "Null or Invalid JSON Data in Request Body";
  private static final String BAD_REQUEST_MISMATCH_BETWEEN_PATH_AND_USER =
      "Usernumber mismatch between path and new user info";

  //✓WorkItem   | Skapa en work item
  @POST
  public Response createWorkItem(final WorkItem workItem) throws IllegalAccessException {

    if (workItem == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }

    try {
      itsRepository.addWorkItem(workItem);
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
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
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    final URI location =
        uriInfo.getAbsolutePathBuilder().path(workItem.getNumber().toString()).build();
    return Response.created(location).build();
  }

  //✓WorkItem   | Ändra status på en work item
  @PUT
  @Path("{workItemNumber}/status")
  public Response updateWorkItemStatus(@PathParam("workItemNumber") final String workItemNumber,
      final String status) {

    if (isInvalidStatus(status)) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    try {
      WorkItem workItem = itsRepository.getWorkItem(requireLongValue(workItemNumber));
      if (status.equals(STATUS_ON_BACKLOG)) {
        workItem.setStatus(Status.ON_BACKLOG);
      } else if (status.equals(STATUS_IN_PROGRESS)) {
        workItem.setStatus(Status.IN_PROGRESS);
      } else if (status.equals(STATUS_DONE)) {
        workItem.setStatus(Status.DONE);
      } else {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(BAD_REQUEST_NULL_OR_INVALID + status).build();
      }
      itsRepository.updateWorkItem(workItem);
      return Response.status(Response.Status.NO_CONTENT).build();
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(e.getMessage()).build();
    }
  }

  //✓WorkItem   | Ta bort* en work item
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

  //✓WorkItem   | Tilldela en work item till en User

  //✓WorkItem (run by itself)  | Söka efter work item som innehåller en viss text i sin beskrivning
  //✓WorkItem   | Hämta alla work item baserat på status
  //✓WorkItem   | Hämta alla work item för ett Team
  //✓WorkItem   | Hämta alla work item för en User
  //✓WorkItem   | Hämta alla work item som har en Issue

  @GET
  public Response getWorkItemsByQuery(@QueryParam("team") @DefaultValue("") final Long teamNumber,
      @QueryParam("description_contains") @DefaultValue("") final String descriptionSubstring,
      @QueryParam("user") @DefaultValue("") final Long userNumber,
      @QueryParam("has_issue") @DefaultValue("") final String hasIssue,
      @QueryParam("status") @DefaultValue(STATUS_IN_PROGRESS) final String statusString){

    if (teamNumber != null) {
      getByTeamQuery(teamNumber);
    }
    if (descriptionSubstring != null) {
      getByByDescriptionQuery(descriptionSubstring);
    }
    if (userNumber != null) {
      getByUserQuery(userNumber);
    }
    if (hasIssue != null) {
      getByIssueQuery(hasIssue);
    }
    if (statusString != null) {
      getStatusByString(statusString);
    }
    return Response.noContent().build();
  }

  /*
  @GET
  public Response getWorkItemsByStatus(
      @QueryParam("status") @DefaultValue(STATUS_IN_PROGRESS) final String statusString) {
    Status status = getStatusByString(statusString);
    if (statusString == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    Collection<WorkItem> workItems = itsRepository.getWorkItemsByStatus(status);
    if (workItems == null) {
      return Response.noContent().build();
    } else {
      return Response.ok(workItems).build();
    }
  }

  @GET
  public Response getWorkItemsByTeam(@QueryParam("team") @DefaultValue("") final Long teamNumber) {
    try {
      Collection<WorkItem> workItems = itsRepository.getWorkItemsByTeam(teamNumber);
      return Response.ok(workItems).build();
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
  }

  @GET
  public Response getWorkItemByDescription(
      @QueryParam("description_contains") @DefaultValue("") final String descriptionSubstring) {
    try {
      Collection<WorkItem> workItems =
          itsRepository.getWorkItemsWithDescriptionLike(descriptionSubstring);
      return Response.ok(workItems).build();
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
  }

  @GET
  public Response getWorkItemsByUser(@QueryParam("user") @DefaultValue("") final Long userNumber) {
    try {
      Collection<WorkItem> workItems = itsRepository.getWorkItemsByUser(userNumber);
      return Response.ok(workItems).build();
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
  }

  @GET
  public Response getWorkItemsByIssue(
      @QueryParam("has_issue") @DefaultValue("") final String hasIssue) {
    if (hasIssue == "true") {
      Collection<WorkItem> workItems = itsRepository.getWorkItemsWithIssue();
      if (workItems != null) {
        return Response.ok(workItems).build();
      } else {
        return Response.status(Response.Status.NOT_FOUND)
            .entity("No workItems with issue").build();
      }
    } else {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
  }
*/
  private static final String STATUS_IN_PROGRESS = "in_progress";
  private static final String STATUS_ON_BACKLOG = "on_backlog";
  private static final String STATUS_DONE = "done";

    private Response getByStatusQuery(final String statusString) {
    Status status = getStatusByString(statusString);
    if (statusString == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    Collection<WorkItem> workItems = itsRepository.getWorkItemsByStatus(status);
    if (workItems == null) {
      return Response.noContent().build();
    } else {
      return Response.ok(workItems).build();
    }
  }

  private Response getByIssueQuery(String hasIssue) {
    if (hasIssue == "true") {
      Collection<WorkItem> workItems = itsRepository.getWorkItemsWithIssue();
      if (workItems != null) {
        return Response.ok(workItems).build();
      } else {
        return Response.status(Response.Status.NOT_FOUND)
            .entity("No workItems with issue").build();
      }
    } else {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
  }

  private Response getByUserQuery(Long userNumber) {
    try {
      Collection<WorkItem> workItems = itsRepository.getWorkItemsByUser(userNumber);
      return Response.ok(workItems).build();
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
  }

  private Response getByByDescriptionQuery(final String descriptionSubstring) {
    try {
      Collection<WorkItem> workItems =
          itsRepository.getWorkItemsWithDescriptionLike(descriptionSubstring);
      return Response.ok(workItems).build();
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
  }

  private Response getByTeamQuery(Long teamNumber) {
    try {
      Collection<WorkItem> workItems = itsRepository.getWorkItemsByTeam(teamNumber);
      return Response.ok(workItems).build();
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
  }

 /* @GET
  public Response getUsers(
=======
 /* public Response getUsers(
>>>>>>> 33b5c0332515cd71d2a2d8d2ffcec22311d53b59
      @QueryParam("team") @DefaultValue("") final String teamNumber,
      @QueryParam("user") @DefaultValue("") final String userNumber,
      @QueryParam("status") @DefaultValue(STATUS_IN_PROGRESS) final String status,
      @QueryParam("description_contains") @DefaultValue("") final String descriptionSubstring,
      @QueryParam("has_issue") @DefaultValue("") final String hasIssue) {

    //Collection<User> usersByName = itsRepository.getUsersByNameLike(searchString);
    //if (usersByName.isEmpty()) {
    //  return Response.noContent().build();
    //} else {
    //  return Response.ok(usersByName).build();
    //}
  }*/

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
  //✓WorkItem   | Skapa en Issue
  //✓WorkItem   | Uppdatera en Issue
  //✓WorkItem   | Lägga till en Issue till en work item

  private boolean isInvalidStatus(String status) {
    if (status == null || status.equals("")) {
      return true;
    }
    if (status.equals(STATUS_IN_PROGRESS)
        || status.equals(STATUS_ON_BACKLOG)
        || status.equals(STATUS_DONE)) {
      return false;
    }
    return true;
  }

  private Long requireLongValue(String stringVal) {
    Long longVal;
    try {
      longVal = Long.valueOf(stringVal);
    } catch (NumberFormatException e) {
      throw new StringNotConvertableToLongException(stringVal);
    }
    return longVal;
  }
}

package nu.jixa.its.web.endpoint;

import java.net.URI;
import java.util.Collection;
import javax.ws.rs.Consumes;
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
import nu.jixa.its.service.ITSRepository;
import nu.jixa.its.service.exception.ITSRepositoryException;
import nu.jixa.its.web.StringNotConvertableToNumberWebApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IssueEndpoint {

  @Autowired
  private ITSRepository itsRepository;

  @Context
  private UriInfo uriInfo;

  private static final String NO_ISSUE_WITH_ISSUENUMBER = "No issue with issuenumber: ";
  private static final String BAD_REQUEST_NULL_OR_INVALID =
      "Null or Invalid JSON Data in Request Body";
  private static final String BAD_REQUEST_MISMATCH_BETWEEN_PATH_AND_ISSUE =
      "Usernumber mismatch between path and new issue info";

  @POST
  public Response createIssue(final Issue issue) throws IllegalAccessException {
    if (issue == null) {
      return Util.badRequestResponse(Util.BAD_REQUEST_NULL_OR_INVALID);
    }
    try {
      itsRepository.addIssue(issue);
    } catch (ITSRepositoryException e) {
      return Util.badRequestResponse(e);
    }
    final URI location =
        uriInfo.getAbsolutePathBuilder().path(issue.getNumber().toString()).build();
    return Response.created(location).build();
  }

  @PUT
  @Path("{issueNumber}")
  public Response updateIssue(@PathParam("issueNumber") final long issueNumber,
      final Issue updatedIssue) {
    if (updatedIssue == null || updatedIssue.getNumber() == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(BAD_REQUEST_NULL_OR_INVALID).build();
    }
    try {
      if (issueNumber == updatedIssue.getNumber()) {
        itsRepository.updateIssue(updatedIssue);
        return Response.status(Response.Status.NO_CONTENT).build();
      } else {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(BAD_REQUEST_MISMATCH_BETWEEN_PATH_AND_ISSUE).build();
      }
    } catch (ITSRepositoryException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(NO_ISSUE_WITH_ISSUENUMBER + issueNumber).build();
    }
  }

  @GET
  public Response getIssues(@QueryParam("page") @DefaultValue("") final String page,
      @QueryParam("page_size") @DefaultValue("") final String pageSize) {
    if (Util.queryEntered(page) && Util.queryEntered(pageSize)) {
      try {
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        Collection<Issue> issues = itsRepository.getIssues(pageInt, pageSizeInt);
        return Response.ok(issues).build();
      } catch (NumberFormatException e) {
        throw new StringNotConvertableToNumberWebApplicationException(
            page + " or " + pageSize + " not convertable to number");
      } catch (ITSRepositoryException e) {
        return Util.badRequestResponse(e);
      }
    }

    return Response.status(Response.Status.NOT_IMPLEMENTED).build();
  }
}

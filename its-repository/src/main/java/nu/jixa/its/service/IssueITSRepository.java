package nu.jixa.its.service;

import java.util.Collection;
import nu.jixa.its.model.Issue;
import nu.jixa.its.model.WorkItem;

public interface IssueITSRepository {

  Issue addIssue(Issue issue);

  Issue getIssue(Long issueNumber);

  Collection<Issue> getIssuesPage(int page, int pageSize);

  WorkItem addIssueToWorkItem(Long workItemNumber, Long IssueNumber);

  void saveIssue(Issue issue);

  Issue updateIssue(Issue issue);

  boolean issueExists(Issue issue);

}

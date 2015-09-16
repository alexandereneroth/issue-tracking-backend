package nu.jixa.its.service.impl;

import nu.jixa.its.model.Issue;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.repository.IssueRepository;
import nu.jixa.its.repository.RepositoryUtil;
import nu.jixa.its.service.exception.ITSRepositoryException;
import nu.jixa.its.service.IssueITSRepository;
import nu.jixa.its.service.WorkItemITSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class IssueITSRepositoryImpl implements IssueITSRepository {

  @Autowired
  private IssueRepository issueRepository;
  
  @Autowired
  private WorkItemITSRepository workItemITSRepository;

  @Override
  public void saveIssue(Issue issue) {
    if (issueExists(issue)) {
      updateIssue(issue);
    } else {
      addIssue(issue);
    }
  }

  @Override
  public Issue updateIssue(Issue issue) {
    Issue issueInRepository = issueRepository.findByNumber(issue.getNumber());

    RepositoryUtil.throwExceptionIfNull(issueInRepository,
        "Could not update issue: issue doesn't exist");
    issueInRepository.copyFields(issue);
    try {
      return issueRepository.save(issueInRepository);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not save issue", e);
    }
  }

  @Override
  public Issue addIssue(Issue issue) {
    if (issueExists(issue)) {
      throw new ITSRepositoryException("Could not add issue: issue already exists");
    }
    try {
      return issueRepository.save(issue);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not save issue", e);
    }
  }

  @Override
  public Issue getIssue(Long issueNumber) {
    Issue issueInRepository = issueRepository.findByNumber(issueNumber);
    if (issueInRepository == null) {
      throw new ITSRepositoryException("Could not get issue: issue not in repository");
    }
    return issueInRepository;
  }

  @Override
  public WorkItem addIssueToWorkItem(Long workItemNumber, Long issueNumber) {
    Issue issue = getIssue(issueNumber);
    RepositoryUtil.throwExceptionIfNull(issue,
        "Could not add issue to workitem: issue with number " + issueNumber + " doesn't exist");
    WorkItem workItem = workItemITSRepository.getWorkItem(workItemNumber);
    RepositoryUtil.throwExceptionIfNull(workItem,
        "Could not add issue to workitem: workItem with number " + workItemNumber + " doesn't exist");
    workItem.setIssue(issue);
    try {
      return workItemITSRepository.updateWorkItem(workItem);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not add update workItem", e);
    }
  }

  @Override
  public boolean issueExists(Issue issue) {
    Issue issueInRepository = issueRepository.findByNumber(issue.getNumber());
    return issueInRepository != null;
  }

}

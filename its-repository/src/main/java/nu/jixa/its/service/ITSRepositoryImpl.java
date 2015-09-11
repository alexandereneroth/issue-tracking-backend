package nu.jixa.its.service;

import java.util.Collection;
import java.util.Iterator;

import nu.jixa.its.model.Issue;
import nu.jixa.its.model.Status;
import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;
import nu.jixa.its.model.WorkItem;
import nu.jixa.its.repository.IssueRepository;
import nu.jixa.its.repository.RepositoryUtil;
import nu.jixa.its.repository.TeamRepository;
import nu.jixa.its.repository.UserRepository;
import nu.jixa.its.repository.WorkItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

public class ITSRepositoryImpl implements ITSRepository {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkItemRepository workItemRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private IssueRepository issueRepository;

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
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(issueInRepository,
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
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(issue,
                "Could not add issue: issue with issueNR" + issueNumber + "doesn't exist");
        WorkItem workItem = getWorkItem(workItemNumber);
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(workItem,
                "Could not add workItem: workItem with workNR" + workItemNumber + "doesn't exist");
        workItem.setIssue(issue);
        try {
            return updateWorkItem(workItem);
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not add update workItem", e);
        }
    }

    @Override
    public boolean issueExists(Issue issue) {
        Issue issueInRepository = issueRepository.findByNumber(issue.getNumber());
        if (issueInRepository == null) {
            return false;
        }
        return true;
    }

    @Override
    public WorkItem updateWorkItem(WorkItem updatedWorkItem) {
        WorkItem workItemFromRepository = getWorkItem(updatedWorkItem.getNumber());
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(workItemFromRepository,
                "Could not add workItem: workItem with workNR"
                        + updatedWorkItem.getNumber()
                        + "doesn't exist");
        Issue workItemIssue = updatedWorkItem.getIssue();
        if (workItemIssue != null) {
            try {
                saveIssue(workItemIssue);
            } catch (DataIntegrityViolationException e) {
                throw new ITSRepositoryException("Could not update issue", e);
            }
        }
        workItemFromRepository.copyFields(updatedWorkItem);
        try {
            return workItemRepository.save(workItemFromRepository);
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not save user", e);
        }
    }

    @Transactional
    @Override
    public WorkItem addWorkItem(WorkItem workItem) {
        try {
            return workItemRepository.save(workItem);
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not add workItem", e);
        }
    }

    @Transactional
    @Override
    public WorkItem removeWorkItem(Long workItemNumber) {
        WorkItem deleteItem = findByNumber(workItemNumber);
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(deleteItem,
                "Could notdelete workItem: workItem with workNR"
                        + deleteItem.getNumber()
                        + "doesn't exist");
        if (deleteItem.getUsers().size() > 0) {
            removeWorkItemFromItsUsers(deleteItem);
        }
        try {
            workItemRepository.delete(deleteItem);
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not delete workItem", e);
        }
        return deleteItem;
    }

    private void removeWorkItemFromItsUsers(WorkItem workItem) {
        WorkItem workItemFromDB = workItemRepository.findByNumber(workItem.getNumber());
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(workItemFromDB,
                "Could not remove users: workItem with workNR"
                        + workItemFromDB.getNumber()
                        + "doesn't exist");
        Iterator<User> userIterator = workItem.getUsers().iterator();
        while (userIterator.hasNext()) {
            User userToRemoveWorkItemFrom = userIterator.next();
            userToRemoveWorkItemFrom.getWorkItems().remove(workItem);
            try {
                userRepository.save(userToRemoveWorkItemFrom);
            } catch (DataIntegrityViolationException e) {
                throw new ITSRepositoryException("Could not save user", e);
            }
            userIterator.remove();
        }
        workItemRepository.save(workItem);
    }

    @Override
    public WorkItem getWorkItem(Long workItemNumber) {
        WorkItem workItemInDB = workItemRepository.findByNumber(workItemNumber);
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(workItemInDB,
                "Could not find workItem: No item with nr " + workItemNumber);
        return workItemRepository.findByNumber(workItemNumber);
    }

    @Transactional
    @Override
    public void setWorkItemStatus(Long workItemNumber, Status status) {
        WorkItem item = workItemRepository.findByNumber(workItemNumber);
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(item,
                "Could not find workItem: No item with nr " + workItemNumber);
        item.setStatus(status);
        try {
            workItemRepository.save(item);
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not save user", e);
        }
    }

    @Override
    public Collection<WorkItem> getWorkItemsByStatus(Status status) {
        return workItemRepository.findByStatus(status);
    }

    @Override
    public Collection<WorkItem> getWorkItemsByTeam(Long teamNumber) {
        return workItemRepository.findByUsersTeamNumber(teamNumber);
    }

    @Override
    public Collection<WorkItem> getWorkItemsByUser(Long userNumber) {
        return workItemRepository.findByUsersNumber(userNumber);
    }

    @Override
    public Collection<WorkItem> getWorkItemsWithIssue() {
        return workItemRepository.findAllWorkItemsWithIssue();
    }

    @Override
    public Collection<WorkItem> getWorkItemsWithDescriptionLike(String descriptionLike) {
        return workItemRepository.findWorkItemsWithDescriptionLike(descriptionLike);
    }

    @Transactional
    @Override
    public User addUser(User user) {
        try {
            workItemRepository.save(user.getWorkItems());
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not add user", e);
        }
        return user;
    }

    @Transactional
    @Override
    public User updateUser(User user) {
        User userInRepo = getUser(user.getNumber());
        userInRepo.copyFields(user);
        try {
            User savedUser = userRepository.save(userInRepo);
            return savedUser;
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not add user", e);
        }
    }

    @Override
    public User deleteUser(Long userNumber) {
        User deletedUser = userRepository.findByNumber(userNumber);
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(deletedUser,
                "Could not delete User: No user with number " + userNumber);
        if (deletedUser.getTeam() != null) {
            // Gotcha: References from other objects need to be cleared and saved to the database
            // before removal
            removeUserFromItsTeam(deletedUser);
        }
        if (deletedUser.getWorkItems().size() > 0) {
            removeUserFromItsWorkItems(deletedUser);
        }
        try {
            userRepository.delete(deletedUser);
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not add user", e);
        }
        return deletedUser;
    }

    private void removeUserFromItsTeam(User leavingUser) {
        Team leavingUsersTeam = leavingUser.getTeam();
        leavingUser.leaveTeam();
        try {
            teamRepository.save(leavingUsersTeam);
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not update team", e);
        }
        try {
            userRepository.save(leavingUser);
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not update user", e);
        }
    }

    private void removeUserFromItsWorkItems(User userToRemove) {
        Iterator<WorkItem> workItemIterator = userToRemove.getWorkItems().iterator();
        while (workItemIterator.hasNext()) {
            WorkItem workItemToRemoveUserFrom = workItemIterator.next();
            workItemToRemoveUserFrom.getUsers().remove(userToRemove);
            try {
                workItemRepository.save(workItemToRemoveUserFrom);
            } catch (DataIntegrityViolationException e) {
                throw new ITSRepositoryException("Could not update workItem", e);
            }
            workItemIterator.remove();
        }
        try {
            userRepository.save(userToRemove);
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not update user", e);
        }
    }

    @Override
    public User getUser(Long userNumber) {
        User gotUser = userRepository.findByNumber(userNumber);
        if (gotUser == null) {
            throw new ITSRepositoryException("Could not get User: No user with id " + userNumber);
        }
        return gotUser;
    }

    @Override
    public Iterable<User> getUsersByTeam(Long teamNumber) {
        return userRepository.selectByTeamId(teamNumber);
    }

    @Override
    public Collection<User> getUsersByNameLike(String nameLike) {
        return userRepository.selectByNameLike(nameLike);
    }

    @Override
    public void addWorkItemToUser(Long userId, Long workItemId) {

        WorkItem item = getWorkItem(workItemId);
        User user = getUser(userId);
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(item,
                "Could not find workItem: No workItem with number " + workItemId);
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(user,
                "Could not find user: No user with number " + userId);
        user.addWorkItem(item);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not save user", e);
        }
    }

    @Override
    public Team addTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public void updateTeam(Team team) {
        Team teamInRepo = teamRepository.findByNumber(team.getNumber());
        teamInRepo.copyFields(team);
        teamRepository.save(teamInRepo);
    }

    @Override
    public Team deleteTeam(Team team) {
        return deleteTeam(team.getNumber());
    }

    @Override
    public Team deleteTeam(Long teamNumber) {
        Team teamToDelete = teamRepository.findByNumber(teamNumber);
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(teamToDelete,
                "Could not delete Team: No team with number " + teamNumber);

        Iterator<User> usersIterator = teamToDelete.getUsers().iterator();
        while (usersIterator.hasNext()) {
            User userToRemoveFromTeam = usersIterator.next();
            usersIterator.remove();
            userToRemoveFromTeam.leaveTeam();
            try {
                userRepository.save(userToRemoveFromTeam);
            } catch (DataIntegrityViolationException e) {
                throw new ITSRepositoryException("Could not update user", e);
            }
        }
        teamRepository.delete(teamToDelete);
        return teamToDelete;
    }

    @Override
    public Team getTeam(Long teamNumber) {
        Team team = teamRepository.findByNumber(teamNumber);
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(team,
                "Could not find Team: No team with number " + teamNumber);
        return team;
    }

    @Override
    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public User addUserToTeamWithNumber(Long userNumber, Long teamNumber) {

        User user = getUser(userNumber);
        Team team = getTeam(teamNumber);
        user.joinTeam(team);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ITSRepositoryException("Could not save user", e);
        }
        return user;
    }

    @Override
    public WorkItem findByNumber(Long workItemNr) {
        WorkItem item = workItemRepository.findByNumber(workItemNr);
        RepositoryUtil.throwExceptionIfArgIsNullCustomMessage(item,
                "Could not find User: No user with id" + workItemNr);
        return item;
    }
}

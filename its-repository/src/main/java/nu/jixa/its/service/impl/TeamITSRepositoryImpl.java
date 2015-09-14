package nu.jixa.its.service.impl;

import java.util.Iterator;
import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;
import nu.jixa.its.repository.RepositoryUtil;
import nu.jixa.its.repository.TeamRepository;
import nu.jixa.its.repository.UserRepository;
import nu.jixa.its.service.TeamITSRepository;
import nu.jixa.its.service.UserITSRepository;
import nu.jixa.its.service.exception.ITSRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class TeamITSRepositoryImpl implements TeamITSRepository{

  @Autowired
  TeamRepository teamRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserITSRepository userITSRepository;

  @Override
  public Team addTeam(Team team) {
    return teamRepository.save(team);
  }

  @Override
  public void updateTeam(Team updatedTeam) {
    Team outdatedTeam = teamRepository.findByNumber(updatedTeam.getNumber());

    updateUsersTeamMembership(updatedTeam, outdatedTeam);

    outdatedTeam.copyFields(updatedTeam);
    teamRepository.save(outdatedTeam);
  }

  private void updateUsersTeamMembership(Team updatedTeam, Team outdatedTeam){
    for(User userOutdated : outdatedTeam.getUsers())
    {
      if(userNotInTeam(userOutdated,updatedTeam))
      {
        userOutdated.setTeam(null);
        userITSRepository.updateUser(userOutdated);
      }
    }
    for(User userUpdated : updatedTeam.getUsers())
    {
      if(userNotInTeam(userUpdated, outdatedTeam))
      {
        userUpdated.setTeam(updatedTeam);
        userITSRepository.updateUser(userUpdated);
      }
    }
  }

  private boolean userNotInTeam(User user, Team team){
    return !team.getUsers().contains(user);
  }

  @Override
  public Team deleteTeam(Team team) {
    return deleteTeam(team.getNumber());
  }

  @Override
  public Team deleteTeam(Long teamNumber) {
    Team teamToDelete = teamRepository.findByNumber(teamNumber);
    RepositoryUtil.throwExceptionIfNull(teamToDelete,
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
    RepositoryUtil.throwExceptionIfNull(team,
        "Could not find Team: No team with number " + teamNumber);
    return team;
  }

  @Override
  public Iterable<Team> getAllTeams() {
    return teamRepository.findAll();
  }

  @Override
  public User addUserToTeamWithNumber(Long userNumber, Long teamNumber) {

    User user = userITSRepository.getUser(userNumber);
    Team team = getTeam(teamNumber);
    user.joinTeam(team);
    try {
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new ITSRepositoryException("Could not save user", e);
    }
    return user;
  }

}

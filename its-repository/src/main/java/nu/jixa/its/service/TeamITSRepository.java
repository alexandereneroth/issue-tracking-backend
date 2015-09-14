package nu.jixa.its.service;

import nu.jixa.its.model.Team;
import nu.jixa.its.model.User;

public interface TeamITSRepository {

  Team addTeam(Team team);

  void updateTeam(Team team);

  Team deleteTeam(Long teamNumber);

  Team deleteTeam(Team testTeam);

  Team getTeam(Long teamNumber);

  Iterable<Team> getAllTeams();

  User addUserToTeamWithNumber(Long userNumber, Long teamNumber);
}

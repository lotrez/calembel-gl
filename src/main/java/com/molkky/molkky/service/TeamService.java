package com.molkky.molkky.service;

import com.molkky.molkky.domain.*;
import com.molkky.molkky.model.AddPlayerModel;
import com.molkky.molkky.model.AddPlayerlistModel;
import com.molkky.molkky.model.CreateTeamModel;
import com.molkky.molkky.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import type.UserRole;

import java.util.ArrayList;
import java.util.List;

import static com.molkky.molkky.utility.StringUtilities.createCode;


@Service
public class TeamService {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTournamentRoleRepository userTournamentRoleRepository;

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    ClubRepository clubRepository;


    public Team create(CreateTeamModel team){

        Integer idTournament = team.getTournament();
        Tournament tournament = tournamentRepository.findById(idTournament);

        Team teamCreate = new Team();

        teamCreate.setName(team.getName());
        teamCreate.setTournament(tournament);

        teamCreate.setCode(createCode(5));

        Club c;
        if (team.getOption().equals("oldClub")) {
            c = clubRepository.findById(team.getClubId());
        }
        else{
            c = new Club();
            c.setName(team.getNewClubName());
        }

        c = clubRepository.save(c);
        teamCreate.setClub(c);
        return teamRepository.save(teamCreate);

    }

    public Team addPlayers(AddPlayerlistModel addPlayerlistModel){

        List<AddPlayerModel> players = addPlayerlistModel.getPlayers();
        List<UserTournamentRole> userTournamentRoles = new ArrayList<>();

        Team team = teamRepository.findById(players.get(0).getTeamId());
        for(AddPlayerModel player : players){

            User user = player.addPlayer();

            if(!userRepository.existsUserByEmail(user.getEmail())){
                user.setPassword(createCode(5));
                user = userRepository.save(user);
            }else{
                user = userRepository.findUserByEmail(user.getEmail());
            }

            UserTournamentRole userTournamentRole = new UserTournamentRole();
            userTournamentRole.setTournament(team.getTournament());
            userTournamentRole.setUser(user);
            userTournamentRole.setTeam(team);
            userTournamentRole.setRole(UserRole.PLAYER);
            userTournamentRoles.add(userTournamentRole);
        }

        userTournamentRoleRepository.saveAll(userTournamentRoles);

        return team;
    }

    boolean areAllDistinct(List<User> users) {
        return users.stream().map(User::getEmail).distinct().count() == users.size();
    }
}

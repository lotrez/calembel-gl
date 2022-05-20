package com.molkky.molkky.service;



import com.molkky.molkky.domain.Team;
import com.molkky.molkky.domain.User;
import com.molkky.molkky.domain.UserTournamentRole;
import com.molkky.molkky.model.UserTournamentRoleModel;
import com.molkky.molkky.repository.TeamRepository;
import com.molkky.molkky.repository.UserRepository;
import com.molkky.molkky.repository.UserTournamentRoleRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import type.UserRole;

import java.util.ArrayList;
import java.util.List;





@Service
public class UserTournamentRoleService {
    @Autowired
    private UserTournamentRoleRepository userTournamentRoleRepository;

    @Autowired
    private TeamRepository teamRepository;

    public UserTournamentRole getUserTournamentRoleFromModel(UserTournamentRoleModel userTournamentRoleModel){
        return userTournamentRoleRepository.findById(userTournamentRoleModel.getId());
    }

    public List<UserTournamentRole> getTournamentStaffFromUser(UserTournamentRoleModel userTournamentRoleModel){
        UserTournamentRole user = getUserTournamentRoleFromModel(userTournamentRoleModel);
        return userTournamentRoleRepository.findUserTournamentRoleByRoleAndAndTournament(UserRole.STAFF, user.getTournament());
    }

    public List<User> getTeamUsers(Integer id) {
        Team correctTeam = teamRepository.findById(id);
        //System.out.println(userTounamentRoleRepository.findUsersByTeamId(4));
        return new ArrayList<User>();
    }


}

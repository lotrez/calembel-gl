package com.molkky.molkky.service;


import com.molkky.molkky.domain.Team;
import com.molkky.molkky.domain.User;
import com.molkky.molkky.repository.TeamRepository;
import com.molkky.molkky.repository.UserRepository;
import com.molkky.molkky.repository.UserTounamentRoleRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class UserTournamentRoleService {

    @Autowired
    UserRepository  userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserTounamentRoleRepository userTounamentRoleRepository;

    public List<User> getTeamUsers(Integer id) {
        Team correctTeam = teamRepository.findById(id);
        //System.out.println(userTounamentRoleRepository.findUsersByTeamId(4));
        return new ArrayList<User>();
    }

}

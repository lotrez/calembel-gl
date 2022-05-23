package com.molkky.molkky.service;

import com.molkky.molkky.domain.Tournament;
import com.molkky.molkky.domain.User;
import com.molkky.molkky.domain.UserTournamentRole;
import com.molkky.molkky.model.TournamentModel;
import com.molkky.molkky.repository.TournamentRepository;
import com.molkky.molkky.repository.UserRepository;
import com.molkky.molkky.repository.UserTournamentRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import type.UserRole;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;


@Service
public class TournamentService {

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTournamentRoleRepository userTournamentRoleRepository;


    public Tournament create(TournamentModel tournamentModel){
        Tournament tournament = new Tournament(tournamentModel);

        tournament = tournamentRepository.save(tournament);

        String mail = tournamentModel.getEmail();

        User user= new User();

        if(!userRepository.existsUserByEmail(mail)){
            user.setEmail(mail);
            user.setPassword(createCode(5));
            user =  userRepository.save(user);
        }else{
            user = userRepository.findUserByEmail(mail);
        }

        UserTournamentRole userTournamentRole = new UserTournamentRole();

        userTournamentRole.setUser(user);
        userTournamentRole.setRole(UserRole.ADM);
        userTournamentRole.setTournament(tournament);

        userTournamentRoleRepository.save(userTournamentRole);

        return tournament;
    }


    public String createCode(int n){
        SecureRandom random = new SecureRandom(); // Compliant for security-sensitive use cases
        byte[] bytes = new byte[n];
        random.nextBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}

package com.molkky.molkky.entity;

import Type.UserRole;
import com.molkky.molkky.MolkkyApplication;
import com.molkky.molkky.domain.Team;
import com.molkky.molkky.domain.User;
import com.molkky.molkky.repository.TeamRepository;
import com.molkky.molkky.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@SpringBootTest(classes = MolkkyApplication.class)
class TeamEntityTest {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testInsertTeam() {
        Team team = teamRepository.save(new Team("Team 1", 2));
        Assertions.assertEquals("Team 1", team.getName(), "Team name is not correct");
        Team recupTeam = teamRepository.findById(team.getId());
        Assertions.assertEquals("Team 1", recupTeam.getName(), "Team name is not correct");
    }

    @Test
    @Rollback(false)
    @Transactional
    void testTeamWithUsers(){
        Team team = teamRepository.save(new Team("Team_test", 1));
        Set<User> users = new HashSet<>();
        User user1 = userRepository.save(new User("pseudoUser1", "surname1", "forename1", "club1", "email1", false, UserRole.PLAYER));
        users.add(user1);
        team.setUsers(users);
        teamRepository.save(team);
        Assertions.assertEquals(1, team.getUsers().size(), "Team has not 2 users");
        Team recupTeam = teamRepository.findById(team.getId());
        Set<User> recupUsers = recupTeam.getUsers();
        Assertions.assertEquals(1, recupUsers.size(), "Team has not 2 users");

        Iterator<User> iter = recupUsers.iterator();

        Assertions.assertEquals("pseudoUser1", iter.next().getPseudo(), "User pseudo is not correct");
    }
}

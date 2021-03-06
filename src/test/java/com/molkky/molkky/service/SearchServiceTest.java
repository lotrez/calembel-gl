package com.molkky.molkky.service;

import com.molkky.molkky.domain.Team;
import com.molkky.molkky.domain.Tournament;
import com.molkky.molkky.domain.User;
import com.molkky.molkky.model.TeamModel;
import com.molkky.molkky.model.TournamentModel;
import com.molkky.molkky.model.UserModel;
import com.molkky.molkky.repository.TeamRepository;
import com.molkky.molkky.repository.TournamentRepository;
import com.molkky.molkky.repository.UserRepository;
import com.molkky.molkky.utility.StringUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SearchServiceTest {
    @Autowired
    private SearchService searchService;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Test
    void searchPreciseTournament() throws InterruptedException {
//        given
        Tournament tournament = new Tournament();
        String randomName = StringUtilities.createCode(20);
        tournament.setName(randomName);
        tournamentRepository.save(tournament);
//        when
        List<TournamentModel> found = searchService.searchTournaments(randomName);
//        then
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals(randomName, found.get(0).getName());
    }

    @Test
    void searchPreciseUser() throws InterruptedException {
//        given
        User user = new User();
        String randomName = StringUtilities.createCode(20);
        user.setSurname(randomName);
        userRepository.save(user);
//        when
        List<UserModel> found = searchService.searchUsers(randomName);
//        then
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals(randomName, found.get(0).getSurname());
    }

    @Test
    void searchPreciseTeam() throws InterruptedException {
//        given
        Team team = new Team();
        String randomName = StringUtilities.createCode(20);
        team.setName(randomName);
        teamRepository.save(team);
//        when
        List<TeamModel> found = searchService.searchTeams(randomName);
//        then
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals(randomName, found.get(0).getName());
    }
    @Test
    void searchLessOneLetterTournament() {
//        given
        Tournament tournament = new Tournament();
        String randomName = StringUtilities.createCode(20);
        StringBuilder sb = new StringBuilder(randomName);
        sb.deleteCharAt(19);
        String randomName2 = sb.toString();

        tournament.setName(randomName);
        tournamentRepository.save(tournament);
//        when
        List<TournamentModel> found = searchService.searchTournaments(randomName2);
//        then
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals(randomName, found.get(0).getName());
    }

    @Test
    void searchLimitTest(){
//        given
        String randomName = StringUtilities.createCode(40);
        for(int i=0; i < 20; i++){
            Tournament tournament = new Tournament();
            tournament.setName(randomName);
            tournamentRepository.save(tournament);
        }
//        when
        List<TournamentModel> found = searchService.searchTournaments(randomName);
//        then
        Assertions.assertEquals(10, found.size());
    }
}

package com.molkky.molkky.controllers;

import com.molkky.molkky.domain.*;
import com.molkky.molkky.model.*;
import com.molkky.molkky.repository.*;
import com.molkky.molkky.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import type.SetTeamIndex;
import type.UserRole;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = MatchController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ExtendWith(MockitoExtension.class)
class MatchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MatchRepository matchRepository;
    @MockBean
    private SetService setService;
    @MockBean
    private UserService userService;
    @MockBean
    private MatchService matchService;
    @MockBean
    private CourtRepository courtRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CourtService courtService;
    @MockBean
    private TeamRepository teamRepository;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private UserTournamentRoleRepository userTournamentRoleRepository;
    @Autowired
    private MatchController matchController;

    @Test
    void testControllerWithoutUser() throws Exception {
        mockMvc.perform(get("/matches/match?match_id=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/connexion"));
    }

    @Test
    void testControllerWithUser() throws Exception {
        UserLogged userLogged = Mockito.mock(UserLogged.class);
        Tournament t = new Tournament();
        t.setId(1);
        when(userLogged.getTournamentRoleId()).thenReturn(1);
        when(userLogged.getTournament()).thenReturn(t);
        Match match = createMatch();
        when(matchRepository.findById(1)).thenReturn(match);

        UserTournamentRole userTournamentRole = Mockito.mock(UserTournamentRole.class);
        userTournamentRole.setId(1);
        userTournamentRole.setRole(UserRole.PLAYER);
        userTournamentRole.setIsRegistered(true);
        when(userTournamentRoleRepository.findById(anyInt())).thenReturn(userTournamentRole);

        when(matchService.getUserTeamIndex(any(MatchModel.class), any(UserTournamentRoleModel.class))).thenReturn(SetTeamIndex.TEAM1);

        this.mockMvc.perform(get("/matches/match?match_id=1").sessionAttr("user", userLogged)
                        .sessionAttr("user",userLogged))
                .andExpect(status().isOk())
                .andExpect(model().attribute("match", MatchService.getMatchModelFromEntity(match)))
                .andExpect(model().attribute("teams", TeamModel.createTeamModels(match.getTeams())))
                //.andExpect(model().attribute("court", new CourtModel(match.getCourt())))
                .andExpect(model().attribute("tournament", new TournamentModel(match.getRound().getPhase().getTournament())))
                .andExpect(model().attribute("sets", SetService.createSetModels(match.getSets())))
                .andExpect(model().attribute("setTeamIndex", SetTeamIndex.TEAM1))
                .andExpect(model().attribute("user", userLogged))
                .andExpect(view().name("/match/match"));

    }

    @Test
    void updateMatchCourtTest() throws Exception {
//        given
        UserLogged userLogged = Mockito.mock(UserLogged.class);
        userLogged.setRole(UserRole.STAFF);
        userLogged.setTournamentRoleId(1);
        when(userLogged.getRole()).thenReturn(UserRole.STAFF);
        UserTournamentRole userTournamentRole = Mockito.mock(UserTournamentRole.class);
        userTournamentRole.setId(1);
        userTournamentRole.setRole(UserRole.STAFF);
        userTournamentRole.setIsRegistered(true);
        when(userTournamentRoleRepository.findById(anyInt())).thenReturn(userTournamentRole);
        Match match = createMatch();
        when(matchRepository.findById(1)).thenReturn(match);
        match.setId(1);
        Court newCourt = new Court(true, "New court Test");
        newCourt.setId(800);
//        when

//        then
        this.mockMvc.perform(post("/match/updateMatchCourt")
                        .sessionAttr("user", userLogged)
                        .param("id", newCourt.getId().toString())
                        .param("matchId", match.getId().toString())
                )
                .andExpect(status().is3xxRedirection());
        verify(matchService, times(1)).setCourt(any(MatchModel.class), any(CourtModel.class));
    }

    Match createMatch() {
        Match match = new Match();
        Court court = new Court();
        court.setName("court");
        match.setCourt(court);
        Team team1 = new Team();
        team1.setId(1);
        Team team2 = new Team();
        team2.setId(2);
        match.setTeams(Arrays.asList(team1, team2));
        Tournament tournament = new Tournament();
        tournament.setName("tournament");
        tournament.setId(1);
        tournament.setDate(Date.from(new Date().toInstant()));
        Phase phase = new Phase();
        Round round = new Round();
        round.setTournament(tournament);
        round.setPhase(phase);
        phase.setRounds(List.of(round));
        phase.setTournament(tournament);
        match.setRound(round);
        Set set = new Set();
        set.setMatch(match);
        match.setSets(List.of(set));
        return match;
    }
}

package com.molkky.molkky.controllers;

import com.molkky.molkky.domain.*;
import com.molkky.molkky.domain.rounds.Pool;
import com.molkky.molkky.domain.rounds.SwissPool;
import com.molkky.molkky.model.UserLogged;
import com.molkky.molkky.model.phase.PhaseListModel;
import com.molkky.molkky.model.phase.PhaseModel;
import com.molkky.molkky.model.phase.PhaseRankingModel;
import com.molkky.molkky.repository.*;
import com.molkky.molkky.service.NotificationService;
import com.molkky.molkky.service.PhaseService;
import com.molkky.molkky.service.RoundService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import type.PhaseType;
import type.TournamentStatus;
import type.UserRole;

import javax.servlet.http.HttpSession;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = PhaseController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ExtendWith(MockitoExtension.class)
 class PhaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PhaseRepository phaseCreate;

    @MockBean
    private TournamentRepository tournamentRepository;

    @MockBean
    private PhaseRepository phaseRepository;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserTournamentRoleRepository userTournamentRoleRepository;

    @MockBean
    private PhaseService phaseService;

    @MockBean
    private NotificationService notificationService;
    @MockBean
    private Tournament tournament;

    @MockBean
    private PhaseListModel listModel;

    @MockBean
    private PhaseModel phase;

    @MockBean
    private Phase phaseEntity;

    @MockBean
    private RoundRepository roundRepository;

    @MockBean
    private RoundService roundService;


    @Test
    void testControllerWithoutUser() throws Exception {
        mockMvc.perform(post("/phase/generate")
                        .param("id","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/connexion"));
    }

   @Test
    void testGeneratePhaseControllerWithUser() throws Exception {
       UserLogged userLogged = mock(UserLogged.class);
       userLogged.setTournamentRoleId(1);
       userLogged.setRole(UserRole.ADM);
       userLogged.setTournament(tournament);
       HttpSession session = new MockHttpSession(null, "user");
       session.setAttribute("user", userLogged);

       String id = "1";

       when(phaseEntity.getId()).thenReturn(1);
       when(phaseEntity.getTournament()).thenReturn(tournament);
       when(tournament.getIndexPhase()).thenReturn(1);
       when(this.phaseRepository.findById(Integer.valueOf(id))).thenReturn(phaseEntity);

        when(tournamentRepository.save(Mockito.any(Tournament.class))).thenAnswer(i -> i.getArguments()[0]);
        when(phaseRepository.save(Mockito.any(Phase.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(teamRepository.save(Mockito.any(Team.class))).thenAnswer(i -> i.getArguments()[0]);
        Map<Round, List<Match>> response = createRounds();

        when(userLogged.getRole()).thenReturn(UserRole.ADM);
        when(phaseService.generate("1")).thenReturn(response);

        this.mockMvc.perform(post("/phase/generate").sessionAttr("user", userLogged)
                        .sessionAttr("user", userLogged)
                        .param("id",id))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/phase/view?id=1&phaseIndex=1"));
    }

    @Test
    void testNextRoundControllerWithoutUser() throws Exception {
        Tournament tournament = new Tournament();
        String id = "1";

        this.mockMvc.perform(post("/phase/round")
                        .param("id",id)
                        .param("nbSet", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/connexion"));
    }

    @Test
    void testNextRoundControllerWithUserAdmin() throws Exception {
        UserLogged userLogged = mock(UserLogged.class);
        userLogged.setTournamentRoleId(1);
        userLogged.setRole(UserRole.ADM);
        userLogged.setTournament(tournament);
        HttpSession session = new MockHttpSession(null, "user");
        session.setAttribute("user", userLogged);
        Tournament tournament = new Tournament();
        String id = "1";

        when(phaseEntity.getId()).thenReturn(1);
        when(phaseEntity.getTournament()).thenReturn(tournament);
        when(this.phaseRepository.findById(Integer.valueOf(id))).thenReturn(phaseEntity);
        when(phaseEntity.getNbSets()).thenReturn(1);
        when(tournamentRepository.save(Mockito.any(Tournament.class))).thenAnswer(i -> i.getArguments()[0]);
        when(phaseRepository.save(Mockito.any(Phase.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(teamRepository.save(Mockito.any(Team.class))).thenAnswer(i -> i.getArguments()[0]);
        Map<Round, List<Match>> response = createRounds();

        when(userLogged.getRole()).thenReturn(UserRole.ADM);
        when(phaseService.generate("1")).thenReturn(response);

        this.mockMvc.perform(post("/phase/round").sessionAttr("user", userLogged)
                        .sessionAttr("user",userLogged)
                        .param("id",id)
                .param("nbSet", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/phase/view?id=1&phaseIndex=0"));
    }

    @Test
    void testNextRoundControllerWithUserNotAdmin() throws Exception {
        UserLogged userLogged = mock(UserLogged.class);
        userLogged.setTournamentRoleId(1);
        userLogged.setRole(UserRole.PLAYER);
        userLogged.setTournament(tournament);
        HttpSession session = new MockHttpSession(null, "user");
        session.setAttribute("user", userLogged);
        Tournament tournament = new Tournament();
        String id = "1";

        when(phaseEntity.getId()).thenReturn(1);
        when(phaseEntity.getTournament()).thenReturn(tournament);
        when(this.phaseRepository.findById(Integer.valueOf(id))).thenReturn(phaseEntity);
        when(phaseEntity.getNbSets()).thenReturn(1);
        when(tournamentRepository.save(Mockito.any(Tournament.class))).thenAnswer(i -> i.getArguments()[0]);
        when(phaseRepository.save(Mockito.any(Phase.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(teamRepository.save(Mockito.any(Team.class))).thenAnswer(i -> i.getArguments()[0]);
        Map<Round, List<Match>> response = createRounds();

        when(userLogged.getRole()).thenReturn(UserRole.PLAYER);
        when(phaseService.generate("1")).thenReturn(response);

        this.mockMvc.perform(post("/phase/round")
                        .sessionAttr("user",userLogged)
                        .param("id",id)
                        .param("nbSet", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void testGenerateControllerPhaseIsOver() throws Exception {
        UserLogged userLogged = Mockito.mock(UserLogged.class);
        String id = "1";

        when(phaseEntity.getId()).thenReturn(1);
        when(phaseEntity.getFinished()).thenReturn(true);
        when(phaseEntity.getTournament()).thenReturn(tournament);
        when(phaseEntity.getTournament().getId()).thenReturn(1);
        when(this.phaseRepository.findById(Integer.valueOf(id))).thenReturn(phaseEntity);

        this.mockMvc.perform(post("/phase/round").sessionAttr("user", userLogged)
                        .sessionAttr("user",userLogged)
                        .param("id",id))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/tournament/view?tournamentId=1"));
    }

    @Test
    void testGeneratePhaseControllerWithWrongRole() throws Exception {
        UserLogged userLogged = Mockito.mock(UserLogged.class);
        when(userLogged.getRole()).thenReturn(UserRole.PLAYER);
        Tournament tournament = new Tournament();
        String id = "1";

        when(phaseEntity.getTournament()).thenReturn(tournament);
        when(this.phaseRepository.findById(Integer.valueOf(id))).thenReturn(phaseEntity);

        this.mockMvc.perform(post("/phase/generate").sessionAttr("user", userLogged)
                        .sessionAttr("user",userLogged)
                        .param("id",id))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    Map<Round, List<Match>> createRounds(){
        Tournament tournament = new Tournament(
                "tournament test",
                "location",
                new Date(),
                new Date(),
                1,
                8,
                true,
                true,
                2,
                3,
                2
        );
        tournament.setId(1);
        tournament.setNbPlayersPerTeam(2);
        tournament.setVisible(true);
        tournament.setStatus(TournamentStatus.AVAILABLE);
        tournament = tournamentRepository.save(tournament);

        Pool pool = new Pool();
        pool.setId(1);
        pool.setNbSets(1);
        pool.setVictoryValue(2);
        pool.setNbPhase(1);
        pool.setNbPools(2);
        pool.setNbTeamsQualified(4);

        pool.setTournament(tournament);
        pool =  phaseRepository.save(pool);

        List<Phase> phases = new ArrayList<>();
        phases.add(pool);
        tournament.setPhases(phases);
        tournament = tournamentRepository.save(tournament);


        for (int i =1; i <= 8; i++){
            Team team = new Team();

            team.setCode("12345");

            team.setName("Team" + i);
            team.setTournament(tournament);


            tournament.getTeams().add(team);

            User player = new User();

            player.setForename("User" + i);

            player =  userRepository.save(player);

            UserTournamentRole userTournamentRole = new UserTournamentRole();

            userTournamentRole.setRole(UserRole.PLAYER);
            userTournamentRole.setUser(player);
            userTournamentRole.setTournament(tournament);
            userTournamentRole.setTeam(team);

            player.getUserTournamentRoles().add(userTournamentRole);
            tournament.getUserTournamentRoles().add(userTournamentRole);
            team.getUserTournamentRoles().add(userTournamentRole);

            team = teamRepository.save(team);
            userTournamentRoleRepository.save(userTournamentRole);
            tournamentRepository.save(tournament);
            userRepository.save(player);
        }
        return  phaseService.generate("1");
    }

    @Test
    void testPhaseGetMethod() throws Exception{
        Integer tournamentId = 1;
        Integer nbRounds = 5;
        when(this.tournamentRepository.findById(tournamentId)).thenReturn(tournament);
        when(tournament.getNbRounds()).thenReturn(nbRounds);
        when(tournament.getId()).thenReturn(tournamentId);

        this.mockMvc.perform(get("/phase/choosePhases")
                .param("tournamentId","1"))
                .andExpect(model().attributeExists("form"))
                .andExpect(view().name("/phase/choosePhases"));

        verify(tournamentRepository,times(1)).findById(1);
        verify(tournament,times(5)).getId();
        verify(tournament,times(6)).getNbRounds();
    }

    @Test
    void testPhasePostTypePhase() throws Exception {
        List<PhaseModel> list = new ArrayList<>();
        list.add(phase);

        when(listModel.getPhases()).thenReturn(list);

        mockMvc.perform(post("/phase/choosePhases")
                .flashAttr("form",listModel))
                .andExpect(model().attributeExists("listPhase"))
                .andExpect(view().name("/phase/editPhases"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testPhasePostPhaseFinnishInformation() throws Exception {
        List<PhaseModel> list = new ArrayList<>();
        list.add(phase);

        Tournament tournament = new Tournament();
        tournament.setId(1);

        when(listModel.getPhases()).thenReturn(list);
        when(phase.getTournament()).thenReturn(tournament.getId());
        when(listModel.getPhases().get(0).getTournament()).thenReturn(tournament.getId());
        when(tournamentRepository.findById(listModel.getPhases().get(0).getTournament())).thenReturn(tournament);
        when(tournamentRepository.save(Mockito.any(Tournament.class))).thenReturn(tournament);
        when(phase.getPhaseType()).thenReturn(PhaseType.FINNISH);
        when(phase.getHourPhaseStart()).thenReturn("");
        when(phase.getTimePhase()).thenReturn("");
        //when(phase.setNbPhase(1)).
        //when(phase.getNbSets()).thenReturn(1);

        mockMvc.perform(post("/phase/editPhases")
                .flashAttr("listPhase",listModel))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/tournament/view?tournamentId=1"));

        verify(listModel,times(4)).getPhases();
        verify(phase,times(2)).getTournament();
        verify(phase,times(1)).getPhaseType();
        verify(phase,times(1)).getHourPhaseStart();
        verify(phase,times(1)).getTimePhase();
        verify(tournamentRepository,times(1)).findById(1);
        verify(tournamentRepository,times(1)).save(Mockito.any(Tournament.class));
    }

    @Test
    void testPhasePostPhasePoolInformation() throws Exception {
        List<PhaseModel> list = new ArrayList<>();
        list.add(phase);

        Tournament tournament = new Tournament();
        tournament.setId(1);

        when(listModel.getPhases()).thenReturn(list);
        when(phase.getTournament()).thenReturn(tournament.getId());
        when(listModel.getPhases().get(0).getTournament()).thenReturn(tournament.getId());
        when(tournamentRepository.findById(listModel.getPhases().get(0).getTournament())).thenReturn(tournament);
        when(tournamentRepository.save(Mockito.any(Tournament.class))).thenReturn(tournament);
        when(phase.getPhaseType()).thenReturn(PhaseType.POOL);
        when(phase.getHourPhaseStart()).thenReturn("");
        when(phase.getTimePhase()).thenReturn("");

        mockMvc.perform(post("/phase/editPhases")
                        .flashAttr("listPhase",listModel))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/tournament/view?tournamentId=1"));

        verify(listModel,times(4)).getPhases();
        verify(phase,times(2)).getTournament();
        verify(phase,times(1)).getPhaseType();
        verify(phase,times(1)).getHourPhaseStart();
        verify(phase,times(1)).getTimePhase();
        verify(tournamentRepository,times(1)).findById(1);
        verify(tournamentRepository,times(1)).save(Mockito.any(Tournament.class));
    }

    @Test
    void testPhasePostPhaseSimpleInformation() throws Exception {
        List<PhaseModel> list = new ArrayList<>();
        list.add(phase);

        Tournament tournament = new Tournament();
        tournament.setId(1);

        when(listModel.getPhases()).thenReturn(list);
        when(phase.getTournament()).thenReturn(tournament.getId());
        when(listModel.getPhases().get(0).getTournament()).thenReturn(tournament.getId());
        when(tournamentRepository.findById(listModel.getPhases().get(0).getTournament())).thenReturn(tournament);
        when(tournamentRepository.save(Mockito.any(Tournament.class))).thenReturn(tournament);
        when(phase.getPhaseType()).thenReturn(PhaseType.SIMPLEGAME);
        when(phase.getHourPhaseStart()).thenReturn("");
        when(phase.getTimePhase()).thenReturn("");

        mockMvc.perform(post("/phase/editPhases")
                        .flashAttr("listPhase",listModel))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/tournament/view?tournamentId=1"));

        verify(listModel,times(4)).getPhases();
        verify(phase,times(2)).getTournament();
        verify(phase,times(1)).getPhaseType();
        verify(phase,times(1)).getHourPhaseStart();
        verify(phase,times(1)).getTimePhase();
        verify(tournamentRepository,times(1)).findById(1);
        verify(tournamentRepository,times(1)).save(Mockito.any(Tournament.class));
    }

    @Test
    void testPhasePostPhaseKnockoutInformation() throws Exception {
        List<PhaseModel> list = new ArrayList<>();
        list.add(phase);

        Tournament tournament = new Tournament();
        tournament.setId(1);

        when(listModel.getPhases()).thenReturn(list);
        when(phase.getTournament()).thenReturn(tournament.getId());
        when(listModel.getPhases().get(0).getTournament()).thenReturn(tournament.getId());
        when(tournamentRepository.findById(listModel.getPhases().get(0).getTournament())).thenReturn(tournament);
        when(tournamentRepository.save(Mockito.any(Tournament.class))).thenReturn(tournament);
        when(phase.getPhaseType()).thenReturn(PhaseType.KNOCKOUT);
        when(phase.getHourPhaseStart()).thenReturn("");
        when(phase.getTimePhase()).thenReturn("");

        mockMvc.perform(post("/phase/editPhases")
                        .flashAttr("listPhase",listModel))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/tournament/view?tournamentId=1"));

        verify(listModel,times(4)).getPhases();
        verify(phase,times(2)).getTournament();
        verify(phase,times(1)).getPhaseType();
        verify(phase,times(1)).getHourPhaseStart();
        verify(phase,times(1)).getTimePhase();
        verify(tournamentRepository,times(1)).findById(1);
        verify(tournamentRepository,times(1)).save(Mockito.any(Tournament.class));
    }

    @Test
    void testPhasePostPhaseSwissInformation() throws Exception {
        List<PhaseModel> list = new ArrayList<>();
        list.add(phase);

        Tournament tournament = new Tournament();
        tournament.setId(1);

        when(listModel.getPhases()).thenReturn(list);
        when(phase.getTournament()).thenReturn(tournament.getId());
        when(listModel.getPhases().get(0).getTournament()).thenReturn(tournament.getId());
        when(tournamentRepository.findById(listModel.getPhases().get(0).getTournament())).thenReturn(tournament);
        when(tournamentRepository.save(Mockito.any(Tournament.class))).thenReturn(tournament);
        when(phase.getPhaseType()).thenReturn(PhaseType.SWISSPOOL);
        when(phase.getHourPhaseStart()).thenReturn("");
        when(phase.getTimePhase()).thenReturn("");

        mockMvc.perform(post("/phase/editPhases")
                        .flashAttr("listPhase",listModel))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/tournament/view?tournamentId=1"));

        verify(listModel,times(4)).getPhases();
        verify(phase,times(2)).getTournament();
        verify(phase,times(1)).getPhaseType();
        verify(phase,times(1)).getHourPhaseStart();
        verify(phase,times(1)).getTimePhase();
        verify(tournamentRepository,times(1)).findById(1);
        verify(tournamentRepository,times(1)).save(Mockito.any(Tournament.class));
    }

    @Test
    void testPhaseControllerView() throws Exception {
        Phase phase = new Phase();
        phase.setId(17888);
        List<Round> rounds = new ArrayList<>();
        Round round = new Round();
        round.setType(PhaseType.KNOCKOUT);
        rounds.add(round);
        phase.setRounds(rounds);
        Tournament tournoi = new Tournament();
        tournoi.setIndexPhase(1);
        phase.setTournament(tournoi);
        when(phaseRepository.findById(17888)).thenReturn(phase);

        mockMvc.perform(get("/phase/view")
                        .param("id", "17888")
                         .param("phaseIndex", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rounds"))
                .andExpect(model().attributeExists("roundTeams"))
                .andExpect(model().attributeExists("currentPhase"))
                .andExpect(model().attributeExists("currentTournament"))
                .andExpect(view().name("/phase/view"));

        verify(this.phaseRepository, times(1)).findById(anyInt());
        verify(this.roundService, times(1)).orderTeamsByScoreInRound(any(Round.class), anyInt());

        mockMvc.perform(get("/phase/view")
                .param("id", "17888")
                .param("phaseIndex", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/phase/view"));
    }

    @Test
    void testGetModifyPhase() throws Exception {
        Phase phase = mock(Phase.class);

        when(tournamentRepository.findById(1)).thenReturn(tournament);
        when(tournament.getPhases()).thenReturn(Arrays.asList(phase));
        when(phase.getDecriminatorValue()).thenReturn(PhaseType.SIMPLEGAME);
        when(phase.getTournament()).thenReturn(tournament);

        mockMvc.perform(get("/phase/modify")
                .param("tournamentId", "1"))
                .andExpect(model().attributeExists("listPhase"))
                .andExpect(view().name("/phase/modifyPhase"))
                .andExpect(status().is2xxSuccessful());

        verify(tournamentRepository,times(1)).findById(1);
        verify(tournament,times(1)).getPhases();
        verify(tournament,times(1)).getId();
        verify(phase,times(2)).getDecriminatorValue();
        verify(phase,times(1)).getTournament();

        verifyNoMoreInteractions(tournamentRepository);
        verifyNoMoreInteractions(tournament);
    }

    @Test
    void testPostModifyPhase() throws Exception {

        when(phaseService.editPhasesInfo(Mockito.any(PhaseListModel.class))).thenReturn(tournament);
        when(tournament.getId()).thenReturn(1);

        mockMvc.perform(post("/phase/modify"))
                .andExpect(view().name("redirect:/tournament/view?tournamentId=1"))
                .andExpect(status().is3xxRedirection());

        verify(phaseService,times(1)).editPhasesInfo(Mockito.any(PhaseListModel.class));
        verify(tournament,times(1)).getId();

        verifyNoMoreInteractions(phaseService);
    }

    @Test
    void testPhaseControllerViewWithSwissPool() throws Exception {
        PhaseModel phaseModelSwissPool = new PhaseModel();
        phaseModelSwissPool.setPhaseType(PhaseType.SWISSPOOL);
        phaseModelSwissPool.setTournament(tournament.getId());
        phaseModelSwissPool.setHourPhaseStart("");
        phaseModelSwissPool.setTimePhase("");
        List<Round> rounds = new ArrayList<>();
        rounds.add(new Round());
        phase.setRounds(rounds);
        Tournament tournoi = new Tournament();
        tournoi.setIndexPhase(1);

        Phase phase = new SwissPool(phaseModelSwissPool, tournoi);
        phase.setTournament(tournoi);
        phase.setId(17888);
        phase.setVictoryValue(3);

        List<PhaseRankingModel> teams = new ArrayList<>();
        PhaseRankingModel phaseRankingModel = new PhaseRankingModel();
        Team team = new Team();

        phaseRankingModel.setTeam(team);
        phaseRankingModel.setTotalPoints(181);
        teams.add(phaseRankingModel);

        when(roundService.orderTeamsByScoreInPhase(phase, phase.getVictoryValue())).thenReturn(teams);
        when(phaseRepository.findById(17888)).thenReturn(phase);

        mockMvc.perform(get("/phase/view")
                        .param("id", "17888")
                        .param("phaseIndex", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rounds"))
                .andExpect(model().attributeExists("roundTeams"))
                .andExpect(model().attributeExists("currentPhase"))
                .andExpect(model().attributeExists("currentTournament"))
                .andExpect(view().name("/phase/view"));

        verify(this.phaseRepository, times(1)).findById(anyInt());

        mockMvc.perform(get("/phase/view")
                        .param("id", "17888")
                        .param("phaseIndex", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/phase/view"));
    }
}

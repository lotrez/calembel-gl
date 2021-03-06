package com.molkky.molkky.controllers;


import com.molkky.molkky.controllers.superclass.DefaultAttributes;
import com.molkky.molkky.domain.*;
import com.molkky.molkky.domain.rounds.*;
import com.molkky.molkky.model.*;
import com.molkky.molkky.repository.TeamRepository;
import com.molkky.molkky.repository.TournamentRepository;
import com.molkky.molkky.repository.UserTournamentRoleRepository;
import com.molkky.molkky.service.NotificationService;
import com.molkky.molkky.service.PhaseService;
import com.molkky.molkky.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import type.PhaseType;
import type.TournamentStatus;
import type.UserRole;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tournament")
public class TournamentController extends DefaultAttributes {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    UserTournamentRoleRepository userTournamentRoleRepository;

    @Autowired
    NotificationService notificationService;

    private String allTournament="tournament";
    private String redirectionAll = "/tournament/allTournament";
    private String redirectViewId = "redirect:/tournament/view?tournamentId=";

    @GetMapping("/allTournament")
    public String tournamentForm(Model model) {
        model.addAttribute(allTournament, tournamentRepository.findAll());
        return redirectionAll;
    }

    @GetMapping("/TournamentOpen")
    public String tournamentOpen(Model model) {
        model.addAttribute(allTournament, tournamentRepository.findByVisibleAndStatus(true,TournamentStatus.AVAILABLE));
        return redirectionAll;
    }


    @GetMapping("/TournamentClose")
    public String tournamentClose(Model model) {
        model.addAttribute(allTournament, tournamentRepository.findByVisibleAndStatus(true,TournamentStatus.CLOSED));

        return redirectionAll;
    }

    @GetMapping("/TournamentInProgress")
    public String tournamentinProgress(Model model) {
        model.addAttribute(allTournament, tournamentRepository.findByVisibleAndStatus(true,TournamentStatus.INPROGRESS));
        return redirectionAll;
    }

    @PostMapping("/allTournament")
    public ModelAndView goToCreate(ModelMap model) {
        return new ModelAndView("redirect:/tournament/create", model);
    }

    @PostMapping("/inscription")
    public ModelAndView goToInscription(ModelMap model){return new ModelAndView("redirect:/team/create",model);}

    @GetMapping ("/tournamentOnGoing")
    public String getTournamentOnGoing() {
        return "/tournament/tournamentOnGoing";
    }

    @GetMapping("/create")
    public String tournamentForm(Model model, HttpSession session) {
        model.addAttribute(allTournament, new TournamentModel());
        return "tournament/create";
    }

    @PostMapping("/create")
    public String tournamentSubmit(@Valid @ModelAttribute("tournament") TournamentModel tournament, Model model) {

        Tournament tournamentEntity = tournamentService.create(tournament);

        int id = tournamentEntity.getId();
        return "redirect:/phase/choosePhases?tournamentId="+id;
    }

    @GetMapping("/modify")
    public String tournamentModify(Model model,@RequestParam(value = "tournamentId") String tournamentId,  HttpSession session){
        UserLogged user = getUser(session);
        Tournament tournament = tournamentRepository.findById(Integer.valueOf(tournamentId));
        if(!user.getEmail().equals(tournamentService.getEmailAdmin(tournament)) || !tournament.getStatus().equals(TournamentStatus.AVAILABLE)){
            return "redirect:"+redirectionAll;
        }
        TournamentModel tournamentModel = new TournamentModel(tournament);
        tournamentModel.setDatesNull();
        tournamentModel.setEmail(tournamentService.getEmailAdmin(tournament));
        model.addAttribute(allTournament,tournamentModel);
        return "/tournament/modify";
    }

    @PostMapping("/modify")
    public String tournamentEdit(@Valid @ModelAttribute("tournament") TournamentModel tournament, Model model){
        Integer oldNbRounds = tournamentRepository.findById(tournament.getId()).getNbRounds();
        Tournament tournament1 = tournamentService.modifyTournament(tournament);
        List<UserTournamentRole> userTournamentRoles = userTournamentRoleRepository.
                findUserTournamentRoleByRoleAndTournament(UserRole.PLAYER,tournament1);
        notificationService.sendNotificationToList("Les infos du tournoi "+tournament1.getName()+" ont ??t?? chang??es",
                "/tournament/view?tournamentId="+tournament1.getId(),userTournamentRoles);
        if(!oldNbRounds.equals(tournament1.getNbRounds())){
            phaseService.clearTournamentPhases(tournament1);
            return "redirect:/phase/choosePhases?tournamentId="+tournament1.getId();
        }
        return "redirect:/phase/modify?tournamentId="+tournament1.getId();
    }

    @GetMapping("/view")
    public String tournamentViewPostLaunch(Model model,@RequestParam(value = "tournamentId", required = false) String tournamentId,  HttpSession session){
        Tournament tournament = tournamentRepository.findById(Integer.valueOf(tournamentId));

        //USER FROM SESSION
        UserLogged user = getUser(session);

        if (user != null) {
            if (user.getTournament().getId().toString().equals(tournamentId) && user.getRole().equals(UserRole.ADM)) {
                model.addAttribute("user", user);
            }else{
                model.addAttribute("user", null);
            }
        }

        List<Phase> phases = tournament.getPhases();

        List<PhaseTypeViewModel> phasesType = new ArrayList<>();
        for (Phase p : phases) {
            if (p instanceof Pool) {
                Pool pool = (Pool) p;
                PhaseTypeViewModel phaseTypeViewModel = new PhaseTypeViewModel();
                phaseTypeViewModel.setPhase(pool);
                phaseTypeViewModel.setPhaseType(PhaseType.POOL);
                phasesType.add(phaseTypeViewModel);
            } else if (p instanceof SimpleGame) {
                SimpleGame pool = (SimpleGame) p;
                PhaseTypeViewModel phaseTypeViewModel = new PhaseTypeViewModel();
                phaseTypeViewModel.setPhase(pool);
                phaseTypeViewModel.setPhaseType(PhaseType.SIMPLEGAME);
                phasesType.add(phaseTypeViewModel);
            } else if (p instanceof Knockout) {
                Knockout pool = (Knockout) p;
                PhaseTypeViewModel phaseTypeViewModel = new PhaseTypeViewModel();
                phaseTypeViewModel.setPhase(pool);
                phaseTypeViewModel.setPhaseType(PhaseType.KNOCKOUT);
                phasesType.add(phaseTypeViewModel);
            } else if (p instanceof SwissPool) {

                SwissPool pool = (SwissPool) p;
                PhaseTypeViewModel phaseTypeViewModel = new PhaseTypeViewModel();
                phaseTypeViewModel.setPhase(pool);
                phaseTypeViewModel.setPhaseType(PhaseType.SWISSPOOL);
                phasesType.add(phaseTypeViewModel);
            } else if (p instanceof Finnish) {
                Finnish pool = (Finnish) p;
                PhaseTypeViewModel phaseTypeViewModel = new PhaseTypeViewModel();
                phaseTypeViewModel.setPhase(pool);
                phaseTypeViewModel.setPhaseType(PhaseType.FINNISH);
                phasesType.add(phaseTypeViewModel);
            }
        }
        if (tournament.getIndexPhase()!=0) model.addAttribute("currentPhase", tournament.getPhases().get(tournament.getIndexPhase()-1));
        else model.addAttribute("currentPhase", null);
        model.addAttribute("phasesType", phasesType);
        model.addAttribute(allTournament, tournament);
        model.addAttribute("nbTeam", tournament.getTeams().size());
        model.addAttribute("isReady", tournamentService.isTournamentReady(tournament));
        return "/tournament/view";

    }

    @PostMapping("/addStaff")
    public ModelAndView addStaffToTournament(ModelMap model, @RequestParam(name="staffCount") String staffCount,
                                       @RequestParam(name="tournamentId") String tournamentId) {

        model.addAttribute("tournamentId", tournamentId);
        model.addAttribute("staff_counter", staffCount);

        List<AddStaff> staffList = new ArrayList<>();

        for(int i =0; i< Integer.valueOf(staffCount) ;i++){
            AddStaff addStaff = new AddStaff();
            addStaff.setTournamentId(Integer.valueOf(tournamentId));
            staffList.add(addStaff);
        }


        model.addAttribute("isDiffMail", true);
        model.addAttribute("staffList", new AddStaffList(staffList));
        return new ModelAndView( "/tournament/addStaff", model) ;
    }

    @PostMapping("/addCourt")
    public ModelAndView addCourtToTournament(ModelMap model, @RequestParam(name="courtCount") String courtCount,
                                       @RequestParam(name="tournamentId") String tournamentId) {

        model.addAttribute("tournamentId", tournamentId);
        model.addAttribute("court_counter", courtCount);

        List<AddCourt> courtList = new ArrayList<>();

        for(int i = 0; i< Integer.parseInt(courtCount) ; i++){
            AddCourt addCourt = new AddCourt();
            addCourt.setTournamentId(Integer.valueOf(tournamentId));
            courtList.add(addCourt);
        }


        model.addAttribute("isDiffMail", true);
        model.addAttribute("courtList", new AddCourtList(courtList));
        return new ModelAndView( "/tournament/addCourt", model) ;
    }

    @PostMapping("/setVisible")
    public String setVisibleTournament(Model model,@RequestParam(name="tournamentId") String tournamentId ){

        Tournament tournament = tournamentRepository.findById(Integer.valueOf(tournamentId));

        tournament.setVisible(true);
        tournament.setRegisterAvailable(true);
        tournamentRepository.save(tournament);

        model.addAttribute("tournament_id", tournamentId);

        return (redirectViewId + tournamentId);

    }

    @PostMapping("/publish")
    public String publishTournament(Model model,@RequestParam(name="tournamentId") String tournamentId ){

        Tournament tournament = tournamentRepository.findById(Integer.valueOf(tournamentId));
        tournament.setStatus(TournamentStatus.INPROGRESS);
        tournament.setIndexPhase(1);
        tournament.setRegisterAvailable(false);
        tournamentRepository.save(tournament);
        phaseService.generate(tournament.getPhases().get(0).getId().toString());

        model.addAttribute("tournament_id", tournamentId);
        return (redirectViewId + tournamentId);
    }

    @PostMapping("/validatePresence")
    public String validatePresence (Model model, @RequestParam(name = "tournamentId")String tournamentId, @RequestParam(name = "teamId")int teamId) {
        Team team = teamRepository.findById(teamId);
        team.setPresent(false);
        team.setEliminated(true);
        teamRepository.save(team);
        return (redirectViewId + tournamentId);
    }

    @GetMapping("/results")
    public String results (Model model, @RequestParam(name="tournamentId") String tournamentId){
        Tournament tournament = tournamentRepository.findById(Integer.valueOf(tournamentId));
        model.addAttribute("tournamentStatus", tournament.getStatus().toString());

        List<Team> teamsWinner = tournamentService.getWinners(tournament);
        List<User> players = new ArrayList<>();

        model.addAttribute("winners", teamsWinner);

        for (Team team : teamsWinner) {
            List<UserTournamentRole> usersTournamentRole = team.getUserTournamentRoles();
            for (UserTournamentRole userTournamentRole : usersTournamentRole) {
                User user = userTournamentRole.getUser();
                players.add(user);
            }
        }
        model.addAttribute("players", players);

        return "/tournament/results";
    }

    @PostMapping("/results")
    public String resultsPost(@RequestParam(name= "tournamentId") Integer tournamentId){
        return "redirect:/tournament/results?tournamentId="+tournamentId;
    }

}



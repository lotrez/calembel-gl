package com.molkky.molkky.controllers;



import com.molkky.molkky.domain.Phase;
import com.molkky.molkky.domain.Team;

import com.molkky.molkky.controllers.superclass.DefaultAttributes;

import com.molkky.molkky.domain.Tournament;
import com.molkky.molkky.domain.User;
import com.molkky.molkky.domain.rounds.*;
import com.molkky.molkky.model.PhaseTypeViewModel;
import com.molkky.molkky.model.TournamentModel;
import com.molkky.molkky.model.UserLogged;
import com.molkky.molkky.repository.TournamentRepository;

import com.molkky.molkky.repository.UserRepository;
import com.molkky.molkky.repository.UserTounamentRoleRepository;
import com.molkky.molkky.service.TournamentService;
import com.molkky.molkky.service.UserTournamentRoleService;

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
import java.util.*;

@Controller
@RequestMapping("/tournament")
public class TournamentController extends DefaultAttributes {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentService tournamentService;


    private String allTournament="tournament";
    private String redirectionAll = "/tournament/allTournament";


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


    @PostMapping ("/currentTournament")
    public String currentTournament() {
        return "/";
    }

    @GetMapping ("/tournamentOnGoing")
    public String getTournamentOnGoing() {
        return "/tournament/tournamentOnGoing";
    }

    @Autowired
    private UserTounamentRoleRepository userTounamentRoleRepository;

    @Autowired
    private UserTournamentRoleService userTournamentRoleService;

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

    @GetMapping("/{id}/view")
    public String tournamentView(Model model, @PathVariable("id") String id, HttpSession session){

        //USER FROM SESSION
        UserLogged user = getUser(session);

        if(user!=null){
            if(user.getTournament().getId().toString().equals(id) && user.getRole().equals(UserRole.ADM)){
                model.addAttribute("user", user);
            }
        }

        //System.out.println(userTounamentRoleRepository.findUsersByTeamId(4));
        userTournamentRoleService.getTeamUsers(4);


        Tournament tournament = tournamentRepository.findById(Integer.valueOf(id));

        List<Team> teams = tournament.getTeams();


        System.out.println(teams.get(0).getId());


        List<Phase> phases = tournament.getPhases();

        List<PhaseTypeViewModel> phasesType = new ArrayList<>();
        for (Phase p : phases){
            if (p instanceof Pool){
                Pool pool = (Pool) p;
                PhaseTypeViewModel phaseTypeViewModel = new PhaseTypeViewModel();
                phaseTypeViewModel.setPhase(pool);
                phaseTypeViewModel.setPhaseType(PhaseType.POOL);
                phasesType.add(phaseTypeViewModel);
            }else  if (p instanceof SimpleGame){
                SimpleGame pool = (SimpleGame) p;
                PhaseTypeViewModel phaseTypeViewModel = new PhaseTypeViewModel();
                phaseTypeViewModel.setPhase(pool);
                phaseTypeViewModel.setPhaseType(PhaseType.SIMPLEGAME);
                phasesType.add(phaseTypeViewModel);
            }else  if (p instanceof Knockout){
                Knockout pool = (Knockout) p;
                PhaseTypeViewModel phaseTypeViewModel = new PhaseTypeViewModel();
                phaseTypeViewModel.setPhase(pool);
                phaseTypeViewModel.setPhaseType(PhaseType.KNOCKOUT);
                phasesType.add(phaseTypeViewModel);
            }else  if (p instanceof SwissPool){
                SwissPool pool = (SwissPool) p;
                PhaseTypeViewModel phaseTypeViewModel = new PhaseTypeViewModel();
                phaseTypeViewModel.setPhase(pool);
                phaseTypeViewModel.setPhaseType(PhaseType.SWISSPOOL);
                phasesType.add(phaseTypeViewModel);
            }else  if (p instanceof Finnish){
                Finnish pool = (Finnish) p;
                PhaseTypeViewModel phaseTypeViewModel = new PhaseTypeViewModel();
                phaseTypeViewModel.setPhase(pool);
                phaseTypeViewModel.setPhaseType(PhaseType.FINNISH);
                phasesType.add(phaseTypeViewModel);
            }
        }

        model.addAttribute("tournament", tournament);
        model.addAttribute("phasesType", phasesType);
        model.addAttribute(allTournament, tournament);
        model.addAttribute("nbTeam", tournament.getTeams().size());
        return "/tournament/view";


    }
    @GetMapping("/view")
    public String tournamentViewPostLaunch(Model model,@RequestParam(value = "tournamentId", required = false) String tournamentId){

        Tournament tournament = tournamentRepository.findById(Integer.valueOf(tournamentId));

        tournament.setStatus(TournamentStatus.INPROGRESS);

        tournamentRepository.save(tournament);
        model.addAttribute("tournament",tournament);
        return "/tournament/view";
    }



}

package com.molkky.molkky.controllers;


import com.molkky.molkky.domain.Team;
import com.molkky.molkky.domain.Tournament;
import com.molkky.molkky.domain.User;
import com.molkky.molkky.model.TournamentModel;
import com.molkky.molkky.repository.TournamentRepository;
import com.molkky.molkky.repository.UserRepository;
import com.molkky.molkky.repository.UserTounamentRoleRepository;
import com.molkky.molkky.service.TounamentService;
import com.molkky.molkky.service.UserTournamentRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import type.TournamentStatus;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tournament")
public class TournamentController {
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TounamentService tournamentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTounamentRoleRepository userTounamentRoleRepository;

    @Autowired
    private UserTournamentRoleService userTournamentRoleService;

    @GetMapping("/create")
    public String tournamentForm(Model model, HttpSession session) {
        model.addAttribute("tournament", new TournamentModel());
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return "tournament/create";
    }

    @PostMapping("/create")
    public String tournamentSubmit(@Valid @ModelAttribute("tournament") TournamentModel tournament, Model model) {

        Tournament tournamentEntity = tournamentService.create(tournament);

        int id = tournamentEntity.getId();
        return "redirect:/tournament/"+id+"/view";
    }

    @GetMapping("/{id}/view")
    public String tournamentView(Model model, @PathVariable("id") String id){

        //USER FROM SESSION
        User user = null;

        userTournamentRoleService.getTeamUsers(4);


        Tournament tournament = tournamentRepository.findById(Integer.valueOf(id));

        List<Team> teams = tournament.getTeams();
        System.out.println(teams.get(0).getId());

        model.addAttribute("tournament", tournament);
        model.addAttribute("user", user);
        model.addAttribute("nbTeam", tournament.getTeams().size());

        return "tournament/view";
    }



    @PostMapping(value = "/view" , params = "launch")
    public String tournamentViewPostLaunch(@RequestParam(value = "tournamentId", required = false) String tournamentId){

        Tournament tournament = tournamentRepository.findById(Integer.valueOf(tournamentId));

        tournament.setStatus(TournamentStatus.INPROGRESS);

        tournamentRepository.save(tournament);

        return "redirect:/tournament/create";
    }



}
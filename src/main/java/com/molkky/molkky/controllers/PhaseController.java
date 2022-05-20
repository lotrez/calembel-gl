package com.molkky.molkky.controllers;

import com.molkky.molkky.controllers.superclass.DefaultAttributes;
import com.molkky.molkky.domain.Match;
import com.molkky.molkky.domain.Phase;
import com.molkky.molkky.domain.Round;
import com.molkky.molkky.domain.Team;
import com.molkky.molkky.model.UserLogged;
import com.molkky.molkky.repository.PhaseRepository;
import com.molkky.molkky.repository.RoundRepository;
import com.molkky.molkky.service.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import type.PhaseStatus;
import type.PhaseType;
import type.UserRole;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/phase")
public class PhaseController extends DefaultAttributes {

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private RoundRepository roundRepository;

    @GetMapping("/generate")
    public String generate(Model model, HttpSession session, @RequestParam(name = "phase_id", required = true) String id){

        UserLogged user = (UserLogged) session.getAttribute("user");

        if(user == null){
            return "redirect:/connexion";
        }
        if(user.getRole().equals(UserRole.ADM) ){
            Map<Round, List<Match>> response = phaseService.generate(id);

            model.addAttribute("round_match", response);
        }else{
            return "redirect:/";
        }

        return "redirect:/";
    }

    @GetMapping("/create")
    public String phaseForm(Model model, HttpSession session){
//        User user = (User)session.getAttribute("user");
//        model.addAttribute("user", user);

        List<String> options = new ArrayList<>();
        options.add(String.valueOf(PhaseStatus.INPROGRESS));
        options.add(String.valueOf(PhaseStatus.ENDED));
        options.add(String.valueOf(PhaseStatus.NOTSTARTED));
        model.addAttribute("options", options);

        return "phase/create";
    }

    @PostMapping("/create")
    public String phaseSubmit(@Valid @ModelAttribute("phase") Phase phase, Model model) {

        //Phase phaseEntity = phaseService.generate();

        //int id = tournamentEntity.getId();
        return "/phase/view";
    }

    @GetMapping("/view")
    public String view(Model model, HttpSession session, @RequestParam(name= "id") Integer id){

        UserLogged user = (UserLogged) session.getAttribute("user");

        if(user == null){
            return "redirect:/connexion";
        }
        if (user.getRole().equals(UserRole.ADM)) {
            Phase phase = phaseRepository.findById(id);
            List<Round> rounds = phase.getRounds();
            rounds.forEach(element -> {
                if(element.getType() == PhaseType.POOL){
                    model.addAttribute("typeRound","Poule");
                    String name = "teamsRound" + element.getId();
                    model.addAttribute("round" + element.getId(), element);
                    List<Team> teams = roundRepository.findById(element.getId()).getTeams();
                    model.addAttribute(name, teams);
                }
                else if (element.getType() == PhaseType.SIMPLEGAME){

                }

            });


            model.addAttribute("currentPhase", phase);
            model.addAttribute("currentTournament", phase.getTournament());
        } else {
            return "redirect:/";
        }

        return "phase/view";
    }

}

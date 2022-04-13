package com.molkky.molkky.controllers.tournament;

import com.molkky.molkky.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TournamentList {
    @Autowired
    private TournamentRepository tournamentRepository;

    @GetMapping("/tournament")
    public String tournamentList(Model model) {
//        get visible, finished and in progress tournaments
        model.addAttribute("openTournaments", tournamentRepository.findTournamentsByVisibleAndStarted(true, false));
        model.addAttribute("inprogressTournaments", tournamentRepository.findTournamentsByVisibleAndStarted(true, true));
        model.addAttribute("endedTournaments", tournamentRepository.findTournamentsByVisibleAndFinished(true, true));
        return "tournament/list";
    }

}
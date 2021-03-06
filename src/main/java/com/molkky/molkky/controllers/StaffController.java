package com.molkky.molkky.controllers;

import com.molkky.molkky.domain.Tournament;
import com.molkky.molkky.domain.User;
import com.molkky.molkky.domain.UserTournamentRole;
import com.molkky.molkky.model.AddStaff;
import com.molkky.molkky.model.AddStaffList;
import com.molkky.molkky.repository.TournamentRepository;
import com.molkky.molkky.repository.UserRepository;
import com.molkky.molkky.repository.UserTournamentRoleRepository;
import com.molkky.molkky.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import type.UserRole;

import java.util.ArrayList;
import java.util.List;

import static com.molkky.molkky.utility.StringUtilities.createCode;


@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    UserTournamentRoleRepository userTournamentRoleRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @PostMapping("/add")
    public String addStaff(@ModelAttribute("staff") AddStaffList staff){

        Tournament tournament = tournamentRepository.findById(staff.getMails().get(0).getTournamentId());
        List<UserTournamentRole> userTournamentRoles = new ArrayList<>();

        for(AddStaff s: staff.getMails()){
            User user = new User();
            user.setEmail(s.getMail());

            if(!userRepository.existsUserByEmail(s.getMail())){
                user.setPassword(createCode(5));
                user.setEmail(s.getMail());
                user = userRepository.save(user);
            }else{
                user = userRepository.findUserByEmail(user.getEmail());
            }
            emailSenderService.sendEmail(s.getMail(), "Bienvenue sur Molkky", "Bonjour,\n\n" +
                    "Vous avez bien cr???? un tournoi sur Molkky.\n" +
                    "Votre mot de passe est : " + user.getPassword() + "\n\n" +
                    "Bon jeu sur Molkky !\n\n" +
                    "L'??quipe Molkky");
            UserTournamentRole userTournamentRole = new UserTournamentRole();
            userTournamentRole.setTournament(tournament);
            userTournamentRole.setUser(user);
            userTournamentRole.setRole(UserRole.STAFF);
            userTournamentRoles.add(userTournamentRole);

        }

        userTournamentRoleRepository.saveAll(userTournamentRoles);

        return "redirect:/tournament/view?tournamentId=" + tournament.getId();

    }

    boolean areAllDistinct(AddStaffList staff) {
        return staff.getMails().stream().distinct().count() == staff.getMails().size();
    }
}

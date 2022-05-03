package com.molkky.molkky.controllers;

import com.molkky.molkky.domain.User;
import com.molkky.molkky.domain.UserTounamentRole;
import com.molkky.molkky.model.UserConnectionModel;
import com.molkky.molkky.model.UserLogged;
import com.molkky.molkky.repository.UserRepository;
import com.molkky.molkky.repository.UserTounamentRoleRepository;
import com.molkky.molkky.service.ConnexionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ConnexionController {
    @Autowired
    private ConnexionService connexionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserTounamentRoleRepository userTounamentRoleRepository;

    private static final String changePageConnection = "redirect:/connexion";

    @GetMapping("/connexion")
    public String home(Model  model,HttpSession session){
        session.invalidate();
        User user = new User();
        model.addAttribute("user" ,user);

        UserConnectionModel userConnectionModel = new UserConnectionModel();

        model.addAttribute("userConnection", userConnectionModel);
        return "connexion";
    }

    @PostMapping("/connexion")
    public ModelAndView connexionUser(@ModelAttribute("userConnection")UserConnectionModel userModel, HttpServletRequest request){

        try {
            if(userRepository.existsUserByEmailAndPassword(userModel.getEmail(), userModel.getPassword())){
                User user = userRepository.findUserByEmailAndPassword(userModel.getEmail(), userModel.getPassword());
                if(userModel.getCode() != null){
                    List<UserTounamentRole> players = userTounamentRoleRepository.findUserWithCode(user,userModel.getCode());
                    if(players.isEmpty()){
                        UserLogged userLogged = new UserLogged(userModel.getEmail(),
                                userModel.getPassword(), players.get(0).getRole(), players.get(0).getTeam(),players.get(0).getTournament());
                        request.getSession().setAttribute("user",userLogged);
                        return new ModelAndView("redirect:/");
                    }
                    //Creer requete SQL, returns usertournamentRole
                    //if return something
                    //new userlogged
                    //with all the info
                    //sessionScope(userlogged)
                }
                List<UserTounamentRole> adminorstaff = userTounamentRoleRepository.findUserAdminStaff(user);
                if(adminorstaff.size()==1){
                    UserLogged adminStaffLogged = new UserLogged(userModel.getEmail(),userModel.getPassword(),adminorstaff.get(0).getRole(),adminorstaff.get(0).getTournament());
                    request.getSession().setAttribute("user",adminStaffLogged);
                    return new ModelAndView("redirect:/");
                }
                else if(adminorstaff.size()>1){
                    request.getSession().setAttribute("user_temp",user);
                    return new ModelAndView("redirect:/user_choice/choiceTournament") ;
                }
                }else{
                    return new ModelAndView(changePageConnection);
                }

        }catch  (Exception e){
            e.printStackTrace();
        }
        return new ModelAndView(changePageConnection);
    }
}

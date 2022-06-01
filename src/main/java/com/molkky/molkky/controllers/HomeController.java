package com.molkky.molkky.controllers;

import com.molkky.molkky.controllers.superclass.DefaultAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController extends DefaultAttributes {

    @GetMapping("/")
    public String index(Model model, HttpSession session){
        System.out.println(model.getAttribute("mobile"));
        return "/home";
    }

}

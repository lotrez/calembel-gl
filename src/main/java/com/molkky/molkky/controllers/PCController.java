package com.molkky.molkky.controllers;

import com.molkky.molkky.controllers.superclass.DefaultAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PCController extends DefaultAttributes {
    @GetMapping("/pc")
    public String index(Model model, HttpSession session) { return "politiqueConfidentialite"; }
}
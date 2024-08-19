package com.ista.Complexivo7.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UsuarioController {

    @GetMapping("/index")
    public String index(@RequestParam(defaultValue = "0") int page, Model model, Principal principal) {
        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
        model.addAttribute("userPrincipal", userDetails);
        return "index";
    }
    @GetMapping("/login")
    public String login(Model model) {
        return "user/login";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(Model model) {
        model.addAttribute("error", "Access Denied");
        return "user/accessDenied";
    }
}

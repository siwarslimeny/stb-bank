package tn.stb.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

    @GetMapping({"/", "/accueil"})
    public String accueil(Model model) {
        model.addAttribute("page", "accueil");
        return "public/accueil";
    }

    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("page", "services");
        return "public/services";
    }

    @GetMapping("/agences")
    public String agences(Model model) {
        model.addAttribute("page", "agences");
        return "public/agences";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("page", "contact");
        return "public/contact";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
}

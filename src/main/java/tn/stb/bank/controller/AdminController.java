package tn.stb.bank.controller;

import tn.stb.bank.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UtilisateurRepository userRepo;

    @GetMapping("/utilisateurs")
    public String utilisateurs(Model model, @AuthenticationPrincipal UserDetails ud) {
        model.addAttribute("user", userRepo.findByUsername(ud.getUsername()).orElseThrow());
        model.addAttribute("utilisateurs", userRepo.findAll());
        return "admin/utilisateurs";
    }
}

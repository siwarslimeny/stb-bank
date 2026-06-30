package tn.stb.bank.controller;

import tn.stb.bank.model.CompteBancaire;
import tn.stb.bank.repository.ClientRepository;
import tn.stb.bank.repository.UtilisateurRepository;
import tn.stb.bank.service.CompteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comptes")
@RequiredArgsConstructor
public class CompteController {

    private final CompteService compteService;
    private final ClientRepository clientRepo;
    private final UtilisateurRepository userRepo;

    private void addUser(Model model, UserDetails ud) {
        model.addAttribute("user", userRepo.findByUsername(ud.getUsername()).orElseThrow());
    }

    @GetMapping
    public String list(Model model, @AuthenticationPrincipal UserDetails ud) {
        addUser(model, ud);
        model.addAttribute("comptes", compteService.findAll());
        model.addAttribute("totalSoldes", compteService.totalSoldes());
        return "comptes/list";
    }

    @GetMapping("/nouveau")
    public String createForm(Model model, @AuthenticationPrincipal UserDetails ud) {
        addUser(model, ud);
        model.addAttribute("compte", new CompteBancaire());
        model.addAttribute("clients", clientRepo.findAll());
        model.addAttribute("isNew", true);
        return "comptes/form";
    }

    @PostMapping("/nouveau")
    public String create(@ModelAttribute CompteBancaire compte,
                         RedirectAttributes ra,
                         @AuthenticationPrincipal UserDetails ud) {
        compteService.save(compte);
        ra.addFlashAttribute("success", "Compte créé avec succès !");
        return "redirect:/comptes";
    }

    @PostMapping("/{id}/supprimer")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        compteService.delete(id);
        ra.addFlashAttribute("success", "Compte supprimé.");
        return "redirect:/comptes";
    }
}

package tn.stb.bank.controller;

import tn.stb.bank.model.Client;
import tn.stb.bank.repository.UtilisateurRepository;
import tn.stb.bank.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final UtilisateurRepository userRepo;

    private void addUser(Model model, UserDetails ud) {
        model.addAttribute("user",
                userRepo.findByUsername(ud.getUsername()).orElseThrow());
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       Model model,
                       @AuthenticationPrincipal UserDetails ud) {
        addUser(model, ud);
        model.addAttribute("clients", clientService.search(q));
        model.addAttribute("q", q);
        model.addAttribute("totalActifs", clientService.countActifs());
        model.addAttribute("total", clientService.countAll());
        return "clients/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model,
                         @AuthenticationPrincipal UserDetails ud) {
        addUser(model, ud);
        model.addAttribute("client", clientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable")));
        return "clients/detail";
    }

    @GetMapping("/nouveau")
    public String createForm(Model model,
                             @AuthenticationPrincipal UserDetails ud) {
        addUser(model, ud);
        model.addAttribute("client", new Client());
        model.addAttribute("isNew", true);
        return "clients/form";
    }

    @PostMapping("/nouveau")
    public String create(@Valid @ModelAttribute Client client,
                         BindingResult br, Model model,
                         RedirectAttributes ra,
                         @AuthenticationPrincipal UserDetails ud) {
        addUser(model, ud);
        if (br.hasErrors()) {
            model.addAttribute("isNew", true);
            return "clients/form";
        }
        clientService.save(client);
        ra.addFlashAttribute("success", "Client créé avec succès !");
        return "redirect:/clients";
    }

    @GetMapping("/{id}/modifier")
    public String editForm(@PathVariable Long id, Model model,
                           @AuthenticationPrincipal UserDetails ud) {
        addUser(model, ud);
        model.addAttribute("client", clientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable")));
        model.addAttribute("isNew", false);
        return "clients/form";
    }

    @PostMapping("/{id}/modifier")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Client client,
                         BindingResult br, Model model,
                         RedirectAttributes ra,
                         @AuthenticationPrincipal UserDetails ud) {
        addUser(model, ud);
        if (br.hasErrors()) {
            model.addAttribute("isNew", false);
            return "clients/form";
        }
        client.setId(id);
        clientService.save(client);
        ra.addFlashAttribute("success", "Client mis à jour !");
        return "redirect:/clients";
    }

    @PostMapping("/{id}/supprimer")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        clientService.delete(id);
        ra.addFlashAttribute("success", "Client supprimé.");
        return "redirect:/clients";
    }
}
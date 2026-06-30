package tn.stb.bank.controller;

import tn.stb.bank.model.*;
import tn.stb.bank.repository.CompteRepository;
import tn.stb.bank.repository.UtilisateurRepository;
import tn.stb.bank.service.EmployeService;
import tn.stb.bank.service.TransactionService;
import tn.stb.bank.service.VirementService;
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
@RequestMapping("/transactions")
@RequiredArgsConstructor
class TransactionController {

    private final TransactionService txService;
    private final CompteRepository compteRepo;
    private final UtilisateurRepository userRepo;

    private void addUser(Model m, UserDetails ud) {
        m.addAttribute("user",
                userRepo.findByUsername(ud.getUsername()).orElseThrow());
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       Model m, @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        m.addAttribute("transactions", txService.search(q));
        m.addAttribute("q", q);
        m.addAttribute("totalCredits", txService.totalCredits());
        m.addAttribute("totalDebits", txService.totalDebits());
        m.addAttribute("count", txService.count());
        return "transactions/list";
    }

    @GetMapping("/nouvelle")
    public String createForm(Model m, @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        m.addAttribute("tx", new Transaction());
        m.addAttribute("comptes", compteRepo.findAll());
        m.addAttribute("isNew", true);
        return "transactions/form";
    }

    @PostMapping("/nouvelle")
    public String create(@Valid @ModelAttribute("tx") Transaction tx,
                         BindingResult br, Model m, RedirectAttributes ra,
                         @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        if (br.hasErrors()) {
            m.addAttribute("comptes", compteRepo.findAll());
            m.addAttribute("isNew", true);
            return "transactions/form";
        }
        Utilisateur u = userRepo.findByUsername(ud.getUsername()).orElseThrow();
        tx.setOperateur(u);
        txService.save(tx);
        ra.addFlashAttribute("success", "Transaction enregistrée !");
        return "redirect:/transactions";
    }

    @GetMapping("/{id}/modifier")
    public String editForm(@PathVariable Long id, Model m,
                           @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        m.addAttribute("tx", txService.findById(id).orElseThrow());
        m.addAttribute("comptes", compteRepo.findAll());
        m.addAttribute("isNew", false);
        return "transactions/form";
    }

    @PostMapping("/{id}/modifier")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("tx") Transaction tx,
                         BindingResult br, Model m, RedirectAttributes ra,
                         @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        if (br.hasErrors()) {
            m.addAttribute("comptes", compteRepo.findAll());
            m.addAttribute("isNew", false);
            return "transactions/form";
        }
        tx.setId(id);
        txService.save(tx);
        ra.addFlashAttribute("success", "Transaction mise à jour !");
        return "redirect:/transactions";
    }

    @PostMapping("/{id}/supprimer")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        txService.delete(id);
        ra.addFlashAttribute("success", "Transaction supprimée.");
        return "redirect:/transactions";
    }
}

@Controller
@RequestMapping("/employes")
@RequiredArgsConstructor
class EmployeController {

    private final EmployeService empService;
    private final UtilisateurRepository userRepo;

    private void addUser(Model m, UserDetails ud) {
        m.addAttribute("user",
                userRepo.findByUsername(ud.getUsername()).orElseThrow());
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       Model m, @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        m.addAttribute("employes", empService.search(q));
        m.addAttribute("q", q);
        m.addAttribute("count", empService.count());
        m.addAttribute("avgSalaire", empService.avgSalaire());
        return "employes/list";
    }

    @GetMapping("/nouveau")
    public String createForm(Model m, @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        m.addAttribute("employe", new Employe());
        m.addAttribute("isNew", true);
        return "employes/form";
    }

    @PostMapping("/nouveau")
    public String create(@Valid @ModelAttribute Employe employe,
                         BindingResult br, Model m, RedirectAttributes ra,
                         @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        if (br.hasErrors()) {
            m.addAttribute("isNew", true);
            return "employes/form";
        }
        empService.save(employe);
        ra.addFlashAttribute("success", "Employé ajouté avec succès !");
        return "redirect:/employes";
    }

    @GetMapping("/{id}/modifier")
    public String editForm(@PathVariable Long id, Model m,
                           @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        m.addAttribute("employe", empService.findById(id).orElseThrow());
        m.addAttribute("isNew", false);
        return "employes/form";
    }

    @PostMapping("/{id}/modifier")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Employe employe,
                         BindingResult br, Model m, RedirectAttributes ra,
                         @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        if (br.hasErrors()) {
            m.addAttribute("isNew", false);
            return "employes/form";
        }
        employe.setId(id);
        empService.save(employe);
        ra.addFlashAttribute("success", "Dossier employé mis à jour !");
        return "redirect:/employes";
    }

    @PostMapping("/{id}/supprimer")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        empService.delete(id);
        ra.addFlashAttribute("success", "Employé supprimé.");
        return "redirect:/employes";
    }
}

@Controller
@RequestMapping("/virements")
@RequiredArgsConstructor
class VirementController {

    private final VirementService virService;
    private final CompteRepository compteRepo;
    private final UtilisateurRepository userRepo;

    private void addUser(Model m, UserDetails ud) {
        m.addAttribute("user",
                userRepo.findByUsername(ud.getUsername()).orElseThrow());
    }

    @GetMapping
    public String list(Model m, @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        m.addAttribute("virements", virService.findAll());
        return "virements/list";
    }

    @GetMapping("/nouveau")
    public String createForm(Model m, @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        m.addAttribute("virement", new Virement());
        m.addAttribute("comptes", compteRepo.findAll());
        return "virements/form";
    }

    @PostMapping("/nouveau")
    public String create(@Valid @ModelAttribute("virement") Virement v,
                         BindingResult br, Model m, RedirectAttributes ra,
                         @AuthenticationPrincipal UserDetails ud) {
        addUser(m, ud);
        if (br.hasErrors()) {
            m.addAttribute("comptes", compteRepo.findAll());
            return "virements/form";
        }
        Utilisateur u = userRepo.findByUsername(ud.getUsername()).orElseThrow();
        v.setOperateur(u);
        virService.save(v);
        ra.addFlashAttribute("success", "Ordre de virement soumis !");
        return "redirect:/virements";
    }

    @PostMapping("/{id}/valider")
    public String valider(@PathVariable Long id, RedirectAttributes ra) {
        virService.valider(id);
        ra.addFlashAttribute("success", "Virement validé et exécuté !");
        return "redirect:/virements";
    }

    @PostMapping("/{id}/rejeter")
    public String rejeter(@PathVariable Long id,
                          @RequestParam(defaultValue = "Motif non précisé") String motif,
                          RedirectAttributes ra) {
        virService.rejeter(id, motif);
        ra.addFlashAttribute("error", "Virement rejeté.");
        return "redirect:/virements";
    }

    @PostMapping("/{id}/supprimer")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        virService.delete(id);
        ra.addFlashAttribute("success", "Virement supprimé.");
        return "redirect:/virements";
    }
}
package tn.stb.bank.controller;

import tn.stb.bank.model.Virement;
import tn.stb.bank.repository.UtilisateurRepository;
import tn.stb.bank.service.ClientService;
import tn.stb.bank.service.EmployeService;
import tn.stb.bank.service.TransactionService;
import tn.stb.bank.service.VirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ClientService clientService;
    private final TransactionService transactionService;
    private final EmployeService employeService;
    private final VirementService virementService;
    private final UtilisateurRepository userRepo;

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            @AuthenticationPrincipal UserDetails userDetails) {
        var user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("totalClients", clientService.countAll());
        model.addAttribute("clientsActifs", clientService.countActifs());
        model.addAttribute("totalTransactions", transactionService.count());
        model.addAttribute("totalCredits", transactionService.totalCredits());
        model.addAttribute("totalDebits", transactionService.totalDebits());
        model.addAttribute("totalEmployes", employeService.count());
        model.addAttribute("avgSalaire", employeService.avgSalaire());
        model.addAttribute("recentTransactions",
                transactionService.findAll().stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("pendingVirements",
                virementService.findAll().stream()
                        .filter(v -> v.getStatut() == Virement.StatutVirement.EN_ATTENTE)
                        .limit(5).collect(Collectors.toList()));
        return "dashboard/home";
    }
}
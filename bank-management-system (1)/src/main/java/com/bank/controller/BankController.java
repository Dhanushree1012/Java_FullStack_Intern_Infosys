package com.bank.controller;

import com.bank.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class BankController {
    private final BankService bank;

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (admin) return "redirect:/admin";
        model.addAttribute("account", bank.getAccount(auth.getName()));
        model.addAttribute("transactions", bank.history(auth.getName()));
        return "dashboard";
    }

    @PostMapping("/deposit")
    public String deposit(Authentication auth, @RequestParam BigDecimal amount, RedirectAttributes ra) {
        try { bank.deposit(auth.getName(), amount); ra.addFlashAttribute("success", "Deposit successful"); }
        catch (IllegalArgumentException e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(Authentication auth, @RequestParam BigDecimal amount, RedirectAttributes ra) {
        try { bank.withdraw(auth.getName(), amount); ra.addFlashAttribute("success", "Withdrawal successful"); }
        catch (IllegalArgumentException e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/dashboard";
    }

    @PostMapping("/transfer")
    public String transfer(Authentication auth, @RequestParam String toAccount,
                           @RequestParam BigDecimal amount, RedirectAttributes ra) {
        try { bank.transfer(auth.getName(), toAccount, amount); ra.addFlashAttribute("success", "Transfer successful"); }
        catch (IllegalArgumentException e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/dashboard";
    }
}

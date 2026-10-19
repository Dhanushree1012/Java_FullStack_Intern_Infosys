package com.bank.controller;

import com.bank.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final BankService bank;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("customers", bank.allCustomers());
        return "admin";
    }
}

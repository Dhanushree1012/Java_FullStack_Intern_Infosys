package com.bank.service;

import com.bank.entity.*;
import com.bank.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class BankService {
    private final UserRepository users;
    private final AccountRepository accounts;
    private final TransactionRepository txns;
    private final PasswordEncoder encoder;

    @Transactional
    public void register(String username, String password, String fullName, String email) {
        if (username.isBlank() || fullName.isBlank() || email.isBlank())
            throw new IllegalArgumentException("All fields are required");
        if (password.length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters");
        if (users.existsByUsername(username.trim()))
            throw new IllegalArgumentException("Username already taken");

        User u = new User();
        u.setUsername(username.trim());
        u.setPassword(encoder.encode(password));
        u.setFullName(fullName.trim());
        u.setEmail(email.trim());
        u.setRole("ROLE_CUSTOMER");

        Account a = new Account();
        a.setAccountNumber(newAccountNumber());
        a.setUser(u);
        u.setAccount(a);
        users.save(u);   // cascades to Account
    }

    public Account getAccount(String username) {
        return accounts.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public List<Transaction> history(String username) {
        return txns.findByAccountOrderByCreatedAtDesc(getAccount(username));
    }

    public List<User> allCustomers() {
        return users.findByRole("ROLE_CUSTOMER");
    }

    @Transactional
    public void deposit(String username, BigDecimal amount) {
        validate(amount);
        Account a = getAccount(username);
        a.setBalance(a.getBalance().add(amount));
        log(a, "DEPOSIT", amount, "Cash deposit");
    }

    @Transactional
    public void withdraw(String username, BigDecimal amount) {
        validate(amount);
        Account a = getAccount(username);
        if (a.getBalance().compareTo(amount) < 0)
            throw new IllegalArgumentException("Insufficient balance");
        a.setBalance(a.getBalance().subtract(amount));
        log(a, "WITHDRAW", amount, "Cash withdrawal");
    }

    @Transactional
    public void transfer(String username, String toAccountNumber, BigDecimal amount) {
        validate(amount);
        Account from = getAccount(username);
        Account to = accounts.findByAccountNumber(toAccountNumber.trim())
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));
        if (from.getId().equals(to.getId()))
            throw new IllegalArgumentException("Cannot transfer to your own account");
        if (from.getBalance().compareTo(amount) < 0)
            throw new IllegalArgumentException("Insufficient balance");

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        log(from, "TRANSFER_OUT", amount, "Sent to " + to.getAccountNumber());
        log(to, "TRANSFER_IN", amount, "Received from " + from.getAccountNumber());
    }

    private void validate(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero");
    }

    private void log(Account a, String type, BigDecimal amount, String desc) {
        Transaction t = new Transaction();
        t.setAccount(a); t.setType(type); t.setAmount(amount); t.setDescription(desc);
        txns.save(t);
    }

    private String newAccountNumber() {
        String no;
        do {
            no = "SB" + (1_000_000_000L + ThreadLocalRandom.current().nextLong(9_000_000_000L));
        } while (accounts.existsByAccountNumber(no));
        return no;
    }
}

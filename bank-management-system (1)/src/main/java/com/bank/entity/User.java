package com.bank.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;      // stored BCrypt-encrypted
    private String fullName;
    private String email;
    private String role;          // ROLE_ADMIN or ROLE_CUSTOMER

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Account account;
}

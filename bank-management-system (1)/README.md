# SB Bank - Banking Management System (Spring Boot + MySQL)

**Stack:** Java 17, Spring Boot 3.2.1, Spring Security, Spring Data JPA, Thymeleaf, MySQL

## Features
- Customer registration and login (BCrypt-encrypted passwords)
- Auto-generated account number for every customer
- Deposit, withdraw and transfer between accounts (transactional)
- Transaction history
- Admin panel listing all customers and balances

## Run
1. Make sure MySQL is running.
2. Open `src/main/resources/application.properties` and set your MySQL password.
   The `bankdb` database and all tables are created automatically.
3. Run `mvn spring-boot:run` (or run `BankApplication` from IntelliJ).
4. Open http://localhost:8080

## Default admin
`admin` / `admin123`   (created on first start)

## Package structure
`entity` -> `repository` -> `service` -> `controller`, plus `config` for security.

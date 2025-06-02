package com.example.qr_bank.repository;

import com.example.qr_bank.model.Account;
import com.example.qr_bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    List<Account> findAllByOwnerId(String userId);

    boolean existsAccountByIban(String iban);

    Optional<Account> findByIban(String iban);

    Optional<Account> findByOwner(User owner);

}
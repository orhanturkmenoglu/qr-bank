package com.example.qr_bank.repository;

import com.example.qr_bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    boolean existsByIdentityNumber(String identityNumber);

    boolean existsByTelephoneNumber(String telephoneNumber);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdentificationNumber(String identificationNumber);

    Optional<User> findByTelephoneNumber(String telephoneNumber);
}
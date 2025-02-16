package com.jb.repository;

import com.jb.entity.Company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByEmailAndPassword(String email, String password);

    Optional<Company> findByUuid(UUID uuid);

}

package com.jb.repository;

import com.jb.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByEmailAndPassword(String email, String password);

    Optional<Customer> findByUuid(UUID uuid);

}

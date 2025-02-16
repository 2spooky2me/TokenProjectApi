package com.jb.repository;

import com.jb.entity.Coupons;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponsRepository extends JpaRepository<Coupons, Integer> {

    Optional<Coupons> findByUuid(UUID uuid);

    List<Coupons> findByCustomers_UuidAndEndBefore(UUID uuid, LocalDate endDate);

    List<Coupons> findByCustomers_Uuid(UUID customerUuid);

    List<Coupons> findByCustomers_UuidNot(UUID customerUuid);

    void deleteByEndBefore(LocalDateTime endDate);

}

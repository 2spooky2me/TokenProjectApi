package com.jb.service;

import com.jb.dto.CompanyDto;
import com.jb.dto.CouponsDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyService {

    Optional<CompanyDto> getCompany(UUID uuid);

    List<CouponsDto> getAllCoupons(UUID companyId);

    CouponsDto addCoupon(CouponsDto couponsDto, UUID companyId);

    Optional<CompanyDto> deleteCoupon(UUID couponUuid, UUID companyId);

    Optional<CouponsDto> updateCouponAmount(UUID companyId, UUID couponId, int newAmount);
}

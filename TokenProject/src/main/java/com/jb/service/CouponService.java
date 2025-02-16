package com.jb.service;

import com.jb.dto.CouponsDto;

import java.util.Optional;
import java.util.UUID;

public interface CouponService {
    Optional<CouponsDto> getCouponById(UUID couponId);
}

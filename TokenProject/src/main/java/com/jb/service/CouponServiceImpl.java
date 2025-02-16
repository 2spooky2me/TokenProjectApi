package com.jb.service;

import com.jb.dto.CouponsDto;
import com.jb.entity.Coupons;
import com.jb.mapper.CouponsMapper;
import com.jb.repository.CouponsRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CrossOrigin
public class CouponServiceImpl implements CouponService {

    private final CouponsRepository couponsRepository;
    private final CouponsMapper couponsMapper;


    @Override
    public Optional<CouponsDto> getCouponById(UUID couponId) {
        Optional<Coupons> coupon = couponsRepository.findByUuid(couponId);

        return coupon.map(couponsMapper::toDto);
    }
}

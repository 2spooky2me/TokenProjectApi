package com.jb.controller;

import com.jb.dto.CouponsDto;
import com.jb.service.CouponService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
@CrossOrigin

public class CouponController {
    private final CouponService couponService;

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponsDto> getCouponById(@PathVariable UUID couponId) {
        Optional<CouponsDto> coupon = couponService.getCouponById(couponId);

        return coupon.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}

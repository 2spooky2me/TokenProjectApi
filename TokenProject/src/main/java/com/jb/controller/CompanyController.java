package com.jb.controller;

import com.jb.client.ClientSession;
import com.jb.client.ClientType;
import com.jb.client.CompanyAuthenticatedInfo;
import com.jb.dto.CompanyDto;
import com.jb.dto.CouponsDto;
import com.jb.service.CompanyService;
import com.jb.service.ex.InvalidLoginException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@CrossOrigin
public class CompanyController {

    private final Map<String, ClientSession> tokens;
    private final CompanyService companyService;


    @GetMapping("/all")
    public ResponseEntity<List<CouponsDto>> getAllCoupons(@RequestHeader("Authorization") String token) {
        CompanyAuthenticatedInfo companyDto = getCompanyDtoFromToken(token);

        if (companyDto.getClientType() == ClientType.COMPANY) {
            UUID companyId = companyDto.getCompanyDto().getUuid();
            List<CouponsDto> allCoupons = companyService.getAllCoupons(companyId);
            return ResponseEntity.ok(allCoupons);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/add-coupon")
    @Transactional
    public ResponseEntity<CouponsDto> addCoupon(
            @RequestBody CouponsDto couponDto,
            @RequestHeader("Authorization") String token, UriComponentsBuilder uriBuilder) {

        CompanyAuthenticatedInfo companyInfo = getCompanyDtoFromToken(token);

        if (companyInfo.getClientType() == ClientType.COMPANY) {
            UUID companyId = companyInfo.getCompanyDto().getUuid();

            CouponsDto addedCoupon = companyService.addCoupon(couponDto, companyId);
            UriComponents uriComponents = uriBuilder
                    .path("/api/coupon/{couponId}")
                    .buildAndExpand(addedCoupon.getUuid());

            return ResponseEntity
                    .created(uriComponents.toUri())
                    .body(addedCoupon);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/delete-coupon/{couponUuid}")
    @Transactional
    public ResponseEntity<Object> deleteCoupon(
            @PathVariable UUID couponUuid,
            @RequestHeader("Authorization") String token) {

        CompanyAuthenticatedInfo companyInfo = getCompanyDtoFromToken(token);

        if (companyInfo.getClientType() == ClientType.COMPANY) {
            UUID companyId = companyInfo.getCompanyDto().getUuid();


            Optional<CompanyDto> deletedCoupon = companyService.deleteCoupon(couponUuid,
                                                                             companyId);
            if (deletedCoupon.isPresent()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/coupons/{couponId}/newAmount")
    public ResponseEntity<Object> updateCouponAmount(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID couponId,
            @RequestParam int newAmount) {
        CompanyAuthenticatedInfo companyInfo = getCompanyDtoFromToken(token);

        if (companyInfo.getClientType() == ClientType.COMPANY) {
            UUID companyIdFromToken = companyInfo.getCompanyDto().getUuid();

            Optional<CouponsDto> updatedCoupon = companyService.updateCouponAmount(
                    companyIdFromToken,
                    couponId,
                    newAmount);

            return updatedCoupon.<ResponseEntity<Object>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Coupon not found"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private CompanyAuthenticatedInfo getCompanyDtoFromToken(String token) {
        ClientSession clientSession = tokens.get(token);

        if (clientSession == null) {
            throw new InvalidLoginException("Invalid or expired token");
        }

        clientSession.access();

        UUID companyId = clientSession.getUuid();
        ClientType clientType = clientSession.getClientType();

        Optional<CompanyDto> companyDto = companyService.getCompany(companyId);

        if (companyDto.isPresent()) {
            return new CompanyAuthenticatedInfo(companyDto.get(), clientType);

        } else {
            throw new InvalidLoginException("Invalid or expired token");
        }
    }
}

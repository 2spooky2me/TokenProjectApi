package com.jb.service;

import com.jb.dto.CompanyDto;
import com.jb.dto.CouponsDto;
import com.jb.entity.Company;
import com.jb.entity.Coupons;
import com.jb.mapper.CompanyMapper;
import com.jb.mapper.CouponsMapper;
import com.jb.repository.CompanyRepository;
import com.jb.repository.CouponsRepository;
import com.jb.service.ex.NoSuchCompanyException;
import com.jb.service.ex.UnauthorizedCouponAccessException;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
@CrossOrigin
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CouponsRepository couponsRepository;
    private final CompanyMapper companyMapper;
    private final CouponsMapper couponsMapper;

    @Override
    public Optional<CompanyDto> getCompany(UUID uuid) {
        return companyRepository.findByUuid(uuid)
                .map(companyMapper::toDto);
    }

    @Override
    public List<CouponsDto> getAllCoupons(UUID companyId) {
        Optional<Company> optCompany = companyRepository.findByUuid(companyId);
        if (optCompany.isEmpty()) {
            throw new NoSuchCompanyException(String.format("No such company for uuid: %s",
                                                           companyId));
        }
        Company company = optCompany.get();
        Set<Coupons> coupons = company.getCoupons();
        return coupons.stream()
                .map(couponsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CouponsDto addCoupon(CouponsDto couponDto, UUID companyId) {
        Optional<Company> optCompany = companyRepository.findByUuid(companyId);
        if (optCompany.isEmpty()) {
            throw new NoSuchCompanyException(String.format("No such company for uuid: %s",
                                                           companyId));
        }

        Company company = optCompany.get();
        Coupons coupon = couponsMapper.toEntity(couponDto);

        UUID randomUUID = UUID.randomUUID();
        coupon.setUuid(randomUUID);

        Date nowDate = new Date(System.currentTimeMillis());
        coupon.setStart_data(nowDate);
        couponsRepository.save(coupon);

        coupon.setCompany(company);
        company.getCoupons().add(coupon);

        companyRepository.save(company);
        return couponsMapper.toDto(coupon);
    }


    @SneakyThrows
    @Transactional
    public Optional<CompanyDto> deleteCoupon(UUID couponUuid, UUID companyId) {
        Optional<Company> optCompany = companyRepository.findByUuid(companyId);
        if (optCompany.isEmpty()) {
            throw new NoSuchCompanyException(String.format("No such company for uuid: %s",
                                                           companyId));
        }

        Company company = optCompany.get();

        Optional<Coupons> optCoupon = couponsRepository.findByUuid(couponUuid);

        if (optCoupon.isPresent()) {
            Coupons coupon = optCoupon.get();

            if (coupon.getCompany().getId() == company.getId()) {
                company.getCoupons().remove(coupon);
                couponsRepository.delete(coupon);
                return Optional.of(companyMapper.toDto(company));

            } else {
            throw new UnauthorizedCouponAccessException("Coupon does not belong to the company");}

        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<CouponsDto> updateCouponAmount(UUID companyId, UUID couponId, int newAmount) {
        Optional<Company> optCompany = companyRepository.findByUuid(companyId);
        if (optCompany.isEmpty()) {
            throw new NoSuchCompanyException(String.format("No such company for uuid: %s",
                                                           companyId));
        }

        Optional<Coupons> optCoupon = couponsRepository.findByUuid(couponId);

        if (optCoupon.isPresent()) {
            Coupons coupon = optCoupon.get();
            coupon.setAmount(newAmount);
            couponsRepository.save(coupon);
            return Optional.of(couponsMapper.toDto(coupon));
        } else {
            return Optional.empty();
        }
    }

}

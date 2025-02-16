package com.jb.service;

import com.jb.dto.CouponsDto;
import com.jb.dto.CustomerDto;
import com.jb.entity.Coupons;
import com.jb.entity.Customer;
import com.jb.mapper.CouponsMapper;
import com.jb.mapper.CustomerMapper;
import com.jb.repository.CouponsRepository;
import com.jb.repository.CustomerRepository;
import com.jb.service.ex.CouponNotFoundException;
import com.jb.service.ex.CouponPurchaseException;
import com.jb.service.ex.CustomerNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CrossOrigin
public class CustomerServiceImpl implements CustomerService {

    private final CouponsRepository couponsRepository;
    private final CustomerRepository customerRepository;
    private final CouponsMapper couponsMapper;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<CustomerDto> getCustomer(UUID uuid) {
        return customerRepository.findByUuid(uuid)
                .map(customerMapper::toDto);
    }

    public List<CouponsDto> getExpiringCouponsForCustomer(UUID customerUuid) {
        LocalDate expirationDateThreshold = LocalDate.now().plusDays(7);
        List<Coupons> expiringCoupons = couponsRepository.findByCustomers_UuidAndEndBefore(
                customerUuid,
                expirationDateThreshold);
        return couponsMapper.toDtoList(expiringCoupons);
    }

    @Override
    public List<CouponsDto> getPurchasedCouponsForCustomer(UUID customerUuid) {
        List<Coupons> purchasedCoupons = couponsRepository.findByCustomers_Uuid(customerUuid);
        return couponsMapper.toDtoList(purchasedCoupons);
    }

    @Override
    public List<CouponsDto> getUnpurchasedCouponsForCustomer(UUID customerUuid) {
        List<Coupons> allCoupons = couponsRepository.findAll();
        List<Coupons> purchasedCoupons = couponsRepository.findByCustomers_Uuid(customerUuid);

        allCoupons.removeAll(purchasedCoupons);

        return couponsMapper.toDtoList(allCoupons);
    }


    public void purchaseCoupon(UUID customerUuid, UUID couponUuid) {
        Customer customer = customerRepository.findByUuid(customerUuid)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Coupons coupon = couponsRepository.findByUuid(couponUuid)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

        if (coupon.getAmount() <= 0) {
            throw new CouponPurchaseException("No available coupons for purchase");
        }

        if (customer.getCoupons().contains(coupon)) {
            throw new CouponPurchaseException("Customer already purchased this coupon");
        }

        coupon.setAmount(coupon.getAmount() - 1);
        customer.getCoupons().add(coupon);

        couponsRepository.save(coupon);
    }

    @Override
    @Transactional
    public Customer addCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());

        if (customer.getUuid() == null) {
            customer.setUuid(UUID.randomUUID());
        }

        return customerRepository.save(customer);

    }

    @Override
    @Transactional
    public void updateCustomerEmail(UUID customerUuid, String newEmail) {
        Customer customer = customerRepository.findByUuid(customerUuid)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customer.setEmail(newEmail);
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void updateCustomerPassword(UUID customerUuid, String newPassword) {
        Customer customer = customerRepository.findByUuid(customerUuid)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));


        customer.setPassword(newPassword);

        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void updateCustomerName(UUID customerUuid, String firstName, String lastName) {
        Customer customer = customerRepository.findByUuid(customerUuid)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customerRepository.save(customer);
    }

}

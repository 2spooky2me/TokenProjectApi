package com.jb.service;

import com.jb.dto.CouponsDto;
import com.jb.dto.CustomerDto;
import com.jb.entity.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Optional<CustomerDto> getCustomer(UUID uuid);

    List<CouponsDto> getExpiringCouponsForCustomer(UUID customerUuid);

    List<CouponsDto> getPurchasedCouponsForCustomer(UUID customerUuid);

    List<CouponsDto> getUnpurchasedCouponsForCustomer(UUID customerUuid);

    void purchaseCoupon(UUID customerUuid, UUID couponUuid);

     void updateCustomerPassword(UUID customerUuid, String newPassword);

    Customer addCustomer(CustomerDto customerDto);

    void updateCustomerEmail(UUID customerUuid, String newEmail);

    void updateCustomerName(UUID customerUuid, String firstName, String lastName);

}

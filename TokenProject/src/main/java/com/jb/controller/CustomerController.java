package com.jb.controller;

import com.jb.client.ClientSession;
import com.jb.client.ClientType;
import com.jb.client.CustomerAuthenticatedInfo;
import com.jb.dto.CouponsDto;
import com.jb.dto.CustomerDto;
import com.jb.entity.Customer;
import com.jb.service.CustomerService;
import com.jb.service.ex.CouponNotFoundException;
import com.jb.service.ex.CouponPurchaseException;
import com.jb.service.ex.InvalidLoginException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerController {

    private final CustomerService customerService;
    private final Map<String, ClientSession> tokens;

    @GetMapping("/in-a-week")
    public ResponseEntity<List<CouponsDto>> getExpiringCouponsForCustomer(@RequestHeader(
            "Authorization") String token) {
        CustomerAuthenticatedInfo customerDto = getCustomerDtoFromToken(token);

        if (customerDto.getClientType() == ClientType.CUSTOMER) {
            UUID customerUuid = customerDto.getCustomerDto().getUuid();
            List<CouponsDto> expiringCoupons = customerService.getExpiringCouponsForCustomer(
                    customerUuid);
            return ResponseEntity.ok(expiringCoupons);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/purchase/{couponUuid}")
    public ResponseEntity<Object> purchaseCoupon(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID couponUuid) {

        CustomerAuthenticatedInfo customerInfo = getCustomerDtoFromToken(token);

        if (customerInfo.getClientType() == ClientType.CUSTOMER) {
            UUID customerUuid = customerInfo.getCustomerDto().getUuid();
            try {
                customerService.purchaseCoupon(customerUuid, couponUuid);
                return ResponseEntity.noContent().build();

            } catch (CouponPurchaseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

            } catch (CouponNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/all/purchased")
    public ResponseEntity<List<CouponsDto>> getPurchasedCoupons(@RequestHeader("Authorization") String token) {
        CustomerAuthenticatedInfo customerDto = getCustomerDtoFromToken(token);

        if (customerDto.getClientType() == ClientType.CUSTOMER) {
            UUID customerUuid = customerDto.getCustomerDto().getUuid();
            List<CouponsDto> purchasedCoupons = customerService.getPurchasedCouponsForCustomer(
                    customerUuid);
            return ResponseEntity.ok(purchasedCoupons);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/all/not-purchased")
    public ResponseEntity<List<CouponsDto>> getUnpurchasedCoupons(@RequestHeader("Authorization") String token) {
        try {
            CustomerAuthenticatedInfo customerInfo = getCustomerDtoFromToken(token);
            if (customerInfo.getClientType() == ClientType.CUSTOMER) {
                UUID customerUuid = customerInfo.getCustomerDto().getUuid();

                List<CouponsDto> unPurchasedCoupons = customerService.getUnpurchasedCouponsForCustomer(customerUuid);
                return ResponseEntity.ok(unPurchasedCoupons);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (InvalidLoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        Customer savedCustomer = customerService.addCustomer(customerDto);


        CustomerDto savedCustomerDto = new CustomerDto();
        savedCustomerDto.setId(savedCustomer.getId());
        savedCustomerDto.setUuid(savedCustomer.getUuid());
        savedCustomerDto.setFirstName(savedCustomer.getFirstName());
        savedCustomerDto.setLastName(savedCustomer.getLastName());
        savedCustomerDto.setEmail(savedCustomer.getEmail());

        return new ResponseEntity<>(savedCustomerDto, HttpStatus.CREATED);
    }

    @PostMapping("/update/password")
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> updateRequest) {
        try {
            String newPassword = updateRequest.get("password");
            if (newPassword == null || newPassword.isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            CustomerAuthenticatedInfo customerInfo = getCustomerDtoFromToken(token);
            if (customerInfo.getClientType() != ClientType.CUSTOMER) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            customerService.updateCustomerPassword(customerInfo.getCustomerDto().getUuid(), newPassword);
            return ResponseEntity.ok("Password updated successfully");
        } catch (InvalidLoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @PostMapping("/update/email")
    public ResponseEntity<String> updateEmail(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> updateRequest) {
        try {
            String newEmail = updateRequest.get("email");
            if (newEmail == null || newEmail.isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }

            CustomerAuthenticatedInfo customerInfo = getCustomerDtoFromToken(token);
            if (customerInfo.getClientType() != ClientType.CUSTOMER) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            customerService.updateCustomerEmail(customerInfo.getCustomerDto().getUuid(), newEmail);
            return ResponseEntity.ok("Email updated successfully");

        } catch (InvalidLoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @PostMapping("/update/nameAndLastName")
    public ResponseEntity<String> updateName(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> updateRequest) {
        try {
            String firstName = updateRequest.get("firstName");
            String lastName = updateRequest.get("lastName");
            if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
                return ResponseEntity.badRequest().body("First name and last name are required");
            }

            CustomerAuthenticatedInfo customerInfo = getCustomerDtoFromToken(token);
            if (customerInfo.getClientType() != ClientType.CUSTOMER) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            customerService.updateCustomerName(customerInfo.getCustomerDto().getUuid(),
                                               firstName,
                                               lastName);
            return ResponseEntity.ok("Name updated successfully");
        } catch (InvalidLoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }


    private CustomerAuthenticatedInfo getCustomerDtoFromToken(String token) {
        ClientSession clientSession = tokens.get(token);

        if (clientSession == null) {
            throw new InvalidLoginException("Invalid or expired token");
        }

        clientSession.access();

        UUID customerId = clientSession.getUuid();
        ClientType clientType = clientSession.getClientType();

        Optional<CustomerDto> customerDto = customerService.getCustomer(customerId);

        if (customerDto.isPresent()) {
            return new CustomerAuthenticatedInfo(customerDto.get(), clientType);

        } else {
            throw new InvalidLoginException("Invalid or expired token");
        }
    }
}
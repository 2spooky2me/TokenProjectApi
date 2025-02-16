package com.jb.controller;

import com.jb.client.ClientSession;
import com.jb.client.LoginCredentials;
import com.jb.entity.Customer;
import com.jb.repository.CustomerRepository;
import com.jb.service.LoginService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin

public class LoginController {

    private final Map<String, ClientSession> tokens;
    private final LoginService loginService;
    private final CustomerRepository customerRepository;

    @PostMapping("/company/login")
    public ResponseEntity<String> companyHandleLogin(@RequestBody LoginCredentials loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        ClientSession session = loginService.companyCreateSession(email, password);

        String token = loginService.generateToken();
        tokens.put(token, session);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/customer/login")
    public ResponseEntity<Map<String, String>> customerHandleLogin(@RequestBody LoginCredentials loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        ClientSession session = loginService.customerCreateSession(email, password);

        Optional<Customer> customer = customerRepository.findByEmailAndPassword(email, password);

        if(customer.isPresent()) {
            String token = loginService.generateToken();
            tokens.put(token, session);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("firstName", customer.get().getFirstName());
            response.put("lastName", customer.get().getLastName());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


}

package com.jb.service;

import com.jb.client.ClientSession;
import com.jb.client.ClientType;
import com.jb.entity.Company;
import com.jb.entity.Customer;
import com.jb.repository.CompanyRepository;
import com.jb.repository.CustomerRepository;
import com.jb.service.ex.InvalidLoginException;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CrossOrigin
public class LoginServiceImpl implements LoginService {

    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;

    @Override
    public ClientSession companyCreateSession(String email, String password) {
        Optional<Company> optCompany = companyRepository.findByEmailAndPassword(email, password);

        if (optCompany.isEmpty()) {
            throw new InvalidLoginException("There is no such company");
        }
        Company company = optCompany.get();
        UUID companyId = company.getUuid();

        return ClientSession.of(companyId, ClientType.COMPANY);
    }

    @Override
    public ClientSession customerCreateSession(String email, String password) {
        Optional<Customer> optCustomer = customerRepository.findByEmailAndPassword(email, password);

        if (optCustomer.isEmpty()) {
            throw new InvalidLoginException("There is no such customer");
        }
        Customer customer = optCustomer.get();
        UUID customerId = customer.getUuid();

        return ClientSession.of(customerId, ClientType.CUSTOMER);
    }

    @Override
    public String generateToken() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "")
                .substring(0, 8);
    }
}

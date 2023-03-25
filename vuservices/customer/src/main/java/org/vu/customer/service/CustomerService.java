package org.vu.customer.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.vu.customer.dto.request.CustomerRegistrationRequest;
import org.vu.customer.model.Customer;

@Service
//@AllArgsConstructor
public class CustomerService {

//    private final CustomerRepository customerRepository;

    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer
                .builder()
                .firstName(customerRegistrationRequest.getFirstName())
                .lastName(customerRegistrationRequest.getLastName())
                .email(customerRegistrationRequest.getEmail())
                .build();
    }
}

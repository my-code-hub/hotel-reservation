package com.hotel.customers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class CustomerMockDataInitializer {

    private final CustomerRepository customerRepository;

    @PostConstruct
    public void init() {
        Customer customer = new Customer();
        customer.setUsername("john.doe@mail.com");
        customerRepository.save(customer);
    }
}

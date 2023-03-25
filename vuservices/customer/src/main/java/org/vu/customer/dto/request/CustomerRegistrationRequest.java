package org.vu.customer.dto.request;

import lombok.Getter;

@Getter
public class CustomerRegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
}
